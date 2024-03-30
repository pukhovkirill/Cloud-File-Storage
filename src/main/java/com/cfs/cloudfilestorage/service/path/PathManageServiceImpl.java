package com.cfs.cloudfilestorage.service.path;

import com.cfs.cloudfilestorage.aps.APS;
import com.cfs.cloudfilestorage.aps.AbstractPathTree;
import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.File;
import com.cfs.cloudfilestorage.model.Person;
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
    public List<FileDto> buildStoragePath(List<File> files) {
        this.tree = new AbstractPathTree(getPersonRootFolder());
        List<FileDto> entities = new ArrayList<>();

        var person =  authorizedPersonService.findPerson().get();

        for(var file : files){
            var entity = new FileDto(file);
            entities.add(entity);
        }
        var iterator = tree.buildTreeByPath(entities);
        entities = new ArrayList<>();
        while(iterator.hasNext()){
            entities.add((FileDto) iterator.next().getEntity());
        }

        return entities;
    }

    @Override
    public List<FileDto> getPath(String path) {
        return null;
    }
}
