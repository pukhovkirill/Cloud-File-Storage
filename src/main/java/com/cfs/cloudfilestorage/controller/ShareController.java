package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.model.SharedItem;
import com.cfs.cloudfilestorage.repository.SharedItemRepository;
import com.cfs.cloudfilestorage.service.path.PathConvertService;
import com.cfs.cloudfilestorage.service.path.PathManageService;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import com.cfs.cloudfilestorage.util.PropertiesUtility;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ShareController extends StorageBaseController {

    private final SharedItemRepository sharedItemRepository;

    public ShareController(PathManageService pathManageService,
                           ItemManageService itemManageService,
                           PathConvertService pathConvertService,
                           AuthorizedPersonService authorizedPersonService,
                           SharedItemRepository sharedItemRepository) {
        super(pathManageService, itemManageService, pathConvertService, authorizedPersonService);
        this.sharedItemRepository = sharedItemRepository;
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

        var sharedItem = sharedItemRepository.findByItem(item);
        if(sharedItem != null){
            var newSharedItem = SharedItem.builder()
                    .id(null)
                    .item(item)
                    .isShared(data.access)
                    .build();
            sharedItem = sharedItemRepository.save(newSharedItem);
        }

        return buildShareLink(sharedItem);
    }


    private String buildShareLink(SharedItem item){
        String link = PropertiesUtility.getApplicationProperty("app.server_link");
        link += item.toString();
        return link;
    }

    @Getter
    @Setter
    @Builder
    public static class SharedData{
        private String path;
        private Boolean access;
    }
}
