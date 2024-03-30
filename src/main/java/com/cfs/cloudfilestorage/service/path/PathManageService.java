package com.cfs.cloudfilestorage.service.path;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.StorageItem;

import java.util.List;

public interface PathManageService {

    List<StorageEntity> buildStoragePath(List<StorageItem> files);

    List<StorageEntity> getPath(String path);
}
