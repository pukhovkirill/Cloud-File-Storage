package com.cfs.cloudfilestorage.service.storage.Impl.command.folder;

import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.repository.FolderRepository;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.beans.factory.annotation.Autowired;

public class DBRemoveFolderCommand extends StorageCommand<FolderDto> {

    @Autowired
    private FolderRepository folderRepository;

    @Override
    protected <E extends StorageEntity> void action(E entity, Object... args) {
        if(entity instanceof FolderDto item){
            folderRepository.deleteById(item.getId());
        }
    }
}