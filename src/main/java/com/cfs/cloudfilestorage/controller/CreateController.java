package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.path.PathConvertService;
import com.cfs.cloudfilestorage.service.path.PathManageService;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.util.Base64;

@Controller
public class CreateController extends StorageBaseController {

    public CreateController(PathManageService pathManageService,
                            ItemManageService itemManageService,
                            PathConvertService pathConvertService,
                            AuthorizedPersonService authorizedPersonService) {
        super(pathManageService, itemManageService, pathConvertService, authorizedPersonService);
    }

    @PostMapping("/create-folder")
    public String createFolder(@RequestParam("folder_name") String folderName, @RequestParam("current_path") String currentPath){

        folderName = pathConvertService.createFolderName(folderName, currentPath);

        var person = findPerson();

        pathManageService.buildStoragePath(person.getAvailableItems());
        var found = pathManageService.pathExists(folderName);

        if(found){
            return "redirect:/vault";
        }

        var folder = StorageEntity.builder()
                        .id(null)
                        .name(pathConvertService.getFileName(folderName))
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
