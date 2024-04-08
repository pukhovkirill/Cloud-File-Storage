package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final AuthorizedPersonService authorizedPersonService;

    public HomeController(AuthorizedPersonService authorizedPersonService){
        this.authorizedPersonService = authorizedPersonService;
    }

    @GetMapping("/")
    public String showHomePage(){
        if(isAuthenticated())
            return "redirect:/vault";
        return "welcome";
    }

    private boolean isAuthenticated() {
        return authorizedPersonService.isAuthenticated();
    }
}
