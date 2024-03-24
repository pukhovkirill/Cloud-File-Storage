package com.cfs.cloudfilestorage.service.storage;

import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.service.person.PersonService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public abstract class BaseManager {

    protected final PersonService personService;

    public BaseManager(PersonService personService){
        this.personService = personService;
    }

    protected Optional<Person> findPerson(){
        var details = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(details instanceof UserDetails user)
            return personService.findByEmail(user.getUsername());

        return Optional.empty();
    }
}
