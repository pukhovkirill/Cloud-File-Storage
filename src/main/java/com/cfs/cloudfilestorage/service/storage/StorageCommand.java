package com.cfs.cloudfilestorage.service.storage;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.repository.PersonRepository;
import com.cfs.cloudfilestorage.service.person.PersonService;
import com.cfs.cloudfilestorage.service.person.PersonServiceImpl;
import com.cfs.cloudfilestorage.util.PropertiesUtility;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

public abstract class StorageCommand<T extends StorageEntity>{
    protected final static String BUCKET_NAME;

    static{
        BUCKET_NAME = PropertiesUtility.getApplicationProperty("app.minio_bucket_name");
    }

    protected StorageCommand<T> next;

    public StorageCommand<T> setNext(StorageCommand<T> next){
        this.next = next;
        return next;
    }

    public <E extends StorageEntity> void execute(E entity, Object ... args) {
        action(entity, args);

        if (next != null) {
            next.execute(entity, args);
        }
    }

    protected Optional<Person> findPerson(PersonService personService){
        var details = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(details instanceof UserDetails user)
            return personService.findByEmail(user.getUsername());

        return Optional.empty();
    }

    protected abstract <E extends StorageEntity> void action(E entity, Object ... args);
}
