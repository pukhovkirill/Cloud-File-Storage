package com.cfs.cloudfilestorage.service.storage;

import com.cfs.cloudfilestorage.dto.FileDto;

public interface FileManageService extends StorageManageService<FileDto> {
    void uploadMultiple(FileDto[] fileDtoArray);
}
