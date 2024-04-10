package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.path.PathConvertService;
import com.cfs.cloudfilestorage.service.path.PathManageService;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TrashController extends StorageBaseController{

    public TrashController(PathManageService pathManageService, ItemManageService itemManageService, PathConvertService pathConvertService, AuthorizedPersonService authorizedPersonService) {
        super(pathManageService, itemManageService, pathConvertService, authorizedPersonService);
    }

    @GetMapping("/trash-bin")
    public String showTrashBin(HttpSession session, Model model){
        var bin = findOrPersistTrashBin(session);
        model.addAttribute("content", bin);
        return "trashbin";
    }

    @PostMapping("/move-to-trash")
    public String moveToTrash(@RequestParam("item_id") Long id, @RequestParam("working_directory") String currentPath, HttpSession session){
        var trashBin = findOrPersistTrashBin(session);

        var item = findStorageEntityById(id);
        trashBin.add(item);

        itemManageService.moveToTrash(item);
        session.setAttribute("trashBin", trashBin);
        return String.format("redirect:%s", currentPath);
    }

    @PostMapping("/undo-from-trash")
    public String undoTrashBin(@RequestParam("item_id") Long id, HttpSession session) throws Exception {
        var bin = findTrashBin(session);
        if(bin == null)
            throw new Exception("Trash bin not found");

        var optItem = bin.stream().filter(x -> x.getId().equals(id)).findFirst();

        if(optItem.isEmpty())
            throw new Exception("Object not found");

        var item = optItem.get();
        item.setBytes(new byte[0]);
        itemManageService.undoFromTrash(item);
        bin.remove(item);
        session.setAttribute("trashBin", bin);
        return "redirect:/trash-bin";
    }

    @GetMapping("/remove-all")
    public String removeAllFromTrashBin(HttpSession session) throws Exception {
        var bin = findTrashBin(session);
        if(bin == null)
            throw new Exception("Trash bin not found");

        for(var trash : bin){
            itemManageService.removeFromTrash(trash);
        }

        session.removeAttribute("trashBin");
        return "redirect:/trash-bin";
    }

    @PostMapping("/remove")
    public String removeFromTrashBin(@RequestParam("item_id") Long id, HttpSession session) throws Exception {
        var bin = findTrashBin(session);
        if(bin == null)
            throw new Exception("Trash bin not found");

        var optItem = bin.stream().filter(x -> x.getId().equals(id)).findFirst();

        if(optItem.isEmpty())
            throw new Exception("Object not found");

        var item = optItem.get();
        itemManageService.removeFromTrash(item);
        session.setAttribute("trashBin", bin);
        return "redirect:/trash-bin";
    }

    private List<StorageEntity> findOrPersistTrashBin(HttpSession session){
        try{
            List<StorageEntity> trashBin = findTrashBin(session);

            if(trashBin == null){
                trashBin = new ArrayList<>();
            }

            return trashBin;
        }catch (Exception ex){
            var trashBin = new ArrayList<StorageEntity>();
            session.setAttribute("trashBin", trashBin);
            return trashBin;
        }
    }

    private List<StorageEntity> findTrashBin(HttpSession session){
        var bin = session.getAttribute("trashBin");
        return (ArrayList<StorageEntity>) bin;
    }

    private StorageEntity findStorageEntityById(Long id){
        var person = findPerson();

        var storageItem = person.getAvailableItems().stream()
                .filter(x -> x.getId().equals(id))
                .findFirst();

        return storageItem.map(StorageEntity::new).orElse(null);
    }
}
