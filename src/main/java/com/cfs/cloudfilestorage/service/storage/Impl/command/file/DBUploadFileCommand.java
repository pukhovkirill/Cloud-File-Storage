package com.cfs.cloudfilestorage.service.storage.Impl.command.file;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.File;
import com.cfs.cloudfilestorage.repository.FileRepository;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

public class DBUploadFileCommand extends StorageCommand<FileDto> {

    @Autowired
    private FileRepository fileRepository;

    @Override
    protected <E extends StorageEntity> void action(E entity, Object ... args) {
        if(entity instanceof FileDto item){
            var optionalPerson = findPerson();

            if(optionalPerson.isEmpty()){
                throw new UsernameNotFoundException("Person not found");
            }

            var person = optionalPerson.get();
            item.setName(String.format("user-%d-files/%s", person.getId(), item.getName()));

            var file = new File();
            file.setName(item.getName());
            file.setFileName(item.getName());
            file.setFileSize(item.getFileSize());
            file.setContentType(item.getContentType());
            file.setLastModified(item.getLastModified());
            file.setOwner(person);

            file = fileRepository.save(file);

            person.getAvailableFiles().add(file);

            personService.updatePerson(person);
        }
    }
}
