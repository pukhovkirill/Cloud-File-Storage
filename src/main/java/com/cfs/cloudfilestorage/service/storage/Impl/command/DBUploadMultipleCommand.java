package com.cfs.cloudfilestorage.service.storage.Impl.command;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.repository.ItemRepository;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.person.PersonService;

public class DBUploadMultipleCommand extends DBUploadCommand {
    public DBUploadMultipleCommand(PersonService personService, ItemRepository itemRepository, AuthorizedPersonService authorizedPersonService) {
        super(personService, itemRepository, authorizedPersonService);
    }

    @Override
    protected void action(StorageEntity entity, Object... args) {
        if(args[0] instanceof StorageEntity[] entities){
            for(var item : entities){
                super.action(item);
            }
        }
    }
}
