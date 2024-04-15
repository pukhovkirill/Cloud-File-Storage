package com.cfs.cloudfilestorage.service.person;

import com.cfs.cloudfilestorage.repository.PersonRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var person = personRepository.findByEmail(email);

        if (person != null) {
            boolean enabled = true;
            boolean accountNonExpired = true;
            boolean credentialsNonExpired = true;
            boolean accountNonLocked = true;
            return new org.springframework.security.core.userdetails.User(
                    person.getEmail(), person.getPassword(), enabled, accountNonExpired,
                    credentialsNonExpired, accountNonLocked, mapRolesToAuthorities());
        }else{
            throw new UsernameNotFoundException("Invalid username or password.");
        }
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities() {
        Collection<SimpleGrantedAuthority> role = new HashSet<>();
        role.add(new SimpleGrantedAuthority("USER"));
        return role;
    }
}
