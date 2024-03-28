package com.cfs.cloudfilestorage.service.storage.Impl;

import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.service.storage.FolderManageService;
import com.cfs.cloudfilestorage.service.storage.Impl.command.folder.*;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("FolderManageService")
public class FolderManageServiceImpl implements FolderManageService {

    private final StorageSwitch storageSwitch;

    public FolderManageServiceImpl(@Qualifier("folderStorageSwitch") StorageSwitch storageSwitch){
        this.storageSwitch = storageSwitch;
    }

    @Override
    public FolderDto create(FolderDto item) {
        storageSwitch.execute("create", item);
        return item;
    }

    @Override
    public FolderDto upload(FolderDto item) {
        storageSwitch.execute("upload", item);
        return item;
    }

    @Override
    public FolderDto rename(FolderDto item, String newName) {
        storageSwitch.execute("rename", item, newName);
        return item;
    }

    @Override
    public FolderDto download(FolderDto item) {
        storageSwitch.execute("download", item);
        return item;
    }

    @Override
    public FolderDto remove(FolderDto item) {
        storageSwitch.execute("remove", item);
        return item;
    }

    @Override
    public FolderDto share(FolderDto item, Long userId) {
        storageSwitch.execute("share", item, userId);
        return item;
    }
}
