package com.cfs.cloudfilestorage.service.storage.Impl.command.file;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.File;
import com.cfs.cloudfilestorage.repository.FileRepository;
import com.cfs.cloudfilestorage.service.person.PersonService;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.Timestamp;

public class DBUploadFileCommand extends StorageCommand<FileDto> {

    private final FileRepository fileRepository;

    private final PersonService personService;

    public DBUploadFileCommand(FileRepository fileRepository, PersonService personService){
        this.fileRepository = fileRepository;
        this.personService = personService;
    }

    @Override
    protected <E extends StorageEntity> void action(E entity, Object ... args) {
        if(entity instanceof FileDto item){
            var optionalPerson = findPerson(personService);

            if(optionalPerson.isEmpty()){
                throw new UsernameNotFoundException("Person not found");
            }

            var person = optionalPerson.get();
            item.setName(String.format("user-%d-files/%s", person.getId(), item.getName()));

            var file = new File(
                    null,
                    item.getName(),
                    item.getPath(),
                    item.getContentType(),
                    new Timestamp(System.currentTimeMillis()),
                    item.getFileSize(),
                    person
            );

            file = fileRepository.save(file);

            person.getAvailableFiles().add(file);

            personService.updatePerson(person);
        }
    }
}
