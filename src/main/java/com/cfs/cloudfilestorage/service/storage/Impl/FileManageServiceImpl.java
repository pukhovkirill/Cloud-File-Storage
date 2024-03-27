package com.cfs.cloudfilestorage.service.storage.Impl;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.service.storage.FileManageService;
import com.cfs.cloudfilestorage.service.storage.Impl.command.file.*;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.stereotype.Service;

@Service("FileManageService")
public class FileManageServiceImpl implements FileManageService {

    private final StorageSwitch storageSwitch;

    public FileManageServiceImpl(StorageSwitch storageSwitch){
        this.storageSwitch = storageSwitch;

        StorageCommand<FileDto> uploadCommand = new DBUploadFileCommand();
        uploadCommand.setNext(new BucketUploadFileCommand());
        storageSwitch.register("upload", uploadCommand);

        StorageCommand<FileDto> downloadCommand = new BucketDownloadFileCommand();
        storageSwitch.register("download", downloadCommand);

        StorageCommand<FileDto> removeCommand = new DBRemoveFileCommand();
        removeCommand.setNext(new BucketRemoveFileCommand());
        storageSwitch.register("remove", removeCommand);

        StorageCommand<FileDto> renameCommand = new DBRenameFileCommand();
        renameCommand.setNext(new BucketRenameFileCommand());
        storageSwitch.register("rename", renameCommand);

        StorageCommand<FileDto> shareCommand = new DBShareFileCommand();
        storageSwitch.register("share", shareCommand);

    }

    @Override
    public FileDto create(FileDto item) {
        return null;
    }

    @Override
    public FileDto upload(FileDto item) {
        storageSwitch.execute("upload", item);
        return item;
    }

    @Override
    public FileDto rename(FileDto item, String newName) {
        storageSwitch.execute("rename", item, newName);
        item.setName(newName);
        return item;
    }

    @Override
    public FileDto download(FileDto item) {
        storageSwitch.execute("download", item);
        return item;
    }

    @Override
    public FileDto remove(FileDto item) {
        storageSwitch.execute("remove", item);
        return item;
    }

    @Override
    public FileDto share(FileDto item, Long userId) {
        storageSwitch.execute("share", item, userId);
        return item;
    }
}
