package com.cfs.cloudfilestorage.service.path;

import com.cfs.cloudfilestorage.aps.PathView;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

@Service
public class StoragePathManageServiceImpl implements StoragePathManageService {

    private final PathStringRefactorService pathStrService;

    public StoragePathManageServiceImpl(PathStringRefactorService pathStringRefactorService) {
        this.pathStrService = pathStringRefactorService;
    }

    @Override
    public String createFolderName(String folderName, String currentPath) {
        try {
            String resultFolderPath;

            if(folderName.isEmpty()){
                folderName = "New_folder";
            }

            var workingDir = pathStrService.getCleanName(currentPath);

            if(workingDir.equals("vault")){
                folderName = pathStrService.addBackSlash(folderName);
                resultFolderPath = pathStrService.addRoot(folderName);
            }else{
                if(isBase64(workingDir)){
                    workingDir = new String(Base64.getDecoder().decode(workingDir.getBytes()), StandardCharsets.UTF_8);
                    workingDir = java.net.URLDecoder.decode(workingDir, StandardCharsets.UTF_8);
                }
                resultFolderPath = makeFolderPath(workingDir, folderName);
            }

            return resultFolderPath;
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            return "";
        }
    }

    @Override
    public String createFileName(String fileName, String currentPath) {
        try {
            String resultFolderPath;
            var workingDir = pathStrService.getOriginalName(currentPath);

            if(workingDir.equals("vault")){
                resultFolderPath = pathStrService.addRoot(fileName);
            }else{
                if(isBase64(workingDir)){
                    workingDir = new String(Base64.getDecoder().decode(workingDir.getBytes()), StandardCharsets.UTF_8);
                    workingDir = java.net.URLDecoder.decode(workingDir, StandardCharsets.UTF_8);
                }

                resultFolderPath = makeFilePath(workingDir, fileName);
            }

            return resultFolderPath;
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            return "";
        }
    }

    @Override
    public String renameFile(String oldName, String newName) {
        try {
            var parent = pathStrService.getParent(oldName);
            return parent+newName;
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            return "";
        }
    }

    @Override
    public String renameFolder(String entityOldFolderName, String oldFolderName, String newFolderName) {
        try {
            if(isFolder(entityOldFolderName)){
                var parent = pathStrService.getParent(oldFolderName);
                newFolderName = parent+newFolderName;

                newFolderName = pathStrService.addBackSlash(newFolderName);

                return entityOldFolderName.replace(oldFolderName, newFolderName);
            }else{
                var parent = pathStrService.getParent(pathStrService.getParent(oldFolderName));
                newFolderName = parent+newFolderName;

                newFolderName = pathStrService.addBackSlash(newFolderName);

                return entityOldFolderName.replace(oldFolderName, newFolderName);
            }
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            return "";
        }
    }

    @Override
    public PathView getPathView(String path) {
        try{
            var fullPath = path;
            path = pathStrService.removeBackSlash(path);

            var parent = pathStrService.getParent(path);
            var workingDirectory = pathStrService.getOriginalName(path);

            String root;
            if(!path.startsWith((root = pathStrService.getPersonRootFolder()))){
                parent = pathStrService.addBackSlash(root);
                fullPath = parent+path;
                workingDirectory = path;
            }

            return PathView.builder()
                    .path(parent.split("/"))
                    .workingDirectory(workingDirectory)
                    .fullPath(fullPath)
                    .build();
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            return PathView.builder()
                    .path(new String[0])
                    .workingDirectory("")
                    .fullPath("")
                    .build();
        }
    }

    @Override
    public boolean isFolder(String path) {
        return path.endsWith("/");
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

    private String makeFilePath(String workingDirectory, String fileName){
        String[] folders = workingDirectory.split("/");

        StringBuilder path = new StringBuilder();
        for(var folder : folders){
            path.append(folder).append("/");
        }
        path.append(fileName);

        return path.toString();
    }

    public boolean isBase64(String str) {
        String pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$";
        return Pattern.matches(pattern, str);
    }
}
