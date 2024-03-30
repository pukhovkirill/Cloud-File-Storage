package com.cfs.cloudfilestorage.service.path;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.File;

import java.util.List;

public interface PathManageService {

    List<FileDto> buildStoragePath(List<File> files);

    List<FileDto> getPath(String path);
}
