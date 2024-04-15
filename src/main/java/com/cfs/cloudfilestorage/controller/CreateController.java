package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.path.*;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;

@Controller
public class CreateController extends StorageBaseController {

    public CreateController(ItemManageService itemManageService,
                            AuthorizedPersonService authorizedPersonService,
                            StoragePathManageService storagePathManageService,
                            PathStringRefactorService pathStringRefactorService,
                            StorageContentManageService storageContentManageService) {

        super(itemManageService, authorizedPersonService,
                storagePathManageService, pathStringRefactorService, storageContentManageService);
    }

    @PostMapping("/create-folder")
    public String createFolder(@RequestParam("folder_name") String folderName, @RequestParam("current_path") String currentPath){

        folderName = storagePathManageService.createFolderName(folderName, currentPath);

        var person = findPerson();

        storageContentManageService.buildStoragePath(person.getAvailableItems());
        var found = storageContentManageService.pathExists(folderName);

        if(found){
            return "redirect:/vault";
        }

        var folder = StorageEntity.builder()
                        .id(null)
                        .name(pathStringRefactorService.getOriginalName(folderName))
                        .path(folderName)
                        .contentType("folder")
                        .lastModified(new Timestamp(System.currentTimeMillis()))
                        .bytes(new byte[0])
                        .owner(person.getEmail())
                        .build();


        itemManageService.upload(folder);

        return String.format("redirect:%s", currentPath);
    }
}
