package com.cfs.cloudfilestorage.service.person;

import com.cfs.cloudfilestorage.model.Person;

import java.util.Optional;

public interface AuthorizedPersonService {
    Optional<Person> findPerson();

    boolean isAuthenticated();
}
