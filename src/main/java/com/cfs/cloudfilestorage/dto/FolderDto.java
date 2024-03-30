package com.cfs.cloudfilestorage.dto;

import com.cfs.cloudfilestorage.model.Person;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class FolderDto extends StorageEntity {

    @Builder
    public FolderDto(
            Long id,
            @NotBlank String name,
            @NotBlank String path,
            @NotBlank String contentType,
            @NotNull Timestamp lastModified,
            @NotNull int fileSize,
            @NotNull String owner,
            List<StorageEntity> content) {
        super(id, name, path, contentType, lastModified, fileSize, owner);
        this.content = content;
    }

    @NotNull
    private List<StorageEntity> content;
}
