package com.cfs.cloudfilestorage.service.storage.Impl;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.service.storage.FileManageService;
import com.cfs.cloudfilestorage.service.storage.Impl.command.file.*;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("FileManageService")
public class FileManageServiceImpl implements FileManageService {

    private final StorageSwitch storageSwitch;

    public FileManageServiceImpl(@Qualifier("fileStorageSwitch") StorageSwitch storageSwitch){
        this.storageSwitch = storageSwitch;
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
    public void uploadMultiple(FileDto[] fileDtoArray) {
        storageSwitch.execute("upload_multiple", null, (Object) fileDtoArray);
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
