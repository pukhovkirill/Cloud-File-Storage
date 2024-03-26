package com.cfs.cloudfilestorage;

import com.cfs.cloudfilestorage.dto.PersonDto;
import com.cfs.cloudfilestorage.service.person.PersonService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = TestConfig.class)
public class CloudFileStorageRegistrationTests {

    @Autowired
    PersonService personService;

    static PersonDto testUser;

    @BeforeEach
    void setup(){
        testUser = new PersonDto();
        testUser.setId(1L);
        testUser.setFirstName("test_first_name");
        testUser.setLastName("test_last_name");
        testUser.setEmail("login@test.com");
        testUser.setPhone("88000000000");
        testUser.setPassword("test");
    }

    @Test
    @Order(1)
    void registrationTest(){
        personService.savePerson(testUser);
        Assertions.assertTrue(
                personService.findByEmail(testUser.getEmail()).isPresent());
    }

    @Test
    @Order(2)
    void deleteUserTest(){
        personService.deletePerson(testUser);
        Assertions.assertTrue(
                personService.findByEmail(testUser.getEmail()).isEmpty());
    }
}
