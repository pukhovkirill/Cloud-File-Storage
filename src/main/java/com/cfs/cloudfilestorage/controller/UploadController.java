package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.service.path.PathConvertService;
import com.cfs.cloudfilestorage.service.storage.FileManageService;
import com.cfs.cloudfilestorage.service.storage.FolderManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;

@Controller
public class UploadController {

    private final PathConvertService pathConvertService;
    private final FileManageService fileStorageService;
    private final FolderManageService folderStorageService;

    public UploadController(
            PathConvertService pathConvertService,
            FileManageService fileStorageService,
            FolderManageService folderStorageService){
        this.pathConvertService = pathConvertService;
        this.fileStorageService = fileStorageService;
        this.folderStorageService = folderStorageService;
    }

    @PostMapping("/upload-files")
    public String uploadFiles(@RequestParam("file") MultipartFile[] files) throws IOException {
        fileStorageService.uploadMultiple(buildFileDtoArray(files));
        return "redirect:/";
    }

    @PostMapping("/upload-folder")
    public String uploadFolder(@RequestParam("folder") MultipartFile[] folder) throws IOException {
        folderStorageService.upload(buildFolder(folder));
        return "redirect:/";
    }

    private FolderDto buildFolder(MultipartFile[] folder) throws IOException {
        var folderName = pathConvertService.getFileName(folder[0].getOriginalFilename());
        var folderPath = pathConvertService.getFullName(folder[0].getOriginalFilename());
        return FolderDto.builder()
                .id(null)
                .name(folderName)
                .path(folderPath)
                .contentType("folder")
                .lastModified(new Timestamp(System.currentTimeMillis()))
                .content(Arrays.asList(buildFileDtoArray(folder)))
                .build();
    }

    private FileDto[] buildFileDtoArray(MultipartFile[] files) throws IOException {
        var fileDtoArray = new FileDto[files.length];

        int count = 0;
        for(var file : files){
            fileDtoArray[count++] = buildFileDto(file);
        }

        return fileDtoArray;
    }

    private FileDto buildFileDto(MultipartFile file) throws IOException {
        var fileName = pathConvertService.getFileName(file.getOriginalFilename());
        var filePath = pathConvertService.getFullName(file.getOriginalFilename());
        return FileDto.builder()
                .id(null)
                .name(fileName)
                .path(filePath)
                .contentType(file.getContentType())
                .lastModified(new Timestamp(System.currentTimeMillis()))
                .fileSize((int)file.getSize())
                .bytes(file.getBytes())
                .build();
    }
}
