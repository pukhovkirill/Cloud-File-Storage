package com.cfs.cloudfilestorage.configuration;

import com.cfs.cloudfilestorage.service.storage.Impl.StorageSwitch;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
    @Bean
    public StorageSwitch storageSwitch(){
        return new StorageSwitch();
    }
}
