package com.cfs.cloudfilestorage.service.storage;

import com.cfs.cloudfilestorage.dto.StorageEntity;

public interface ItemManageService extends StorageManageService{
    void uploadMultiple(StorageEntity[] storageEntities);
}
