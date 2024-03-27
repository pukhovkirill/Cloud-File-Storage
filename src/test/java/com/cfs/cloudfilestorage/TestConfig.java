package com.cfs.cloudfilestorage;

import com.redis.testcontainers.RedisContainer;
import io.minio.MinioClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.MinIOContainer;
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

    @Bean
    public RedisContainer redisContainer(DynamicPropertyRegistry registry){
        var container = new RedisContainer(
                RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG));
        registry.add("spring.data.redis.host", container::getRedisHost);
        registry.add("spring.data.redis.port", container::getRedisPort);

        return container;
    }

    @Bean
    public MinIOContainer minIOContainer(){
        return new MinIOContainer("minio/minio:latest")
                .withUserName("bf24e339e96f0c056c1b685807c0ba6496d5a6f637f2")
                .withPassword("7341c0b12ef3faa77bfd9525918a325a18e1a40b9c6f7ff3a2497c23fc067a1f")
                .withEnv("MINIO_DEFAULT_BUCKETS", "user-files");
    }

    @Bean
    public MinioClient minioClient(MinIOContainer minIOContainer){
        return MinioClient
                .builder()
                .endpoint(minIOContainer.getS3URL())
                .credentials(minIOContainer.getUserName(), minIOContainer.getPassword())
                .build();
    }
}
