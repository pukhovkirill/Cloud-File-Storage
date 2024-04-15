package com.cfs.cloudfilestorage.service.path;

import com.cfs.cloudfilestorage.aps.PathView;

public interface StoragePathManageService {

    String createFolderName(String folderName, String currentPath);

    String createFileName(String fileName, String currentPath);

    String renameFile(String oldName, String newName);

    String renameFolder(String entityOldFolderName, String oldFolderName, String newFolderName);

    PathView getPathView(String path);

    boolean isFolder(String path);
}
