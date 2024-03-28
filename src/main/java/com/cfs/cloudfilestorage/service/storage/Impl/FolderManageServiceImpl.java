package com.cfs.cloudfilestorage.service.storage.Impl;

import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.service.storage.FolderManageService;
import com.cfs.cloudfilestorage.service.storage.Impl.command.folder.*;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.stereotype.Service;

@Service("FolderManageService")
public class FolderManageServiceImpl implements FolderManageService {

    private final StorageSwitch storageSwitch;

    public FolderManageServiceImpl(StorageSwitch storageSwitch){
        this.storageSwitch = storageSwitch;

        StorageCommand<FolderDto> createCommand = new DBCreateFolderCommand();
        createCommand.setNext(new BucketCreateFolderCommand());
        storageSwitch.register("create", createCommand);

        StorageCommand<FolderDto> uploadCommand = new DBUploadFolderCommand();
        uploadCommand.setNext(new BucketUploadFolderCommand());
        storageSwitch.register("upload", uploadCommand);

        StorageCommand<FolderDto> renameCommand = new DBRenameFolderCommand();
        renameCommand.setNext(new BucketRenameFolderCommand());
        storageSwitch.register("rename", renameCommand);

        StorageCommand<FolderDto> downloadCommand = new BucketDownloadFolderCommand();
        storageSwitch.register("download", downloadCommand);

        StorageCommand<FolderDto> removeCommand = new DBRemoveFolderCommand();
        removeCommand.setNext(new BucketRemoveFolderCommand());
        storageSwitch.register("remove", removeCommand);

        StorageCommand<FolderDto> shareCommand = new DBShareFolderCommand();
        storageSwitch.register("share", shareCommand);
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
