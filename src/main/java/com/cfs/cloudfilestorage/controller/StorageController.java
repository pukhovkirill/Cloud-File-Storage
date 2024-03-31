package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.aps.PathView;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.service.path.PathConvertService;
import com.cfs.cloudfilestorage.service.path.PathManageService;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class StorageController {

    private final AuthorizedPersonService authorizedPersonService;
    private final PathManageService pathManageService;
    private final PathConvertService pathConvertService;

    public StorageController(
            AuthorizedPersonService authorizedPersonService,
            PathManageService pathManageService,
            PathConvertService pathConvertService){
        this.authorizedPersonService = authorizedPersonService;
        this.pathManageService = pathManageService;
        this.pathConvertService = pathConvertService;
    }

    @GetMapping("/vault")
    public String showRootFolder(Model model){
        var person = findPerson();
        var content = pathManageService.buildStoragePath(person.getAvailableItems());
        fillStorage(content, person, model);
        return "home";
    }

    @GetMapping("/folders/{path}")
    public String showFolders(@PathVariable String path, Model model){
        var person = findPerson();

        pathManageService.buildStoragePath(person.getAvailableItems());
        var content = pathManageService.changeDirectory(path);

        fillStorage(content, person, model);
        return "home";
    }

    private Person findPerson(){
        var optPerson = authorizedPersonService.findPerson();

        if(optPerson.isEmpty()){
            throw new UsernameNotFoundException("Person not found");
        }

        return optPerson.get();
    }

    private void fillStorage(List<StorageEntity> content, Person person, Model model){
        PathView pathView = PathView.builder()
                .path(new String[0])
                .workingDirectory("")
                .build();

        if(!content.isEmpty()){
            var element = content.getFirst();
            pathView = pathConvertService.getPathView(element.getPath());
        }

        model.addAttribute("person", person);
        model.addAttribute("content", content);
        model.addAttribute("pathView", pathView);
        model.addAttribute("root", "user-"+person.getId()+"-files");
    }
}
