package com.cfs.cloudfilestorage.service.storage.Impl;

import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.service.storage.FolderManageService;
import org.springframework.stereotype.Service;

@Service("FolderManageService")
public class FolderManageServiceImpl implements FolderManageService {

    @Override
    public FolderDto create(FolderDto item) {
        return null;
    }

    @Override
    public FolderDto upload(FolderDto item) {
        return null;
    }

    @Override
    public FolderDto rename(FolderDto item, String newName) {
        return null;
    }

    @Override
    public FolderDto download(FolderDto item) {
        return null;
    }

    @Override
    public FolderDto remove(FolderDto item) {
        return null;
    }

    @Override
    public FolderDto share(FolderDto item, Long userId) {
        return null;
    }
}
