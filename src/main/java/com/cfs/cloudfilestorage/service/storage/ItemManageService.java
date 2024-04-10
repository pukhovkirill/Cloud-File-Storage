package com.cfs.cloudfilestorage.service.storage;

import com.cfs.cloudfilestorage.dto.StorageEntity;

public interface ItemManageService extends StorageManageService{
    void uploadMultiple(StorageEntity[] items);

    void moveToTrash(StorageEntity item);

    void undoFromTrash(StorageEntity item);

    void removeFromTrash(StorageEntity item);
}
