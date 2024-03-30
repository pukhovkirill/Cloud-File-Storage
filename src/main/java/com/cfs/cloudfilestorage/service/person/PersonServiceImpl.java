package com.cfs.cloudfilestorage.service.person;

import com.cfs.cloudfilestorage.dto.PersonDto;
import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService, AuthorizedPersonService{
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, PasswordEncoder passwordEncoder){
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void savePerson(PersonDto person) {
        var newPerson = Person.builder()
                .id(null)
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .email(person.getEmail())
                .phone(person.getPhone())
                .password(passwordEncoder.encode(person.getPassword()))
                .availableFiles(new ArrayList<>())
                .build();

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

    @Override
    public Optional<Person> findPerson() {
        var details = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(details instanceof UserDetails user)
            return Optional.ofNullable(personRepository.findByEmail(user.getUsername()));

        return Optional.empty();
    }

    @Override
    public boolean isAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }
}
