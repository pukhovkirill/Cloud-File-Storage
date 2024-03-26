package com.cfs.cloudfilestorage;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestConfig {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer(DynamicPropertyRegistry registry){
        var container = new PostgreSQLContainer<>("postgres:latest");
        registry.add("postgresql.driver", container::getDriverClassName);
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.name", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);

        return container;
    }
}
