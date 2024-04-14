package com.cfs.cloudfilestorage.configuration;

import com.cfs.cloudfilestorage.repository.ItemRepository;
import com.cfs.cloudfilestorage.repository.PersonRepository;
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
                                           AuthorizedPersonService authorizedService, PersonRepository personRepository){
        var storageSwitch = new StorageSwitch();

        StorageCommand uploadCommand = new DBUploadCommand(personService, itemRepository, authorizedService);
        uploadCommand.setNext(new BucketUploadCommand());
        storageSwitch.register("upload", uploadCommand);

        StorageCommand uploadMultipleCommand = new DBUploadMultipleCommand(personService, itemRepository, authorizedService);
        uploadMultipleCommand.setNext(new BucketUploadMultipleCommand());
        storageSwitch.register("upload_multiple", uploadMultipleCommand);

        StorageCommand downloadCommand = new BucketDownloadCommand();
        storageSwitch.register("download", downloadCommand);

        StorageCommand moveToTrashCommand = new DBRemoveCommand(itemRepository, personRepository, authorizedService);
        moveToTrashCommand
                .setNext(new BucketMoveToTrashBinCommand())
                .setNext(new BucketRemoveCommand());
        storageSwitch.register("move_to_trash", moveToTrashCommand);

        StorageCommand undoFromTrashCommand = new BucketUndoFromTrashBinCommand();
        undoFromTrashCommand
                .setNext(new BucketRemoveFromTrashBinCommand())
                .setNext(new DBUploadCommand(personService, itemRepository, authorizedService));
        storageSwitch.register("undo_from_trash", undoFromTrashCommand);

        StorageCommand removeFromTrashCommand = new BucketRemoveFromTrashBinCommand();
        storageSwitch.register("remove_from_trash", removeFromTrashCommand);

        StorageCommand renameCommand = new DBRenameCommand(itemRepository, authorizedService);
        renameCommand
                .setNext(new BucketRenameFileCommand())
                .setNext(new BucketRenameFolderCommand())
                .setNext(new BucketRemoveCommand());
        storageSwitch.register("rename", renameCommand);

        StorageCommand shareCommand = new DBShareCommand(itemRepository, personService);
        storageSwitch.register("share", shareCommand);

        StorageCommand removeShareCommand = new DBRemoveCommand(itemRepository, personRepository, authorizedService);
        removeShareCommand.setNext(new BucketMoveToTrashBinCommand());
        storageSwitch.register("remove_share", removeShareCommand);

        return storageSwitch;
    }
}
