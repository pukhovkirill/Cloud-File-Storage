package com.cfs.cloudfilestorage.repository;

import com.cfs.cloudfilestorage.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
    Person findByEmail(final String email);
}
