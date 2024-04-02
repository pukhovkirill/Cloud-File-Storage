package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.service.path.PathConvertService;
import com.cfs.cloudfilestorage.service.path.PathManageService;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class StorageBaseController {

    protected final ItemManageService itemManageService;
    protected final PathManageService pathManageService;
    protected final PathConvertService pathConvertService;
    protected final AuthorizedPersonService authorizedPersonService;

    public StorageBaseController(PathManageService pathManageService,
                            ItemManageService itemManageService,
                            PathConvertService pathConvertService,
                            AuthorizedPersonService authorizedPersonService) {
        this.itemManageService = itemManageService;
        this.pathManageService = pathManageService;
        this.pathConvertService = pathConvertService;
        this.authorizedPersonService = authorizedPersonService;
    }

    protected Person findPerson(){
        var optPerson = authorizedPersonService.findPerson();

        if(optPerson.isEmpty()){
            throw new UsernameNotFoundException("Person not found");
        }

        return optPerson.get();
    }
}
