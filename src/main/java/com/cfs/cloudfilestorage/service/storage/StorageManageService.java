package com.cfs.cloudfilestorage.service.storage;

public interface StorageManageService<T> {

    void create(String name);

    void upload(T item);

    void rename(T item, String newName);

    T download(String name, String saveAsName);

    void remove(T item);

    void share(T item);
}
