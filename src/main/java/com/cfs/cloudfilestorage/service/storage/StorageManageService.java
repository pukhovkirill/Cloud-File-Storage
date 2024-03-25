package com.cfs.cloudfilestorage.service.storage;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface StorageManageService<T> {

    void create(String name);

    void upload(T item) throws IOException, NoSuchAlgorithmException, InvalidKeyException;

    void rename(T item, String newName);

    T download(String name, String saveAsName);

    void remove(T item);

    void share(T item);
}
