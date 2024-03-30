package com.cfs.cloudfilestorage.dto;

import com.cfs.cloudfilestorage.model.File;
import com.cfs.cloudfilestorage.model.Person;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public abstract class StorageEntity {

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
    protected int fileSize;

    @NotNull
    protected String owner;

    public StorageEntity(File file){
        this.id = file.getId();
        this.name = file.getName();
        this.path = file.getFileName();
        this.contentType = file.getContentType();
        this.lastModified = file.getLastModified();
        this.fileSize = file.getFileSize();
        this.owner = file.getOwnerEmail();
    }
}
