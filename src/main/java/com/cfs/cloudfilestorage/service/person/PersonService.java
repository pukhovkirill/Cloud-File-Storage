package com.cfs.cloudfilestorage.service.person;

import com.cfs.cloudfilestorage.dto.PersonDto;
import com.cfs.cloudfilestorage.model.Person;

import java.util.Optional;

public interface PersonService {

    void savePerson(PersonDto person);
    Optional<Person> findByEmail(String email);
    Optional<Person> findById(Long id);
    void deletePerson(PersonDto person);
    void updatePerson(Person person);
}
