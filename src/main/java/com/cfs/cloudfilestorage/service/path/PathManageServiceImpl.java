package com.cfs.cloudfilestorage.service.path;

import com.cfs.cloudfilestorage.aps.APS;
import com.cfs.cloudfilestorage.aps.AbstractPathTree;
import com.cfs.cloudfilestorage.aps.PathView;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.StorageItem;
import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

@Service
public class PathManageServiceImpl implements PathManageService, PathConvertService{
    private final AuthorizedPersonService authorizedPersonService;

    private APS tree;

    public PathManageServiceImpl(AuthorizedPersonService authorizedPersonService){
        this.authorizedPersonService = authorizedPersonService;
    }

    @Override
    public String createFolderName(String folderName, String currentPath) {
        String resultFolderPath;

        if(folderName.isEmpty()){
            folderName = "New_folder";
        }

        var workingDir = getFileName(currentPath);

        if(workingDir.equals("vault")){
            folderName = folderName+"/";
            resultFolderPath = getFullName(folderName);
        }else{
            workingDir = new String(Base64.getDecoder().decode(workingDir.getBytes()));
            resultFolderPath = makeFolderPath(workingDir, folderName);
        }

        return resultFolderPath;
    }

    private String makeFolderPath(String workingDirectory, String folderName){
        String[] folders = workingDirectory.split("/");

        StringBuilder path = new StringBuilder();
        for(var folder : folders){
            path.append(folder).append("/");
        }
        path.append(folderName).append("/");

        return path.toString();
    }

    @Override
    public String createFileName(String fileName, String currentPath) {
        String resultFolderPath;
        var workingDir = getFileName(currentPath);

        if(workingDir.equals("vault")){
            resultFolderPath = getFullName(fileName);
        }else{
            workingDir = new String(Base64.getDecoder().decode(workingDir.getBytes()));
            resultFolderPath = makeFilePath(workingDir, fileName);
        }

        return resultFolderPath;
    }

    private String makeFilePath(String workingDirectory, String fileName){
        String[] folders = workingDirectory.split("/");

        StringBuilder path = new StringBuilder();
        for(var folder : folders){
            path.append(folder).append("/");
        }
        path.append(fileName);

        return path.toString();
    }

    @Override
    public String getFileName(String path) {
        try{
            var oPath = Paths.get(path);
            var result = oPath.getFileName().toString();

            if(path.charAt(path.length()-1) == '/')
                result += "/";

            return result;
        }catch (Exception e){
            return "";
        }
    }

    private String getCleanName(String path){
        try{
            var oPath = Paths.get(path);
            return oPath.getFileName().toString();
        }catch (Exception e){
            return "";
        }
    }

    @Override
    public String getFullName(String path) {
        try{
            var root = getPersonRootFolder();
            return String.format("%s/%s", root, path);
        }catch (Exception e){
            return "";
        }
    }

    @Override
    public String getParent(String path) {
        try{
            var oPath = Paths.get(path);
            return oPath.getParent().toString()+"/";
        }catch (Exception e){
            return "";
        }
    }

    @Override
    public PathView getPathView(String path) {
        var fullPath = path;
        path = removeLastSlash(path);
        var parent = getParent(path);
        var workingDirectory = getFileName(path);

        return PathView.builder()
                .path(parent.split("/"))
                .workingDirectory(workingDirectory)
                .fullPath(fullPath)
                .build();
    }

    private String removeLastSlash(String path){
        var parent = getParent(path);
        var name = getCleanName(path);
        return parent+"/"+name;
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
    public void buildStoragePath(List<StorageItem> items) {
        this.tree = new AbstractPathTree(getPersonRootFolder());
        List<StorageEntity> entities = new ArrayList<>();

        for(var item : items){
            var entity = new StorageEntity(item);
            entities.add(entity);
        }

        tree.buildTreeByPath(entities);
    }

    @Override
    public List<StorageEntity> changeDirectory(String path) {
        var iterator = this.tree.getFolder(path);
        return iteratorToList(iterator);
    }

    @Override
    public List<StorageEntity> previousDirectory(String path) {
        return null;
    }

    @Override
    public List<StorageEntity> goToRoot() {
        var iterator = this.tree.getRoot();
        return iteratorToList(iterator);
    }

    private List<StorageEntity> iteratorToList(Iterator<AbstractPathTree.PathNode> iterator){

        if(iterator == null)
            return null;

        List<StorageEntity> result = new ArrayList<>();
        while(iterator.hasNext()){
            var nxt = iterator.next();
            result.add(nxt.getEntity());
        }

        return result;
    }

    @Override
    public boolean pathExists(String path) {
        return tree.pathExists(path);
    }

}
