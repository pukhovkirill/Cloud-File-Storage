package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.service.path.PathConvertService;
import com.cfs.cloudfilestorage.service.path.PathManageService;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RestController
public class RestDownloadController extends StorageBaseController{

    public RestDownloadController(PathManageService pathManageService, ItemManageService itemManageService, PathConvertService pathConvertService, AuthorizedPersonService authorizedPersonService) {
        super(pathManageService, itemManageService, pathConvertService, authorizedPersonService);
    }

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public @ResponseBody StorageResponse downloadFiles(@RequestBody List<String> request){
        if(request.size() == 1){
            var file = findFile(request.getFirst(), findPerson());
            itemManageService.download(file);
            return fileResponse(file);
        }

        return null;
        //return archiveResponse(findStorageEntities(request));
    }

    private StorageResponse archiveResponse(StorageEntity[] entities){
        return null;
    }

    private StorageResponse fileResponse(StorageEntity file){
        return StorageResponse.builder()
                .name(file.getName())
                .contentType(file.getContentType())
                .bytes(file.getBytes())
                .build();

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
                .toList();

        for(var item : list){
            result.add(new StorageEntity(item));
        }

        return result;
    }

    private StorageEntity findFile(String filePath, Person person){
        var item = person.getAvailableItems().stream()
                .filter(x -> x.getPath().equals(filePath)).findFirst();

        return item.map(StorageEntity::new).orElse(null);
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
