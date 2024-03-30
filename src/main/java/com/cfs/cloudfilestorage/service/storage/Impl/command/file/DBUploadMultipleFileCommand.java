package com.cfs.cloudfilestorage.service.storage.Impl.command.file;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.repository.FileRepository;
import com.cfs.cloudfilestorage.service.person.PersonService;

public class DBUploadMultipleFileCommand extends DBUploadFileCommand{
    public DBUploadMultipleFileCommand(FileRepository fileRepository, PersonService personService) {
        super(fileRepository, personService);
    }

    @Override
    protected <E extends StorageEntity> void action(E entity, Object... args) {
        if(args[0] instanceof FileDto[] fileDtoArray){
            for(var file : fileDtoArray){
                super.action(file);
            }
        }
    }
}
