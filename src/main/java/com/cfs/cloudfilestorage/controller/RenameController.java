package com.cfs.cloudfilestorage.controller;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.Person;
import com.cfs.cloudfilestorage.service.path.*;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import com.cfs.cloudfilestorage.service.storage.ItemManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RenameController extends StorageBaseController{


    public RenameController(ItemManageService itemManageService,
                            AuthorizedPersonService authorizedPersonService,
                            StoragePathManageService storagePathManageService,
                            PathStringRefactorService pathStringRefactorService,
                            StorageContentManageService storageContentManageService) {

        super(itemManageService, authorizedPersonService,
                storagePathManageService, pathStringRefactorService, storageContentManageService);
    }

    @PostMapping("/rename")
    public String renameFile(@RequestParam("entity_path") String path,
                             @RequestParam("entity_name") String name,
                             @RequestParam("working_directory") String currentPath) throws FileAlreadyExistsException {

        var entities = findStorageEntity(path);

        if(entities.length == 1){
            path = storagePathManageService.renameFile(path, name);

            if(isItemExists(path)){
                throw new FileAlreadyExistsException("File exists");
            }

            itemManageService.rename(entities[0], name, path);
        }else{
            for(var entity : entities){
                path = storagePathManageService.renameFolder(entity.getPath(), path, name);

                if(isItemExists(path)){
                    throw new FileAlreadyExistsException("Folder exists");
                }

                itemManageService.rename(entity, name, path);
            }
        }

        return String.format("redirect:%s", currentPath);
    }

    private boolean isItemExists(String path){
        return findFile(path, findPerson()).length > 0;
    }

    private StorageEntity[] findStorageEntity(String path){
        var person = findPerson();

        if(storagePathManageService.isFolder(path)){
            return findFolder(path, person);
        }else{
            return findFile(path, findPerson());
        }
    }

    private StorageEntity[] findFolder(String path, Person person){
        List<StorageEntity> storageEntities = new ArrayList<>();

        var items = person.getAvailableItems().stream().filter(x -> x.getPath().startsWith(path)).toList();

        for(var item : items){
            storageEntities.add(new StorageEntity(item));
        }

        return storageEntities.toArray(new StorageEntity[0]);
    }

    private StorageEntity[] findFile(String filePath, Person person){
        var item = person.getAvailableItems().stream()
                .filter(x -> x.getPath().equals(filePath)).findFirst();

        return new StorageEntity[]{item.map(StorageEntity::new).orElse(null)};
    }
}
