package com.cfs.cloudfilestorage.service.path;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.StorageItem;

import java.util.List;

public interface PathManageService {

    void buildStoragePath(List<StorageItem> files);

    List<StorageEntity> changeDirectory(String path);

    List<StorageEntity> getAllFiles();

    List<StorageEntity> getAllDirectory();

    List<StorageEntity> goToRoot();

    boolean pathExists(String path);
}
