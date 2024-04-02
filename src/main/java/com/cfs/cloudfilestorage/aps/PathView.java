package com.cfs.cloudfilestorage.aps;

import lombok.Builder;
import lombok.Getter;

import java.util.Base64;

@Getter
@Builder
public class PathView {
    private String[] path;
    private String workingDirectory;
    private String fullPath;

    public String pathToBase64(){
        return Base64.getEncoder().encodeToString(this.fullPath.getBytes());
    }
}
