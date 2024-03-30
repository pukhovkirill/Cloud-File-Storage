package com.cfs.cloudfilestorage.controller;


import com.cfs.cloudfilestorage.service.path.PathManageService;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    private String showHome(Model model){
        var optPerson = authorizedPersonService.findPerson();

        if(optPerson.isEmpty()){
            throw new UsernameNotFoundException("Person not found");
        }

        var person = optPerson.get();

        var content = pathManageService.buildStoragePath(person.getAvailableFiles());
        model.addAttribute("content", content);
        model.addAttribute("person", person);
        return "home";
    }

    private boolean isAuthenticated() {
        return authorizedPersonService.isAuthenticated();
    }
}
