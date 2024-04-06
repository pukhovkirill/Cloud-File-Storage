package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.aps.PathView;
import com.cfs.cloudfilestorage.service.path.PathConvertService;
import com.cfs.cloudfilestorage.service.path.PathManageService;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
public class StorageController extends StorageBaseController {

    public StorageController(PathManageService pathManageService,
                             ItemManageService itemManageService,
                             PathConvertService pathConvertService,
                             AuthorizedPersonService authorizedPersonService) {
        super(pathManageService, itemManageService, pathConvertService, authorizedPersonService);
    }

    @GetMapping("/vault")
    public String showRootFolder(Model model){
        Map<String, Object> attributes = new HashMap<>();

        var person = findPerson();

        pathManageService.buildStoragePath(person.getAvailableItems());
        var content = pathManageService.goToRoot();
        var allFiles = pathManageService.getAllFiles();
        var allFolders = pathManageService.getAllDirectory();

        setCurrentPath("", attributes);

        attributes.put("person", person);
        attributes.put("content", content);
        attributes.put("allFiles", allFiles);
        attributes.put("allFolders", allFolders);
        attributes.put("workingDirectory", "/vault");
        attributes.put("root", String.format("user-%d-files", person.getId()));

        fillStorage(model, attributes);
        return "home";
    }

    @GetMapping("/folders/{path}")
    public String showFolders(@PathVariable String path, Model model){
        Map<String, Object> attributes = new HashMap<>();

        var person = findPerson();

        var decodePath = new String(Base64.getDecoder().decode(path.getBytes()));
        pathManageService.buildStoragePath(person.getAvailableItems());
        var content = pathManageService.changeDirectory(decodePath);
        var allFiles = pathManageService.getAllFiles();
        var allFolders = pathManageService.getAllDirectory();

        setCurrentPath(decodePath, attributes);

        attributes.put("person", person);
        attributes.put("content", content);
        attributes.put("allFiles", allFiles);
        attributes.put("allFolders", allFolders);
        attributes.put("workingDirectory", String.format("/folders/%s", path));
        attributes.put("root", String.format("user-%d-files", person.getId()));

        fillStorage(model, attributes);
        return "home";
    }

    @GetMapping("/previous/{path}/details")
    public String showPreviousFolder(@PathVariable String path, @RequestParam("index") Integer index){

        var previous = getPreviousPath(path, index);
        previous = Base64.getEncoder().encodeToString(previous.getBytes());

        return String.format("redirect:/folders/%s", previous);
    }

    private String getPreviousPath(String path, int index){
        var decodePath = new String(Base64.getDecoder().decode(path.getBytes()));

        var pathView = pathConvertService.getPathView(decodePath);
        StringBuilder newPath = new StringBuilder();

        for(int i = 0; i <= index; i++){
            newPath.append(pathView.getPath()[i]).append("/");
        }

        return newPath.toString();
    }

    private void setCurrentPath(String currentPath, Map<String, Object> attributes){
        PathView pathView;
        if(currentPath.isEmpty()){
            pathView = PathView.builder()
                    .path(new String[0])
                    .workingDirectory("")
                    .fullPath("")
                    .build();
        }else{
            pathView = pathConvertService.getPathView(currentPath);
        }
        attributes.put("pathView", pathView);
    }

    private void fillStorage(Model model, Map<String, Object> attributes){
        for(var key : attributes.keySet()){
            model.addAttribute(key, attributes.get(key));
        }
    }
}
