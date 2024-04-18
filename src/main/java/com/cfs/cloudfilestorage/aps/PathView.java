package com.cfs.cloudfilestorage.aps;

import lombok.Builder;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Getter
@Builder
public class PathView {
    private String[] path;
    private String workingDirectory;
    private String fullPath;

    public String pathToBase64(){
        var URLEncodeString = java.net.URLEncoder.encode(this.fullPath, StandardCharsets.UTF_8);
        return new String(Base64.getEncoder().encode(URLEncodeString.getBytes()), StandardCharsets.UTF_8);
    }
}
