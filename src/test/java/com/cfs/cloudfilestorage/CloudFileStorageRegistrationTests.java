package com.cfs.cloudfilestorage;

import com.cfs.cloudfilestorage.dto.PersonDto;
import com.cfs.cloudfilestorage.service.person.PersonService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestConfiguration(proxyBeanMethods = false)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CloudFileStorageRegistrationTests {

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

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
