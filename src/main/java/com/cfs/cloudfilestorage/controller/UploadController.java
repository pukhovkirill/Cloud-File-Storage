package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.path.PathConvertService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;

@Controller
public class UploadController {

    private final PathConvertService pathConvertService;
    private final ItemManageService itemManageService;

    public UploadController(
            PathConvertService pathConvertService,
            ItemManageService itemManageService){
        this.pathConvertService = pathConvertService;
        this.itemManageService = itemManageService;
    }

    @PostMapping("/upload-files")
    public String uploadFiles(@RequestParam("file") MultipartFile[] files) throws IOException {
        itemManageService.uploadMultiple(buildFileDtoArray(files));
        return "redirect:/";
    }

    @PostMapping("/upload-folder")
    public String uploadFolder(@RequestParam("folder") MultipartFile[] folder) throws IOException {
        return "redirect:/";
    }

    private StorageEntity buildFolder(MultipartFile[] folder) throws IOException {
        var folderName = pathConvertService.getFileName(folder[0].getOriginalFilename());
        var folderPath = pathConvertService.getFullName(folder[0].getOriginalFilename());
        return StorageEntity.builder()
                .id(null)
                .name(folderName)
                .path(folderPath)
                .contentType("folder")
                .lastModified(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    private StorageEntity[] buildFileDtoArray(MultipartFile[] files) throws IOException {
        var storageEntities = new StorageEntity[files.length];

        int count = 0;
        for(var file : files){
            storageEntities[count++] = buildFileDto(file);
        }

        return storageEntities;
    }

    private StorageEntity buildFileDto(MultipartFile file) throws IOException {
        var fileName = pathConvertService.getFileName(file.getOriginalFilename());
        var filePath = pathConvertService.getFullName(file.getOriginalFilename());
        return StorageEntity.builder()
                .id(null)
                .name(fileName)
                .path(filePath)
                .contentType(file.getContentType())
                .lastModified(new Timestamp(System.currentTimeMillis()))
                .size((int)file.getSize())
                .bytes(file.getBytes())
                .build();
    }
}
