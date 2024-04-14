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
    public void uploadMultiple(StorageEntity[] storageEntityArray) {
        storageSwitch.execute("upload_multiple", null, (Object) storageEntityArray);
    }

    @Override
    public void moveToTrash(StorageEntity storageEntity) {
        storageSwitch.execute("move_to_trash", storageEntity);
    }

    @Override
    public void undoFromTrash(StorageEntity storageEntity) {
        storageSwitch.execute("undo_from_trash", storageEntity);
    }

    @Override
    public void removeFromTrash(StorageEntity item) {
        storageSwitch.execute("remove_from_trash", item);
    }

    @Override
    public void removeShare(StorageEntity item) {
        storageSwitch.execute("remove_share", item);
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
    public StorageEntity share(StorageEntity item, Long userId) {
        storageSwitch.execute("share", item, userId);
        return item;
    }
}
