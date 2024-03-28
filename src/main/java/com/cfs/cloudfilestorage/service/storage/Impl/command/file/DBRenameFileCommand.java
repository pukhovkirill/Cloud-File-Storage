package com.cfs.cloudfilestorage.service.storage.Impl.command.file;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.repository.FileRepository;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;

import java.io.FileNotFoundException;

public class DBRenameFileCommand extends StorageCommand<FileDto> {

    private final FileRepository fileRepository;

    public DBRenameFileCommand(FileRepository fileRepository){
        this.fileRepository = fileRepository;
    }

    @Override
    protected <E extends StorageEntity> void action(E entity, Object ... args) {
        String newFileName = (String) args[0];
        try {
            if(entity instanceof FileDto item){
                var optionalFile = fileRepository.findById(item.getId());

                if(optionalFile.isEmpty()){
                    throw new FileNotFoundException();
                }

                var file = optionalFile.get();
                file.setName(newFileName);

                fileRepository.save(file);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
