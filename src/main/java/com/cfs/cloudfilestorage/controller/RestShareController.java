package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.SharedItem;
import com.cfs.cloudfilestorage.model.StorageItem;
import com.cfs.cloudfilestorage.repository.ItemRepository;
import com.cfs.cloudfilestorage.repository.SharedItemRepository;
import com.cfs.cloudfilestorage.service.path.*;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import com.cfs.cloudfilestorage.util.PropertiesUtility;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
public class RestShareController extends StorageBaseController {

    private final ItemRepository itemRepository;

    private final SharedItemRepository sharedItemRepository;

    public RestShareController(ItemRepository itemRepository,
                               ItemManageService itemManageService,
                               SharedItemRepository sharedItemRepository,
                               AuthorizedPersonService authorizedPersonService,
                               StoragePathManageService storagePathManageService,
                               PathStringRefactorService pathStringRefactorService,
                               StorageContentManageService storageContentManageService) {

        super(itemManageService, authorizedPersonService,
                storagePathManageService, pathStringRefactorService, storageContentManageService);
        this.itemRepository = itemRepository;
        this.sharedItemRepository = sharedItemRepository;
    }

    @GetMapping("/available")
    public String showAvailableItems(Model model){
        var person = findPerson();

        var content = person.getAvailableItems().stream()
                .filter(this::checkAvailability)
                .dropWhile(x -> x.getOwnerEmail().equals(person.getEmail()))
                .map(StorageEntity::new).toList();

        model.addAttribute("content", content);
        return "available";
    }

    private Boolean checkAvailability(StorageItem item){
        var currentPerson = findPerson();
        var sharedAccess = sharedItemRepository.findByItem(item);

        if(sharedAccess == null){
            return false;
        }

        return sharedAccess.getIsShared() ||
                item.getOwnerEmail().equals(currentPerson.getEmail());
    }

    @RequestMapping(value = "/share-item", method = RequestMethod.POST)
    public @ResponseBody String shareItem(@RequestBody SharedData data) throws Exception {
        var person = findPerson();

        var optItem = person.getAvailableItems().stream()
                .filter(x -> x.getPath().equals(data.path))
                .findFirst();

        if(optItem.isEmpty()){
            throw new Exception("item not found");
        }

        var item = optItem.get();

        var sharedItem = findOrPersistSharedItem(item, data);

        return buildShareLink(sharedItem);
    }

    @GetMapping("/sharing")
    public String sharing(@RequestParam("source") String source) throws Exception {
        var person = findPerson();

        var path = new String(Base64.getDecoder().decode(source.getBytes()));
        path = java.net.URLDecoder.decode(path, StandardCharsets.UTF_8);
        var item = itemRepository.findByPath(path);
        var sharedItem = sharedItemRepository.findByItem(item);

        if(sharedItem != null) {
            if(sharedItem.getIsShared()){
                if(item == null){
                    throw new Exception("item not found");
                }

                var storageEntity = new StorageEntity(item);

                if(!person.getEmail().equals(item.getOwnerEmail()))
                    itemManageService.share(storageEntity, person.getId());
            }
        }

        return "redirect:/vault";
    }

    private SharedItem findOrPersistSharedItem(StorageItem item, SharedData data) {
        var sharedItem = sharedItemRepository.findByItem(item);
        if(sharedItem == null){
            var newSharedItem = SharedItem.builder()
                    .id(null)
                    .item(item)
                    .isShared(data.access)
                    .build();
            newSharedItem = sharedItemRepository.save(newSharedItem);
            item.setSharedItem(newSharedItem);
            itemRepository.save(item);
            return newSharedItem;
        }
        sharedItem.setIsShared(data.access);
        return sharedItemRepository.save(sharedItem);
    }

    private String buildShareLink(SharedItem item) {
        String link = PropertiesUtility.getApplicationProperty("app.server_link");
        System.out.println(item.toString());
        var path = java.net.URLEncoder.encode(item.getItem().getPath(), StandardCharsets.UTF_8);
        path = new String(Base64.getEncoder().encode(path.getBytes()), StandardCharsets.UTF_8);
        link += "sharing?source="+path;
        return link;
    }

    @Getter
    @Setter
    public static class SharedData implements Serializable {
        private String path;
        private Boolean access;
    }
}
