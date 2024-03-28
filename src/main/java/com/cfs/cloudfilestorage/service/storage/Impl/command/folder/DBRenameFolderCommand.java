package com.cfs.cloudfilestorage.service.storage.Impl.command.folder;

import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.repository.FolderRepository;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;

public class DBRenameFolderCommand extends StorageCommand<FolderDto> {

    private final FolderRepository folderRepository;

    public DBRenameFolderCommand(FolderRepository folderRepository){
        this.folderRepository = folderRepository;
    }

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
