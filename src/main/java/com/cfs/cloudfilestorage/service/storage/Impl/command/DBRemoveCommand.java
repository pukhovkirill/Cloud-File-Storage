package com.cfs.cloudfilestorage.service.storage.Impl.command;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.repository.ItemRepository;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DBRemoveCommand extends StorageCommand {

    private final ItemRepository itemRepository;
    private final AuthorizedPersonService authorizedPersonService;

    public DBRemoveCommand(ItemRepository itemRepository,
                           AuthorizedPersonService authorizedPersonService){
        this.itemRepository = itemRepository;
        this.authorizedPersonService = authorizedPersonService;
    }

    @Override
    protected void action(StorageEntity entity, Object ... args) {
        var optPerson = authorizedPersonService.findPerson();

        if(optPerson.isEmpty()){
            throw new UsernameNotFoundException("Person not found");
        }

        var person = optPerson.get();

        var optItem = itemRepository.findById(entity.getId());

        if(optItem.isEmpty()){
            return;
        }

        var item = optItem.get();

        person.getAvailableItems().remove(item);

        itemRepository.delete(item);
    }
}
