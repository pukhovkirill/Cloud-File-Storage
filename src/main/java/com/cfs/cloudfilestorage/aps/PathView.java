package com.cfs.cloudfilestorage.aps;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PathView {
    private String[] path;
    private String workingDirectory;
}
