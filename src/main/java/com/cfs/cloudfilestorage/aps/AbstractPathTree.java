package com.cfs.cloudfilestorage.aps;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import lombok.Builder;

import java.util.*;

public class AbstractPathTree implements APS{

    private PathNode root;

    public AbstractPathTree(String rootName){
        initTree(rootName);
    }

    private void initTree(String rootName){
        var token = Token.builder()
                .type(TokenType.FOLDER)
                .name(rootName)
                .path(rootName+"/")
                .build();

        this.root = PathNode.builder()
                .token(token)
                .children(new ArrayList<>())
                .build();
    }

    @Builder
    private static class Token{
        private TokenType type;
        private String name;
        private String path;
        private StorageEntity dto;
    }

    @Builder
    public static class PathNode{
        private Token token;

        private final List<PathNode> children;

        public TokenType getType(){
            return token.type;
        }

        public String getName(){
            return token.name;
        }

        public String getPath(){
            return token.path;
        }

        public StorageEntity getEntity(){
            return token.dto;
        }

        public Iterator<PathNode> getChildren(){
            return children.iterator();
        }

        public void addChild(PathNode node){
            children.add(node);
        }
    }


    @Override
    public Iterator<PathNode> getRoot() {
        return this.root.getChildren();
    }

    @Override
    public boolean pathExists(String path) {
        return find(path) != null;
    }

    @Override
    public Iterator<PathNode> getFolder(String folder){
        var pathNode = find(folder);

        if(pathNode == null)
            return null;

        return find(folder).getChildren();
    }

    @Override
    public void buildTreeByPath(List<StorageEntity> entities) {
        for(var entity : entities){
            var root = find(entity);
            if(root == null){
                if(entity.getContentType().equals("folder")){
                    buildFoldersByPath(entity.getPath(), entity);
                    continue;
                }

                var folderPath = entity.getPath().replace(entity.getName(), "");
                root = buildFoldersByPath(folderPath, entity);
            }

            var token = Token.builder()
                    .type(TokenType.FILE)
                    .name(entity.getName())
                    .path(entity.getPath())
                    .dto(entity)
                    .build();

            var node = PathNode.builder()
                    .token(token)
                    .build();

            root.addChild(node);
        }
    }

    private PathNode buildFoldersByPath(String path, StorageEntity entity){
        PathNode start;

        var folders = path.split("/");
        var lastFolder = folders[folders.length-1];
        var nextPath = replaceLast(path, lastFolder+"/", "");

        if((start = find(path)) == null)
            start = buildFoldersByPath(nextPath, entity);
        else
            return start;

        var dto = StorageEntity.builder()
                .name(lastFolder)
                .path(path)
                .owner(entity.getOwner())
                .lastModified(entity.getLastModified())
                .contentType("folder")
                .build();

        var token = Token.builder()
                .type(TokenType.FOLDER)
                .name(lastFolder)
                .path(path)
                .dto(dto)
                .build();

        var node = PathNode.builder()
                .token(token)
                .children(new ArrayList<>())
                .build();

        start.addChild(node);
        return start;
    }

    public static String replaceLast(String string, String toReplace, String replacement) {
        int pos = string.lastIndexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos)
                    + replacement
                    + string.substring(pos + toReplace.length());
        } else {
            return string;
        }
    }

    private boolean isExist(StorageEntity entity){
        return find(entity) != null;
    }

    private PathNode find(StorageEntity entity){
        var iterator = new BFSIterator(this.root);

        while(iterator.hasNext()){
            var path = iterator.next();
            if(path.getPath().equals(entity.getPath()))
                return path;
        }

        return null;
    }

    private PathNode find(String entity){
        var iterator = new BFSIterator(this.root);

        while(iterator.hasNext()){
            var path = iterator.next();
            if(path.getPath().equals(entity))
                return path;
        }

        return null;
    }

    private static class BFSIterator implements Iterator<PathNode>{
        private final Queue<PathNode> queue;

        public BFSIterator(PathNode root){
            queue = new ArrayDeque<>();
            queue.add(root);
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public PathNode next() {
            var path = queue.poll();

            if(path.children != null)
                queue.addAll(path.children);

            return path;
        }
    }
}
