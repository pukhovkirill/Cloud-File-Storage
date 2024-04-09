package com.cfs.cloudfilestorage.service.path;

import com.cfs.cloudfilestorage.aps.APS;
import com.cfs.cloudfilestorage.aps.AbstractPathTree;
import com.cfs.cloudfilestorage.aps.PathView;
import com.cfs.cloudfilestorage.aps.TokenType;
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
import java.util.regex.Pattern;

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
            workingDir = new String(Base64.getMimeDecoder().decode(workingDir.getBytes()));
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
            if(isBase64(workingDir))
                workingDir = new String(Base64.getMimeDecoder().decode(workingDir.getBytes()));
            resultFolderPath = makeFilePath(workingDir, fileName);
        }

        return resultFolderPath;
    }

    public boolean isBase64(String str) {
        String pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$";
        return Pattern.matches(pattern, str);
    }

    @Override
    public String renameFile(String oldName, String newName) {
        var parent = getParent(oldName);
        return parent+newName;
    }

    @Override
    public String renameFolder(String entityOldFolderName, String oldFolderName, String newFolderName) {
        if(isFolder(entityOldFolderName)){
            var parent = getParent(oldFolderName);
            newFolderName = parent+newFolderName;

            if(!newFolderName.endsWith("/"))
                newFolderName = newFolderName+"/";

            return entityOldFolderName.replace(oldFolderName, newFolderName);
        }else{
            var parent = getParent(getParent(oldFolderName));
            newFolderName = parent+newFolderName;

            if(!newFolderName.endsWith("/"))
                newFolderName = newFolderName+"/";

            return entityOldFolderName.replace(oldFolderName, newFolderName);
        }
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

            if(path.endsWith("/"))
                result += "/";

            return result;
        }catch (Exception e){
            return "";
        }
    }

    @Override
    public String getCleanName(String path){
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
    public String subtraction(String minuend, String subtrahend) {
        var subtrahendIndex = minuend.indexOf(subtrahend);
        return minuend.substring(subtrahendIndex+subtrahend.length());
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

    @Override
    public boolean isFolder(String path) {
        return path.endsWith("/");
    }

    private String removeLastSlash(String path){
        var parent = getParent(path);
        var name = getCleanName(path);
        return parent+name;
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
    public List<StorageEntity> getAllFiles() {
        return getAll(TokenType.FILE);
    }

    @Override
    public List<StorageEntity> getAllDirectory() {
       return getAll(TokenType.FOLDER);
    }

    private List<StorageEntity> getAll(TokenType type){
        List<StorageEntity> entities = new ArrayList<>();
        var iterator = this.tree.getTreeIterator();

        while (iterator.hasNext()) {
            var entity = iterator.next();
            if(entity.getType() == type){
                if(entity.getEntity() == null)
                    continue;
                entities.add(entity.getEntity());
            }
        }

        return entities;
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
