package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.dto.PersonDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.service.path.*;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.person.PersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController extends StorageBaseController{

    private final PersonService personService;


    public ProfileController(PersonService personService,
                             ItemManageService itemManageService,
                             AuthorizedPersonService authorizedPersonService,
                             StoragePathManageService storagePathManageService,
                             PathStringRefactorService pathStringRefactorService,
                             StorageContentManageService storageContentManageService) {

        super(itemManageService, authorizedPersonService,
                storagePathManageService, pathStringRefactorService, storageContentManageService);
        this.personService = personService;
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model){
        var person = findPerson();
        model.addAttribute("person", person);
        return "profile";
    }

    @GetMapping("/delete-account")
    public String deleteAccount(){
        var person = findPerson();

        deleteAllFiles(person);

        var personDto = PersonDto.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .email(person.getEmail())
                .phone(person.getPhone())
                .password(person.getPassword())
                .build();

        personService.deletePerson(personDto);
        return "redirect:/logout";
    }

    private void deleteAllFiles(Person person){
        var list = person.getAvailableItems().stream().map(StorageEntity::new).toList();
        var storageEntities = list.toArray((new StorageEntity[0]));
        itemManageService.fullRemove(storageEntities);
    }
}
