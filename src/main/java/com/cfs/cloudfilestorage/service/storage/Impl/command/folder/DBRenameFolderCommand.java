package com.cfs.cloudfilestorage.service.storage.Impl.command.folder;

import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.repository.FolderRepository;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.beans.factory.annotation.Autowired;

public class DBRenameFolderCommand extends StorageCommand<FolderDto> {

    @Autowired
    private FolderRepository folderRepository;

    @Override
    protected <E extends StorageEntity> void action(E entity, Object... args) {
        String newFolderName = (String) args[0];
        if(entity instanceof FolderDto item){
            var optFolder = folderRepository.findById(entity.getId());

            if(optFolder.isEmpty()){
                return;
            }

            var folder = optFolder.get();
            folder.setName(newFolderName);

            folderRepository.save(folder);
        }
    }
}
