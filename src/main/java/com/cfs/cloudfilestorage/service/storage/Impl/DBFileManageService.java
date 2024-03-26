package com.cfs.cloudfilestorage.service.storage.Impl;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.model.File;
import com.cfs.cloudfilestorage.repository.FileRepository;
import com.cfs.cloudfilestorage.service.person.PersonService;
import com.cfs.cloudfilestorage.service.storage.FileManageService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class DBFileManageService extends BaseDBManager implements FileManageService {

    private final FileRepository fileRepository;

    public DBFileManageService(PersonService personService, FileRepository fileRepository){
        super(personService);
        this.fileRepository = fileRepository;
    }


    @Override
    public void create(String name) {

    }

    @Override
    public void upload(FileDto item) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        var optionalPerson = findPerson();

        if(optionalPerson.isEmpty()){
            throw new UsernameNotFoundException("Person not found");
        }

        var person = optionalPerson.get();
        item.setName(String.format("user-%d-files/%s", person.getId(), item.getName()));

        var file = new File();
        file.setName(item.getName());
        file.setFileName(item.getFileName());
        file.setFileSize(item.getFileSize());
        file.setContentType(item.getContentType());
        file.setLastModified(item.getLastModified());
        file.setOwner(person);

        file = fileRepository.save(file);

        person.getAvailableFiles().add(file);

        personService.updatePerson(person);
    }

    @Override
    public void rename(FileDto item, String newName) {

    }

    @Override
    public FileDto download(String name, String saveAsName) {
        return null;
    }

    @Override
    public void remove(FileDto item) {

    }

    @Override
    public void share(FileDto item) {

    }
}
