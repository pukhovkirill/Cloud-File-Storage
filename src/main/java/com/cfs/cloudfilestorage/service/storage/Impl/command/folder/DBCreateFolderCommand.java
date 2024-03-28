package com.cfs.cloudfilestorage.service.storage.Impl.command.folder;

import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.Folder;
import com.cfs.cloudfilestorage.repository.FolderRepository;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DBCreateFolderCommand extends StorageCommand<FolderDto> {

    @Autowired
    private FolderRepository folderRepository;

    @Override
    protected <E extends StorageEntity> void action(E entity, Object... args) {
        if(entity instanceof FolderDto item){

            if(folderRepository.findById(item.getId()).isPresent()){
                return;
            }

            var optionalPerson = findPerson();

            if(optionalPerson.isEmpty()){
                throw new UsernameNotFoundException("Person not found");
            }

            var person = optionalPerson.get();

            var folder = new Folder(item.getId(), item.getName(), person, null);

            folderRepository.save(folder);
        }
    }
}
