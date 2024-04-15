package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.path.*;
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


    public TrashController(ItemManageService itemManageService,
                           AuthorizedPersonService authorizedPersonService,
                           StoragePathManageService storagePathManageService,
                           PathStringRefactorService pathStringRefactorService,
                           StorageContentManageService storageContentManageService) {

        super(itemManageService, authorizedPersonService,
                storagePathManageService, pathStringRefactorService, storageContentManageService);
    }

    @GetMapping("/trash-bin")
    public String showTrashBin(HttpSession session, Model model){
        var trashBin = findOrPersistTrashBin(session);

        var bin = new ArrayList<StorageEntity>();
        for(int i = 0; i < trashBin.size(); i++){
            var trash = trashBin.get(i);
            if(trash.getContentType().equals("folder")){
                var folderPath = trash.getPath();
                var next = trashBin.stream()
                        .filter(x -> x.getPath().startsWith(folderPath))
                        .dropWhile(x -> x.getContentType().equals("folder"))
                        .count();
                i += (int)next;
                bin.add(trash);
                continue;
            }
            bin.add(trash);
        }

        model.addAttribute("content", bin);
        return "trashbin";
    }

    @PostMapping("/move-to-trash")
    public String moveToTrashBin(@RequestParam("path") String path, @RequestParam("working_directory") String currentPath, HttpSession session) throws Exception {
        var item = findStorageEntity(path);
        putToTrashBin(item, session);

        return String.format("redirect:%s", currentPath);
    }

    @PostMapping("/undo-from-trash")
    public String undoFromTrashBin(@RequestParam("path") String path, HttpSession session) throws Exception {
        var bin = findTrashBin(session);
        pullFromTrashBin(bin, path, session);

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
    public String removeFromTrashBin(@RequestParam("path") String path, HttpSession session) throws Exception {
        var bin = findTrashBin(session);
        var item = findStorageEntityInTrashBin(path, bin);
        itemManageService.removeFromTrash(item);
        session.setAttribute("trashBin", bin);
        return "redirect:/trash-bin";
    }

    @PostMapping("/remove-shared")
    public String removeSharedFromTrashBin(@RequestParam("path") String path) throws Exception {
        var item = findStorageEntity(path);
        itemManageService.removeShare(item);
        return "redirect:/vault";
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

    private StorageEntity findStorageEntityInTrashBin(String path, List<StorageEntity> trashBin) throws Exception {
        if(trashBin == null)
            throw new Exception("Trash bin not found");

        var optItem = trashBin.stream().filter(x -> x.getPath().equals(path)).findFirst();

        if(optItem.isEmpty())
            throw new Exception("Object not found");

        return optItem.get();
    }

    private StorageEntity findStorageEntity(String path) throws Exception {
        var person = findPerson();

        var storageItem = person.getAvailableItems().stream()
                .filter(x -> x.getPath().equals(path))
                .findFirst();

        if(storageItem.isEmpty()){
            throw new Exception("Object not found");
        }

        return new StorageEntity(storageItem.get());
    }

    private void putToTrashBin(StorageEntity entity, HttpSession session) {
        var trashBin = findOrPersistTrashBin(session);

        if(entity.getContentType().equals("folder"))
            putFolderToTrashBin(entity, trashBin);
        else putFileToTrashBin(entity, trashBin);

        session.setAttribute("trashBin", trashBin);
    }

    private void putFolderToTrashBin(StorageEntity item, List<StorageEntity> trashBin) {
        var person = findPerson();
        var path = item.getPath();

        var files = person.getAvailableItems().stream()
                .filter(x -> x.getPath().startsWith(path))
                .map(StorageEntity::new).toList();

        for(var file : files){
            itemManageService.moveToTrash(file);
            trashBin.add(file);
        }
    }

    private void putFileToTrashBin(StorageEntity item, List<StorageEntity> trashBin){
        trashBin.add(item);
        itemManageService.moveToTrash(item);
    }

    private void pullFromTrashBin(List<StorageEntity> trashBin, String path, HttpSession session) throws Exception {
        var item = findStorageEntityInTrashBin(path, trashBin);

        if(item.getContentType().equals("folder"))
            pullFolderFromTrash(item, trashBin);
        else pullFileFromTrashBin(item, trashBin);

        session.setAttribute("trashBin", trashBin);
    }

    private void pullFolderFromTrash(StorageEntity item, List<StorageEntity> trashBin) {
        var path = item.getPath();

        var files = trashBin.stream()
                .filter(x -> x.getPath().startsWith(path))
                .toList();

        for(var file : files){
            itemManageService.undoFromTrash(file);
            trashBin.remove(file);
        }
    }

    private void pullFileFromTrashBin(StorageEntity item, List<StorageEntity> trashBin) {
        itemManageService.undoFromTrash(item);
        trashBin.remove(item);
    }
}
