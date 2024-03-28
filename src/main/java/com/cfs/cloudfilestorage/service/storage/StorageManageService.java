package com.cfs.cloudfilestorage.service.storage;

import com.cfs.cloudfilestorage.dto.StorageEntity;

public interface StorageManageService<T extends StorageEntity>{

    T create(T item);

    T upload(T item);

    T rename(T item, String newName);

    T download(T item);

    T remove(T item);

    T share(T item, Long userId);
}
