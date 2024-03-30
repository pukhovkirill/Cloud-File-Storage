package com.cfs.cloudfilestorage.controller;


import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.path.PathManageService;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Controller
public class HomeController {

    private final AuthorizedPersonService authorizedPersonService;

    private final PathManageService pathManageService;

    public HomeController(AuthorizedPersonService authorizedPersonService, PathManageService pathManageService){
        this.authorizedPersonService = authorizedPersonService;
        this.pathManageService = pathManageService;
    }

    @GetMapping("/")
    public String showHomePage(Model model){
        if(isAuthenticated())
            return showHome(model);

        return "welcome";
    }

    @GetMapping("?folder")
    public String showFolderPage(Model model){
        return "redirect:/";
    }

    private String showHome(Model model){
        var optPerson = authorizedPersonService.findPerson();

        if(optPerson.isEmpty()){
            throw new UsernameNotFoundException("Person not found");
        }

        var person = optPerson.get();

        var content = pathManageService.buildStoragePath(person.getAvailableItems());

        List<String> pathView;

        if(content.isEmpty()){
            pathView = new LinkedList<>();
        }else{
            pathView = getPath(content.getFirst());
        }

        model.addAttribute("content", content);
        model.addAttribute("person", person);
        model.addAttribute("pathView", pathView);
        return "home";
    }

    private boolean isAuthenticated() {
        return authorizedPersonService.isAuthenticated();
    }

    private List<String> getPath(StorageEntity entity){

        if(entity == null){
            return new LinkedList<>();
        }

        var path = entity.getPath();
        var points = path.split("/");
        var count = points.length;
        return new ArrayList<>(Arrays.asList(points).subList(1, count-1));
    }
}
