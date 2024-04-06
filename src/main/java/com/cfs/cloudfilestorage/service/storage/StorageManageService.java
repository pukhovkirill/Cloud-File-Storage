package com.cfs.cloudfilestorage.service.storage;

import com.cfs.cloudfilestorage.dto.StorageEntity;

public interface StorageManageService{

    StorageEntity create(StorageEntity item);

    StorageEntity upload(StorageEntity item);

    StorageEntity rename(StorageEntity item, String newName, String newPath);

    StorageEntity download(StorageEntity item);

    StorageEntity remove(StorageEntity item);

    StorageEntity share(StorageEntity item, Long userId);
}
