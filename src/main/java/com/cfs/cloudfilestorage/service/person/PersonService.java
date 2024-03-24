package com.cfs.cloudfilestorage.service.person;

import com.cfs.cloudfilestorage.dto.PersonDto;
import com.cfs.cloudfilestorage.model.Person;

import java.util.Optional;

public interface PersonService {
    void savePerson(PersonDto person);
    Optional<Person> findByEmail(String email);
    void deletePerson(PersonDto person);
}
