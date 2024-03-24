package com.cfs.cloudfilestorage.service.storage;

import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.service.person.PersonService;
import org.springframework.stereotype.Service;

@Service
public class FolderManageServiceImpl extends BaseManager implements FolderManageService{

    public FolderManageServiceImpl(PersonService personService) {
        super(personService);
    }

    @Override
    public void create(String name) {

    }

    @Override
    public void upload(FolderDto item) {

    }

    @Override
    public void rename(FolderDto item, String newName) {

    }

    @Override
    public FolderDto download(String name, String saveAsName) {
        return null;
    }

    @Override
    public void remove(FolderDto item) {

    }

    @Override
    public void share(FolderDto item) {

    }
}
