package com.cfs.cloudfilestorage.service.storage;

import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.service.person.PersonService;
import com.cfs.cloudfilestorage.util.PropertiesUtility;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public abstract class BaseManager {
    protected final static String BUCKET_NAME;

    protected final PersonService personService;

    static{
        BUCKET_NAME = PropertiesUtility.getApplicationProperty("app.minio_bucket_name");
    }

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
