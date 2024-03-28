package com.cfs.cloudfilestorage.repository;

import com.cfs.cloudfilestorage.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


public interface PersonRepository extends CrudRepository<Person, Long> {
    Person findByEmail(final String email);
}
