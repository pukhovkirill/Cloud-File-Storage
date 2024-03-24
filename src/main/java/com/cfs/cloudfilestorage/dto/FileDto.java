package com.cfs.cloudfilestorage.dto;

import jakarta.validation.constraints.NotBlank;
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
public class FileDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String fileName;

    @NotBlank
    private String contentType;

    @NotNull
    private Timestamp lastModified;

    @NotNull
    private double fileSize;
}
