package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.path.PathConvertService;
import com.cfs.cloudfilestorage.service.path.PathManageService;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;

@Controller
public class UploadController extends StorageBaseController{

    public UploadController(PathManageService pathManageService,
                            ItemManageService itemManageService,
                            PathConvertService pathConvertService,
                            AuthorizedPersonService authorizedPersonService) {
        super(pathManageService, itemManageService, pathConvertService, authorizedPersonService);
    }

    @PostMapping("/upload-files")
    public String uploadFiles(@RequestParam("file") MultipartFile[] files, @RequestParam("current_path") String currentPath) throws IOException {
        itemManageService.uploadMultiple(buildFileDtoArray(files, currentPath));
        return String.format("redirect:%s", currentPath);
    }

    @PostMapping("/upload-folder")
    public String uploadFolder(@RequestParam("folder") MultipartFile[] folder, @RequestParam("current_path") String currentPath) throws IOException {
        itemManageService.uploadMultiple(buildFolderDtoArray(folder, currentPath));
        return String.format("redirect:%s", currentPath);
    }

    private StorageEntity[] buildFolderDtoArray(MultipartFile[] folder, String currentPath) throws IOException {
        var person = findPerson();
        var name = folder[0].getOriginalFilename();
        var folderName = pathConvertService.getParent(name);
        var folderPath = pathConvertService.createFileName(folderName, currentPath);

        folderName = pathConvertService.getCleanName(folderName);
        var eFolder = StorageEntity.builder()
                .id(null)
                .name(folderName)
                .path(folderPath)
                .contentType("folder")
                .lastModified(new Timestamp(System.currentTimeMillis()))
                .bytes(new byte[0])
                .owner(person.getEmail())
                .build();

        itemManageService.upload(eFolder);
        return buildFolderFileDtoArray(folder, folderPath);
    }

    private StorageEntity[] buildFolderFileDtoArray(MultipartFile[] folder, String currentPath) throws IOException {
        var cPath = Base64.getEncoder().encodeToString(currentPath.getBytes());
        return buildFileDtoArray(folder, cPath);
    }

    private StorageEntity[] buildFileDtoArray(MultipartFile[] files, String currentPath) throws IOException {
        var storageEntities = new StorageEntity[files.length];

        int count = 0;
        for(var file : files){
            storageEntities[count++] = buildFileDto(file, currentPath);
        }

        return storageEntities;
    }

    private StorageEntity buildFileDto(MultipartFile file, String currentPath) throws IOException {
        var fileName = pathConvertService.getFileName(file.getOriginalFilename());
        var filePath = pathConvertService.createFileName(fileName, currentPath);
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
