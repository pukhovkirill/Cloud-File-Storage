package com.cfs.cloudfilestorage.repository;

import com.cfs.cloudfilestorage.model.StorageItem;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<StorageItem, Long> {
    StorageItem findByPath(String path);
}
