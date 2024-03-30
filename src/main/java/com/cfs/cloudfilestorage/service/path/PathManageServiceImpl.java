package com.cfs.cloudfilestorage.service.path;

import com.cfs.cloudfilestorage.aps.APS;
import com.cfs.cloudfilestorage.aps.AbstractPathTree;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.StorageItem;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class PathManageServiceImpl implements PathManageService, PathConvertService{
    private final AuthorizedPersonService authorizedPersonService;

    private APS tree;

    public PathManageServiceImpl(AuthorizedPersonService authorizedPersonService){
        this.authorizedPersonService = authorizedPersonService;
    }

    @Override
    public String getFileName(String path) {
        var oPath = Paths.get(path);
        return oPath.getFileName().toString();
    }

    @Override
    public String getFullName(String path) {
        var root = getPersonRootFolder();
        return String.format("%s/%s", root, path);
    }

    private String getPersonRootFolder(){
        var optPerson = authorizedPersonService.findPerson();

        if(optPerson.isEmpty()){
            throw new UsernameNotFoundException("person");
        }

        var person = optPerson.get();

        return String.format("user-%d-files", person.getId());
    }

    @Override
    public List<StorageEntity> buildStoragePath(List<StorageItem> items) {
        this.tree = new AbstractPathTree(getPersonRootFolder());
        List<StorageEntity> entities = new ArrayList<>();

        for(var item : items){
            var entity = new StorageEntity(item);
            entities.add(entity);
        }

        var iterator = tree.buildTreeByPath(entities);
        entities = new ArrayList<>();
        while(iterator.hasNext()){
            entities.add(iterator.next().getEntity());
        }

        return entities;
    }

    @Override
    public List<StorageEntity> getPath(String path) {
        return null;
    }


}
