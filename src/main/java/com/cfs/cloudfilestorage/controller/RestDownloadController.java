package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.model.StorageItem;
import com.cfs.cloudfilestorage.repository.SharedItemRepository;
import com.cfs.cloudfilestorage.service.path.PathConvertService;
import com.cfs.cloudfilestorage.service.path.PathManageService;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class RestDownloadController extends StorageBaseController{

    private final SharedItemRepository sharedItemRepository;

    public RestDownloadController(PathManageService pathManageService,
                                  ItemManageService itemManageService,
                                  PathConvertService pathConvertService,
                                  SharedItemRepository sharedItemRepository,
                                  AuthorizedPersonService authorizedPersonService) {
        super(pathManageService, itemManageService, pathConvertService, authorizedPersonService);
        this.sharedItemRepository = sharedItemRepository;
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public @ResponseBody byte[] downloadFiles(@RequestBody List<String> request) throws IOException {
        if(request.size() == 1 && !pathConvertService.isFolder(request.getFirst())){
            var file = findFile(request.getFirst(), findPerson());
            itemManageService.download(file);
            return fileResponse(file);
        }

        var items = findStorageEntities(request);
        for(var item : items){
            itemManageService.download(item);
        }

        return archiveResponse(items);
    }

    private byte[] archiveResponse(StorageEntity[] entities) throws IOException {
        var archiveName = ("archive-"+(new Timestamp(System.currentTimeMillis()))+".zip").getBytes();
        var nameLength = nameLengthEncode(archiveName.length);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(nameLength);
        baos.write(archiveName);

        final ZipOutputStream zos = new ZipOutputStream(baos);

        for(var file : entities){
            var fileName = subtractionRoot(file.getPath());
            ZipEntry entry = new ZipEntry(fileName);
            entry.setSize(file.getSize());
            zos.putNextEntry(entry);
            zos.write(file.getBytes());
            zos.closeEntry();
        }

        zos.close();
        baos.close();
        return baos.toByteArray();
    }

    private String subtractionRoot(String name){
        var root = "user-"+findPerson().getId()+"-files/";
        return name.replace(root, "");
    }

    private byte[] fileResponse(StorageEntity file) throws IOException {
        var fileName = file.getName().getBytes();
        var nameLength = nameLengthEncode(fileName.length);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(nameLength);
        baos.write(fileName);
        baos.write(file.getBytes());

        baos.close();

        return baos.toByteArray();
    }

    private byte[] nameLengthEncode(int length){
        byte[] lengthBytes = new byte[4];
        lengthBytes[0] = (byte)(char)( length        & 255);
        lengthBytes[1] = (byte)(char)((length >>  8) & 255);
        lengthBytes[2] = (byte)(char)((length >> 16) & 255);
        lengthBytes[3] = (byte)(char)((length >> 24) & 255);

        return lengthBytes;
    }

    private StorageEntity[] findStorageEntities(List<String> entities){
        List<StorageEntity> storageEntities = new ArrayList<>();

        var person = findPerson();

        for(var entity : entities){
            if(pathConvertService.isFolder(entity)){
                storageEntities.addAll(findFolderFiles(entity, person));
            }
            else{
                var file = findFile(entity, person);
                if(file != null)
                    storageEntities.add(file);
            }
        }

        return storageEntities.toArray(new StorageEntity[0]);
    }

    private List<StorageEntity> findFolderFiles(String folderPath, Person person){
        List<StorageEntity> result = new LinkedList<>();

        var list = person.getAvailableItems().stream()
                .filter(x -> x.getPath().startsWith(folderPath))
                .filter(x -> !x.getContentType().equals("folder"))
                .toList();

        for(var item : list){
            result.add(new StorageEntity(item));
        }

        return result;
    }

    private StorageEntity findFile(String filePath, Person person){
        var item = person.getAvailableItems().stream()
                .filter(x -> x.getPath().equals(filePath)).filter(this::checkAvailability).findFirst();

        return item.map(StorageEntity::new).orElse(null);
    }

    private Boolean checkAvailability(StorageItem item){
        var sharedAccess = sharedItemRepository.findByItem(item);

        if(sharedAccess == null){
            return true;
        }

        return sharedAccess.getIsShared();
    }

    @Getter
    @Setter
    @Builder
    public static class StorageResponse{
        private String name;
        private String contentType;
        private byte[] bytes;
    }
}
