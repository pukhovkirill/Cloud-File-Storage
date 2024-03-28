package com.cfs.cloudfilestorage.service.storage.Impl.command.file;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.repository.FileRepository;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.FileNotFoundException;

public class DBShareFileCommand extends StorageCommand<FileDto> {

    @Autowired
    private FileRepository fileRepository;

    @Override
    protected <E extends StorageEntity> void action(E entity, Object ... args) {
        var optionalPerson = personService.findById((Long) args[0]);

        try {
            if(optionalPerson.isEmpty()){
                throw new UsernameNotFoundException("Person not found");
            }

            var person = optionalPerson.get();

            if(entity instanceof FileDto item){
                var optionalFile = fileRepository.findById(item.getId());
                if(optionalFile.isEmpty()){
                    throw new FileNotFoundException();
                }

                var file = optionalFile.get();

                person.getAvailableFiles().add(file);

                personService.updatePerson(person);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}