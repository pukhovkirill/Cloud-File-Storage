package com.cfs.cloudfilestorage.service.path;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.StorageItem;

import java.util.List;

public interface PathManageService {

    List<StorageEntity> buildStoragePath(List<StorageItem> files);

    List<StorageEntity> changeDirectory(String path);

    List<StorageEntity> previousDirectory(String path);

    List<StorageEntity> goToRoot();
}
