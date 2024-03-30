package com.cfs.cloudfilestorage.configuration;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.repository.FileRepository;
import com.cfs.cloudfilestorage.service.person.PersonService;
import com.cfs.cloudfilestorage.service.storage.Impl.StorageSwitch;
import com.cfs.cloudfilestorage.service.storage.Impl.command.file.*;
import com.cfs.cloudfilestorage.service.storage.Impl.command.folder.*;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
    @Bean("fileStorageSwitch")
    public StorageSwitch fileStorageSwitch(FileRepository fileRepository, PersonService personService){
        var storageSwitch = new StorageSwitch();

        StorageCommand<FileDto> uploadCommand = new DBUploadFileCommand(fileRepository, personService);
        uploadCommand.setNext(new BucketUploadFileCommand());
        storageSwitch.register("upload", uploadCommand);

        StorageCommand<FileDto> uploadMultipleCommand = new DBUploadMultipleFileCommand(fileRepository, personService);
        uploadMultipleCommand.setNext(new BucketUploadMultipleFileCommand());
        storageSwitch.register("upload_multiple", uploadMultipleCommand);

        StorageCommand<FileDto> downloadCommand = new BucketDownloadFileCommand();
        storageSwitch.register("download", downloadCommand);

        StorageCommand<FileDto> removeCommand = new DBRemoveFileCommand(fileRepository);
        removeCommand.setNext(new BucketRemoveFileCommand());
        storageSwitch.register("remove", removeCommand);

        StorageCommand<FileDto> renameCommand = new DBRenameFileCommand(fileRepository);
        renameCommand.setNext(new BucketRenameFileCommand());
        storageSwitch.register("rename", renameCommand);

        StorageCommand<FileDto> shareCommand = new DBShareFileCommand(fileRepository, personService);
        storageSwitch.register("share", shareCommand);

        return storageSwitch;
    }

    @Bean("folderStorageSwitch")
    public StorageSwitch folderStorageSwitch(FileRepository fileRepository, PersonService personService){
        var storageSwitch = new StorageSwitch();
        return storageSwitch;
    }
}
