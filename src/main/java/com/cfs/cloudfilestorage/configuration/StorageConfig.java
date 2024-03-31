package com.cfs.cloudfilestorage.configuration;

import com.cfs.cloudfilestorage.repository.ItemRepository;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.person.PersonService;
import com.cfs.cloudfilestorage.service.storage.Impl.StorageSwitch;
import com.cfs.cloudfilestorage.service.storage.Impl.command.file.*;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
    @Bean("fileStorageSwitch")
    public StorageSwitch fileStorageSwitch(ItemRepository itemRepository, PersonService personService, AuthorizedPersonService authorizedService){
        var storageSwitch = new StorageSwitch();

        StorageCommand uploadCommand = new DBUploadFileCommand(personService, itemRepository, authorizedService);
        uploadCommand.setNext(new BucketUploadFileCommand());
        storageSwitch.register("upload", uploadCommand);

        StorageCommand uploadMultipleCommand = new DBUploadFilesCommand(personService, itemRepository, authorizedService);
        uploadMultipleCommand.setNext(new BucketUploadFilesCommand());
        storageSwitch.register("upload_multiple", uploadMultipleCommand);

        StorageCommand downloadCommand = new BucketDownloadFileCommand();
        storageSwitch.register("download", downloadCommand);

        StorageCommand removeCommand = new DBRemoveFileCommand(itemRepository);
        removeCommand.setNext(new BucketRemoveFileCommand());
        storageSwitch.register("remove", removeCommand);

        StorageCommand renameCommand = new DBRenameFileCommand(itemRepository);
        renameCommand.setNext(new BucketRenameFileCommand());
        storageSwitch.register("rename", renameCommand);

        StorageCommand shareCommand = new DBShareFileCommand(itemRepository, personService);
        storageSwitch.register("share", shareCommand);

        return storageSwitch;
    }

    @Bean("folderStorageSwitch")
    public StorageSwitch folderStorageSwitch(){
        var storageSwitch = new StorageSwitch();
        return storageSwitch;
    }
}
