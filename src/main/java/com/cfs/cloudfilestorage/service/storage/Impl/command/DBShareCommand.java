package com.cfs.cloudfilestorage.service.storage.Impl.command;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.repository.ItemRepository;
import com.cfs.cloudfilestorage.service.person.PersonService;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.FileNotFoundException;

public class DBShareCommand extends StorageCommand {

    private final ItemRepository itemRepository;

    private final PersonService personService;

    public DBShareCommand(ItemRepository itemRepository, PersonService personService){
        this.itemRepository = itemRepository;
        this.personService = personService;
    }

    @Override
    protected void action(StorageEntity entity, Object ... args) {
        var optPerson = personService.findById((Long) args[0]);

        try {
            if(optPerson.isEmpty()){
                throw new UsernameNotFoundException("Person not found");
            }

            var person = optPerson.get();

            var optItem = itemRepository.findById(entity.getId());
            if(optItem.isEmpty()){
                throw new FileNotFoundException();
            }

            var item = optItem.get();

            if(!person.getEmail().equals(item.getOwnerEmail())){
                if(!person.getAvailableItems().contains(item)){
                    person.getAvailableItems().add(item);
                    personService.updatePerson(person);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
