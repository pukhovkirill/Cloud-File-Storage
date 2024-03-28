package com.cfs.cloudfilestorage.service.storage.Impl.command.file;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.repository.FileRepository;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;

public class DBRemoveFileCommand extends StorageCommand<FileDto> {

    private final FileRepository fileRepository;

    public DBRemoveFileCommand(FileRepository fileRepository){
        this.fileRepository = fileRepository;
    }

    @Override
    protected <E extends StorageEntity> void action(E entity, Object ... args) {
        if(entity instanceof FileDto item)
            fileRepository.deleteById(item.getId());
    }
}
