package com.cfs.cloudfilestorage.configuration;

import com.cfs.cloudfilestorage.repository.ItemRepository;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.person.PersonService;
import com.cfs.cloudfilestorage.service.storage.Impl.StorageSwitch;
import com.cfs.cloudfilestorage.service.storage.Impl.command.*;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
    @Bean("fileStorageSwitch")
    public StorageSwitch fileStorageSwitch(PersonService personService,
                                           ItemRepository itemRepository,
                                           AuthorizedPersonService authorizedService){
        var storageSwitch = new StorageSwitch();

        StorageCommand uploadCommand = new DBUploadCommand(personService, itemRepository, authorizedService);
        uploadCommand.setNext(new BucketUploadCommand());
        storageSwitch.register("upload", uploadCommand);

        StorageCommand uploadMultipleCommand = new DBUploadMultipleCommand(personService, itemRepository, authorizedService);
        uploadMultipleCommand.setNext(new BucketUploadMultipleCommand());
        storageSwitch.register("upload_multiple", uploadMultipleCommand);

        StorageCommand downloadCommand = new BucketDownloadFileCommand();
        downloadCommand.setNext(new BucketDownloadFolderCommand());
        storageSwitch.register("download", downloadCommand);

        StorageCommand removeCommand = new DBRemoveCommand(itemRepository);
        removeCommand.setNext(new BucketRemoveFileCommand());
        removeCommand.setNext(new BucketDownloadFolderCommand());
        storageSwitch.register("remove", removeCommand);

        StorageCommand renameCommand = new DBRenameCommand(itemRepository);
        renameCommand.setNext(new BucketRenameFileCommand());
        removeCommand.setNext(new BucketDownloadFolderCommand());
        storageSwitch.register("rename", renameCommand);

        StorageCommand shareCommand = new DBShareCommand(itemRepository, personService);
        storageSwitch.register("share", shareCommand);

        return storageSwitch;
    }
}
