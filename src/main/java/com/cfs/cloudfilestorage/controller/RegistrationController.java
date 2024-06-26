package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.dto.PersonDto;
import com.cfs.cloudfilestorage.service.person.PersonService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final PersonService personService;

    public RegistrationController(PersonService personService){
        this.personService = personService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        var personDto = new PersonDto();
        model.addAttribute("person", personDto);
        return "register";
    }

    @PostMapping("/register")
    public String registration( @ModelAttribute("person") @Valid PersonDto personDto,
                               Errors errors,
                               Model model){
        var existingPerson = personService.findByEmail(personDto.getEmail());

        if(errors.hasErrors()){
            model.addAttribute("person", personDto);
            return "register";
        }

        if(existingPerson.isPresent()){
            errors.rejectValue("email", null,
                    "There is already an account registered with the same email");
            return "register";
        }

        personService.savePerson(personDto);
        return "redirect:/login";
    }
}
