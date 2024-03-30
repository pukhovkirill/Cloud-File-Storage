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

@Getter
@Setter
@Builder
@AllArgsConstructor
public class StorageEntity {

    protected Long id;

    @NotBlank
    protected String name;

    @NotBlank
    protected String path;

    @NotBlank
    protected String contentType;

    @NotNull
    protected Timestamp lastModified;

    @NotNull
    protected int size;

    @NotNull
    protected String owner;

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
}
