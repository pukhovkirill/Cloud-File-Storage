package com.cfs.cloudfilestorage.dto;

import com.cfs.cloudfilestorage.model.StorageItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StorageEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String path;

    @NotBlank
    private String contentType;

    @NotNull
    private Timestamp lastModified;

    @NotNull
    private int size;

    @NotNull
    private String owner;

    private Boolean isAvailable = true;

    @NotEmpty
    private byte[] bytes;

    public StorageEntity(StorageItem item){
        this.id = item.getId();
        this.name = item.getName();
        this.path = item.getPath();
        this.size = item.getItemSize();
        this.owner = item.getOwnerEmail();
        this.contentType = item.getContentType();
        this.lastModified = item.getLastModified();
    }

    public String pathToBase64(){
        var URLEncodeString = java.net.URLEncoder.encode(this.path, StandardCharsets.UTF_8);
        return new String(Base64.getEncoder().encode(URLEncodeString.getBytes()), StandardCharsets.UTF_8);
    }
}
