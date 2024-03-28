package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.service.storage.FileManageService;
import com.cfs.cloudfilestorage.service.storage.StorageManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;

@Controller
public class HomeController {

    private final FileManageService service;

    public HomeController(FileManageService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String showHomePage(){
        if(isAuthenticated())
            return "home";
        return "welcome";
    }

    private boolean isAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        var fileDto = new FileDto();
        fileDto.setName(file.getOriginalFilename());
        fileDto.setPath(file.getOriginalFilename());
        fileDto.setContentType(file.getContentType());
        fileDto.setLastModified(new Timestamp(System.currentTimeMillis()));
        fileDto.setFileSize((int) file.getSize());
        fileDto.setBytes(file.getBytes());

        service.upload(fileDto);
        return "redirect:/";
    }
}
