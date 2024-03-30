package com.cfs.cloudfilestorage.aps;

import com.cfs.cloudfilestorage.dto.StorageEntity;

import java.util.Iterator;
import java.util.List;

public interface APS {

    TreeState saveState();

    void restoreState(TreeState state);

    void addFile(StorageEntity fileDto);

    void addFolder(StorageEntity folderDto);

    Iterator<AbstractPathTree.PathNode> getFolder(StorageEntity folderDto);

    Iterator<AbstractPathTree.PathNode> buildTreeByPath(List<StorageEntity> entities);
}
