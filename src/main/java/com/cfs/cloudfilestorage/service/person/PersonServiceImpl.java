package com.cfs.cloudfilestorage.service.person;

import com.cfs.cloudfilestorage.dto.PersonDto;
import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService{
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
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
        newPerson.setAvailableFiles(new ArrayList<>());
        newPerson.setAvailableFolders(new ArrayList<>());
        personRepository.save(newPerson);
    }

    @Override
    public Optional<Person> findByEmail(String email) {
        var person = personRepository.findByEmail(email);
        return Optional.ofNullable(person);
    }
    
    @Override
    public Optional<Person> findById(Long id){
        return personRepository.findById(id);
    }

    @Override
    public void deletePerson(PersonDto person) {
        personRepository.deleteById(person.getId());
    }

    @Override
    public void updatePerson(Person person){
        personRepository.save(person);
    }
}
