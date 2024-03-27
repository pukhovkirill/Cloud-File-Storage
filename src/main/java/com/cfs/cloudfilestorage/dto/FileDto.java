package com.cfs.cloudfilestorage.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileDto extends StorageEntity{

    @NotNull
    private Timestamp lastModified;

    @NotNull
    private double fileSize;
}
