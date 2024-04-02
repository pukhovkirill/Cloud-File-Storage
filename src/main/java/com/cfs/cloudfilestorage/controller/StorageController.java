package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.aps.PathView;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.service.path.PathConvertService;
import com.cfs.cloudfilestorage.service.path.PathManageService;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Base64;
import java.util.List;

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
        var person = findPerson();

        pathManageService.buildStoragePath(person.getAvailableItems());
        var content = pathManageService.goToRoot();

        fillStorage(content, person, model, null, "/vault");
        return "home";
    }

    @GetMapping("/folders/{path}")
    public String showFolders(@PathVariable String path, Model model){
        var person = findPerson();

        var decodePath = new String(Base64.getDecoder().decode(path.getBytes()));
        pathManageService.buildStoragePath(person.getAvailableItems());
        var content = pathManageService.changeDirectory(decodePath);

        fillStorage(content, person, model, decodePath, String.format("/folders/%s", path));
        return "home";
    }

    @GetMapping("/previous/{path}")
    public String showPreviousFolder(@PathVariable String path){

        var decodePath = new String(Base64.getDecoder().decode(path.getBytes()));
        decodePath = pathConvertService.getParent(decodePath);
        decodePath = Base64.getEncoder().encodeToString(decodePath.getBytes());

        return String.format("redirect:/folders/%s", decodePath);
    }


    private void fillStorage(List<StorageEntity> content, Person person, Model model, String currentPath, String workingDirectory){
        PathView pathView;
        if(currentPath == null){
            pathView = PathView.builder()
                    .path(new String[0])
                    .workingDirectory("")
                    .build();
        }else{
            pathView = pathConvertService.getPathView(currentPath);
        }

        model.addAttribute("person", person);
        model.addAttribute("content", content);
        model.addAttribute("pathView", pathView);
        model.addAttribute("workingDirectory", workingDirectory);
        model.addAttribute("root", String.format("user-%d-files", person.getId()));
    }
}
