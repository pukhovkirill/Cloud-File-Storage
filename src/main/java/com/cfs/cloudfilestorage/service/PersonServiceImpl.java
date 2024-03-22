package com.cfs.cloudfilestorage.service;

import com.cfs.cloudfilestorage.dto.PersonDto;
import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.repository.PersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService{
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PersonServiceImpl(PersonRepository personRepository, PasswordEncoder passwordEncoder){
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void savePerson(PersonDto person) {
        var newPerson = new Person();
        newPerson.setFirstName(person.getFirstName());
        newPerson.setLastName(person.getLastName());
        newPerson.setEmail(person.getEmail());
        newPerson.setPhone(person.getPhone());
        newPerson.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepository.save(newPerson);
    }

    @Override
    public Person findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    @Override
    public void deletePerson(PersonDto person) {
        personRepository.deleteById(person.getId());
    }
}
