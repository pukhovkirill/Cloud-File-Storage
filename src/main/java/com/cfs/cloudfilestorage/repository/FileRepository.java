package com.cfs.cloudfilestorage.repository;

import com.cfs.cloudfilestorage.model.File;
import com.cfs.cloudfilestorage.model.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface FileRepository extends CrudRepository<File, Long> {
    @Query("select f from File f where f.owner = :owner")
    List<File> findByOwner(@Param("owner") Person owner);
}
