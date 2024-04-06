package com.cfs.cloudfilestorage.service.storage.Impl;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ItemManageServiceImpl implements ItemManageService {
    private final StorageSwitch storageSwitch;

    public ItemManageServiceImpl(@Qualifier("fileStorageSwitch") StorageSwitch storageSwitch){
        this.storageSwitch = storageSwitch;
    }

    @Override
    public StorageEntity create(StorageEntity item) {
        return null;
    }

    @Override
    public StorageEntity upload(StorageEntity item) {
        storageSwitch.execute("upload", item);
        return item;
    }

    @Override
    public void uploadMultiple(StorageEntity[] StorageEntityArray) {
        storageSwitch.execute("upload_multiple", null, (Object) StorageEntityArray);
    }

    @Override
    public StorageEntity rename(StorageEntity item, String newName, String path) {
        storageSwitch.execute("rename", item, newName, path);
        item.setName(newName);
        item.setPath(path);
        return item;
    }

    @Override
    public StorageEntity download(StorageEntity item) {
        storageSwitch.execute("download", item);
        return item;
    }

    @Override
    public StorageEntity remove(StorageEntity item) {
        storageSwitch.execute("remove", item);
        return item;
    }

    @Override
    public StorageEntity share(StorageEntity item, Long userId) {
        storageSwitch.execute("share", item, userId);
        return item;
    }
}
