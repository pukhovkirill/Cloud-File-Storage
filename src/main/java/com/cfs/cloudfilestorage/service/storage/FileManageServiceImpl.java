package com.cfs.cloudfilestorage.service.storage;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.service.person.PersonService;
import org.springframework.stereotype.Service;

@Service
public class FileManageServiceImpl extends BaseManager implements FileManageService{

    public FileManageServiceImpl(PersonService personService){
        super(personService);
    }

    @Override
    public void create(String name) {

    }

    @Override
    public void upload(FileDto item) {

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
