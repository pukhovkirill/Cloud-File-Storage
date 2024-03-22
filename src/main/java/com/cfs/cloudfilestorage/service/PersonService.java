package com.cfs.cloudfilestorage.service;

import com.cfs.cloudfilestorage.dto.PersonDto;
import com.cfs.cloudfilestorage.model.Person;

public interface PersonService {
    void savePerson(PersonDto person);
    Person findByEmail(String email);
    void deletePerson(PersonDto person);
}
