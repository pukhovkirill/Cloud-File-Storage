package com.cfs.cloudfilestorage.service.storage.Impl.command.folder;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.repository.FolderRepository;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.FileNotFoundException;
import java.nio.file.DirectoryNotEmptyException;

public class DBShareFolderCommand extends StorageCommand<FolderDto> {

    @Autowired
    private FolderRepository folderRepository;

    @Override
    protected <E extends StorageEntity> void action(E entity, Object... args) {
        var optionalPerson = personService.findById((Long) args[0]);

        try {
            if(optionalPerson.isEmpty()){
                throw new UsernameNotFoundException("Person not found");
            }

            var person = optionalPerson.get();

            if(entity instanceof FileDto item){
                var optionalFolder = folderRepository.findById(item.getId());
                if(optionalFolder.isEmpty()){
                    return;
                }

                var folder = optionalFolder.get();

                person.getAvailableFolders().add(folder);

                personService.updatePerson(person);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
