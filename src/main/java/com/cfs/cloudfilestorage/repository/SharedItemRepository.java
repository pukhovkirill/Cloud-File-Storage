package com.cfs.cloudfilestorage.repository;

import com.cfs.cloudfilestorage.model.SharedItem;
import com.cfs.cloudfilestorage.model.StorageItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SharedItemRepository extends CrudRepository<SharedItem, Long> {
    @Query("select i from SharedItem i where i.item = :item")
    SharedItem findByItem(@Param("item")StorageItem item);
}
