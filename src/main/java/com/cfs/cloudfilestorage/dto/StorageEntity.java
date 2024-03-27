package com.cfs.cloudfilestorage.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class StorageEntity {

    protected Long id;

    @NotBlank
    protected String name;

    @NotBlank
    protected String path;

    @NotBlank
    protected String contentType;
}
