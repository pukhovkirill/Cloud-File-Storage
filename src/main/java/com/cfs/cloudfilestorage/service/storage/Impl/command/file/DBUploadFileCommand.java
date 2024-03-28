package com.cfs.cloudfilestorage.service.storage.Impl.command.file;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.File;
import com.cfs.cloudfilestorage.repository.FileRepository;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;

public class DBUploadFileCommand extends StorageCommand<FileDto> {

    @Autowired
    private FileRepository fileRepository;

    @Override
    protected <E extends StorageEntity> void action(E entity, Object ... args) {
        try{
            if(entity instanceof FileDto item){
                var optionalPerson = findPerson();

                if(optionalPerson.isEmpty()){
                    throw new UsernameNotFoundException("Person not found");
                }

                var person = optionalPerson.get();
                item.setName(String.format("user-%d-files/%s", person.getId(), item.getName()));

                var filePath = Path.of(item.getPath());

                var file = new File(
                        null,
                        item.getName(),
                        item.getPath(),
                        Files.probeContentType(filePath),
                        new Timestamp(System.currentTimeMillis()),
                        Files.size(filePath),
                        person
                );

                file = fileRepository.save(file);

                person.getAvailableFiles().add(file);

                personService.updatePerson(person);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
