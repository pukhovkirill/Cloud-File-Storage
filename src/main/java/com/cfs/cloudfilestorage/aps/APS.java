package com.cfs.cloudfilestorage.aps;

import com.cfs.cloudfilestorage.dto.StorageEntity;

import java.util.Iterator;
import java.util.List;

public interface APS {

    Iterator<AbstractPathTree.PathNode> getRoot();

    Iterator<AbstractPathTree.PathNode> getTreeIterator();

    boolean pathExists(String path);

    Iterator<AbstractPathTree.PathNode> getFolder(String folder);

    void buildTreeByPath(List<StorageEntity> entities);
}
