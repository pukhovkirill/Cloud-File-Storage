package com.cfs.cloudfilestorage.service.storage.Impl.command.file;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.StorageItem;
import com.cfs.cloudfilestorage.repository.ItemRepository;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.person.PersonService;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DBUploadFileCommand extends StorageCommand{

    private final ItemRepository itemRepository;

    private final AuthorizedPersonService authorizedPersonService;

    private final PersonService personService;

    public DBUploadFileCommand(
            PersonService personService,
            ItemRepository itemRepository,
            AuthorizedPersonService authorizedPersonService){
        this.personService = personService;
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

        var item = StorageItem.builder()
                .id(null)
                .name(entity.getName())
                .path(entity.getPath())
                .contentType(entity.getContentType())
                .lastModified(entity.getLastModified())
                .itemSize(entity.getSize())
                .ownerEmail(person.getEmail())
                .build();

        item = itemRepository.save(item);

        person.getAvailableItems().add(item);

        personService.updatePerson(person);
    }
}
