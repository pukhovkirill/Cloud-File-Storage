package com.cfs.cloudfilestorage.service.storage.Impl.command.file;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;

public class DBRenameFileCommand extends StorageCommand<FileDto> {
    @Override
    protected <E extends StorageEntity> void action(E entity, Object ... args) {

    }
}
