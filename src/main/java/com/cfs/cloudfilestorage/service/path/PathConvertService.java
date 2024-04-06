package com.cfs.cloudfilestorage.service.path;

import com.cfs.cloudfilestorage.aps.PathView;

public interface PathConvertService {

    String createFolderName(String folderName, String currentPath);

    String createFileName(String fileName, String currentPath);

    String renameFile(String oldName, String newName);

    String renameFolder(String entityOldFolderName, String oldFolderName, String newFolderName);

    String getFileName(String path);

    String getCleanName(String path);

    String getFullName(String path);

    String getParent(String path);

    PathView getPathView(String path);

    boolean isFolder(String path);
}
