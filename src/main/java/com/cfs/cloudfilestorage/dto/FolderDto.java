package com.cfs.cloudfilestorage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FolderDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String folderPath;

    @NotNull
    private List<FileDto> content;
}
