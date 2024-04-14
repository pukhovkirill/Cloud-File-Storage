package com.cfs.cloudfilestorage.service.storage.Impl.command;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.repository.ItemRepository;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.FileNotFoundException;

public class DBRenameCommand extends StorageCommand {

    private final ItemRepository itemRepository;
    private final AuthorizedPersonService authorizedPersonService;

    public DBRenameCommand(ItemRepository itemRepository,
    AuthorizedPersonService authorizedPersonService){
        this.itemRepository = itemRepository;
        this.authorizedPersonService = authorizedPersonService;
    }

    @Override
    protected void action(StorageEntity entity, Object ... args) {
        String newFileName = (String) args[0];
        String newFilePath = (String) args[1];
        try {
            var optItem = itemRepository.findById(entity.getId());
            var optPerson = authorizedPersonService.findPerson();

            if(optItem.isEmpty()){
                throw new FileNotFoundException();
            }

            if(optPerson.isEmpty()){
                throw new UsernameNotFoundException("User not found");
            }

            var item = optItem.get();
            var person = optPerson.get();

            if(item.getOwnerEmail().equals(person.getEmail())){
                item.setName(newFileName);
                item.setPath(newFilePath);
                itemRepository.save(item);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
