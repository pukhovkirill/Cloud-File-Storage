package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.service.path.PathConvertService;
import com.cfs.cloudfilestorage.service.path.PathManageService;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TrashController extends StorageBaseController{

    public TrashController(PathManageService pathManageService, ItemManageService itemManageService, PathConvertService pathConvertService, AuthorizedPersonService authorizedPersonService) {
        super(pathManageService, itemManageService, pathConvertService, authorizedPersonService);
    }

    @PostMapping("/trash")
    public String moveToTrash(@RequestParam("item_id") Long id, @RequestParam("working_directory") String currentPath){
        
        return String.format("redirect: %s", currentPath);
    }


}
