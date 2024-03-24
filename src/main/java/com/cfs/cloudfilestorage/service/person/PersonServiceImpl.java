package com.cfs.cloudfilestorage.service.person;

import com.cfs.cloudfilestorage.dto.PersonDto;
import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.repository.PersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public Optional<Person> findByEmail(String email) {
        var person = personRepository.findByEmail(email);
        return Optional.ofNullable(person);
    }

    @Override
    public void deletePerson(PersonDto person) {
        personRepository.deleteById(person.getId());
    }
}
