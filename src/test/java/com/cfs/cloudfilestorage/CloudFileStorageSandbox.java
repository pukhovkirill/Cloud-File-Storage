package com.cfs.cloudfilestorage;

import org.springframework.boot.SpringApplication;

public class CloudFileStorageSandbox {
    public static void main(String[] arg){
        SpringApplication
                .from(CloudFileStorageApplication::main)
                .with(TestConfig.class)
                .run(arg);
    }
}
