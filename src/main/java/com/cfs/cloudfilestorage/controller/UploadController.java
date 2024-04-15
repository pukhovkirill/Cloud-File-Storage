package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.service.path.*;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Controller
public class UploadController extends StorageBaseController{


    public UploadController(ItemManageService itemManageService,
                            AuthorizedPersonService authorizedPersonService,
                            StoragePathManageService storagePathManageService,
                            PathStringRefactorService pathStringRefactorService,
                            StorageContentManageService storageContentManageService) {
        super(itemManageService, authorizedPersonService,
                storagePathManageService, pathStringRefactorService, storageContentManageService);
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
        var folderName = pathStringRefactorService.getParent(name);
        var folderPath = storagePathManageService.createFileName(folderName, currentPath);

        folderName = pathStringRefactorService.getCleanName(folderName);
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
        findSubfolders(folder, person, currentPath, folderName+"/");
        return buildFolderFileDtoArray(folder, folderPath);
    }

    private void findSubfolders(MultipartFile[] folder, Person person, String currentPath, String uploadedFolder){
        Set<String> subfolders = new HashSet<>();
        for (MultipartFile file : folder) {
            var name = file.getOriginalFilename();
            var parent = pathStringRefactorService.getParent(name);

            if(parent.equals(uploadedFolder) || subfolders.contains(parent))
                continue;

            var folderPath = storagePathManageService.createFileName(parent, currentPath);
            var folderName = pathStringRefactorService.getCleanName(parent);
            var eFolder = StorageEntity.builder()
                    .id(null)
                    .name(folderName)
                    .path(folderPath)
                    .contentType("folder")
                    .lastModified(new Timestamp(System.currentTimeMillis()))
                    .bytes(new byte[0])
                    .owner(person.getEmail())
                    .build();

            subfolders.add(parent);
            itemManageService.upload(eFolder);
        }
    }

    private StorageEntity[] buildFolderFileDtoArray(MultipartFile[] folder, String currentPath) throws IOException {
        var storageEntities = new StorageEntity[folder.length];

        int count = 0;
        for(var file : folder){
            var root = pathStringRefactorService.getOriginalName(currentPath);
            var fileName = pathStringRefactorService.subtraction(file.getOriginalFilename(), root);
            var filePath = currentPath+fileName;
            fileName = pathStringRefactorService.getOriginalName(fileName);
            storageEntities[count++] = buildFileDto(file, fileName, filePath);
        }

        return storageEntities;
    }

    private StorageEntity[] buildFileDtoArray(MultipartFile[] files, String currentPath) throws IOException {
        var storageEntities = new StorageEntity[files.length];

        int count = 0;
        for(var file : files){
            var fileName = pathStringRefactorService.getOriginalName(file.getOriginalFilename());
            var filePath = storagePathManageService.createFileName(fileName, currentPath);
            storageEntities[count++] = buildFileDto(file, fileName, filePath);
        }

        return storageEntities;
    }

    private StorageEntity buildFileDto(MultipartFile file, String fileName, String filePath) throws IOException {
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
