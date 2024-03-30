package com.cfs.cloudfilestorage.aps;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TreeState {
    private TokenType type;
    private String name;
    private String path;
    private StorageEntity entity;
    private List<AbstractPathTree.PathNode> children;
}
