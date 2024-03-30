package com.cfs.cloudfilestorage.aps;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;

import java.util.Iterator;
import java.util.List;

public interface APS {

    TreeState saveState();

    void restoreState(TreeState state);

    void addFile(FileDto fileDto);

    void addFolder(FolderDto folderDto);

    Iterator<AbstractPathTree.PathNode> getFolder(FolderDto folderDto);

    Iterator<AbstractPathTree.PathNode> buildTreeByPath(List<FileDto> entities);
}
