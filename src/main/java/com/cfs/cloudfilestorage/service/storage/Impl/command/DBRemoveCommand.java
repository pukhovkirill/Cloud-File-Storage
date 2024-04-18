package com.cfs.cloudfilestorage.service.storage.Impl.command;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.repository.ItemRepository;
import com.cfs.cloudfilestorage.repository.PersonRepository;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DBRemoveCommand extends StorageCommand {

    private final ItemRepository itemRepository;
    private final PersonRepository personRepository;
    private final AuthorizedPersonService authorizedPersonService;

    public DBRemoveCommand(ItemRepository itemRepository,
                           PersonRepository personRepository,
                           AuthorizedPersonService authorizedPersonService){
        this.itemRepository = itemRepository;
        this.personRepository = personRepository;
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

        var itemUsers = item.getPeople();
        for(var user : itemUsers){
            user.getAvailableItems().remove(item);
            personRepository.save(user);
        }

        if(item.getOwnerEmail().equals(person.getEmail()))
            itemRepository.delete(item);
    }
}
