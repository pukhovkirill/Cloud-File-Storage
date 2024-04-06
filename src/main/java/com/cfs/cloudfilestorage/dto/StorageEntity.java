package com.cfs.cloudfilestorage.dto;

import com.cfs.cloudfilestorage.model.StorageItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Base64;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StorageEntity {

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
        return Base64.getEncoder().encodeToString(this.path.getBytes());
    }
}
