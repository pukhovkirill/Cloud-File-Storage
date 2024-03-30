package com.cfs.cloudfilestorage.dto;

import com.cfs.cloudfilestorage.model.File;
import com.cfs.cloudfilestorage.model.Person;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
public class FileDto extends StorageEntity{
    @Builder
    public FileDto(
            Long id,
            @NotBlank String name,
            @NotBlank String path,
            @NotBlank String contentType,
            @NotNull Timestamp lastModified,
            @NotNull int fileSize,
            @NotEmpty byte[] bytes,
            @NotNull String owner) {
        super(id, name, path, contentType, lastModified, fileSize, owner);
        this.bytes = bytes;
    }

    public FileDto(File file){
        super(file);
    }

    @NotEmpty
    private byte[] bytes;
}
