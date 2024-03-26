package com.cfs.cloudfilestorage.service.storage.Impl;

import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.service.person.PersonService;
import com.cfs.cloudfilestorage.service.storage.FolderManageService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class DBFolderManageService extends BaseDBManager implements FolderManageService {
    public DBFolderManageService(PersonService personService) {
        super(personService);
    }

    @Override
    public void create(String name) {

    }

    @Override
    public void upload(FolderDto item) throws IOException, NoSuchAlgorithmException, InvalidKeyException {

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
