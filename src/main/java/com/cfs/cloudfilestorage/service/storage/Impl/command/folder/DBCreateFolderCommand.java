package com.cfs.cloudfilestorage.service.storage.Impl.command.folder;

import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.Folder;
import com.cfs.cloudfilestorage.repository.FolderRepository;
import com.cfs.cloudfilestorage.service.person.PersonService;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DBCreateFolderCommand extends StorageCommand<FolderDto> {

    private final FolderRepository folderRepository;

    private final PersonService personService;

    public DBCreateFolderCommand(FolderRepository folderRepository, PersonService personService){
        this.folderRepository = folderRepository;
        this.personService = personService;
    }

    @Override
    protected <E extends StorageEntity> void action(E entity, Object... args) {
        if(entity instanceof FolderDto item){

            if(folderRepository.findById(item.getId()).isPresent()){
                return;
            }

            var optionalPerson = findPerson(personService);

            if(optionalPerson.isEmpty()){
                throw new UsernameNotFoundException("Person not found");
            }

            var person = optionalPerson.get();

            var folder = new Folder(item.getId(), item.getName(), person, null);

            folderRepository.save(folder);
        }
    }
}
