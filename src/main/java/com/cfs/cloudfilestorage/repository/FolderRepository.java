package com.cfs.cloudfilestorage.repository;

import com.cfs.cloudfilestorage.model.Folder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface FolderRepository extends CrudRepository<Folder, Long> {

}
