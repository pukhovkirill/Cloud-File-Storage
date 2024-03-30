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

    @Override
    public TreeState saveState() {
        return TreeState.builder()
                .type(TokenType.FOLDER)
                .name(this.root.token.name)
                .path(this.root.token.path)
                .entity(this.root.token.dto)
                .children(this.root.children)
                .build();
    }

    @Override
    public void restoreState(TreeState state) {

        var token = Token.builder()
                .type(state.getType())
                .name(state.getName())
                .path(state.getPath())
                .dto(state.getEntity())
                .build();

        this.root = PathNode.builder()
                .token(token)
                .children(state.getChildren())
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
    public void addFile(StorageEntity fileDto){
        if(!isExist(fileDto)){
            var folderPath = fileDto.getPath().replace(fileDto.getName(), "");
            var folder = find(
                    StorageEntity.builder()
                    .path(folderPath)
                    .build()
            );

            if(folder == null){
                return;
            }

            var token = Token.builder()
                    .name(fileDto.getName())
                    .path(fileDto.getPath())
                    .type(TokenType.FILE)
                    .build();

            var node = PathNode.builder()
                    .token(token)
                    .build();

            folder.addChild(node);
        }
    }

    @Override
    public void addFolder(StorageEntity folderDto){
        if(!isExist(folderDto)){
            var folderPath = folderDto.getPath().replace(folderDto.getName(), "");
            var folder = find(
                    StorageEntity.builder()
                            .path(folderPath)
                            .build()
            );

            if(folder == null){
                return;
            }

            var token = Token.builder()
                    .name(folderDto.getName())
                    .path((folderDto.getPath()))
                    .type(TokenType.FOLDER)
                    .build();

            var node = PathNode.builder()
                    .token(token)
                    .children(new ArrayList<>())
                    .build();

            folder.addChild(node);
        }
    }

    @Override
    public Iterator<PathNode> getFolder(StorageEntity entity){
        return find(entity).getChildren();
    }

    @Override
    public Iterator<PathNode> buildTreeByPath(List<StorageEntity> entities) {
        for(var entity : entities){
            var root = find(entity);
            if(root == null){
                if(entity.getContentType().equals("folder")){
                    buildFoldersByPath(entity.getPath());
                    continue;
                }

                var folderPath = entity.getPath().replace(entity.getName(), "");
                root = buildFoldersByPath(folderPath);
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

        return this.root.getChildren();
    }

    private PathNode buildFoldersByPath(String path){
        PathNode start = this.root;

        var folders = path.split("/");
        var lastFolder = folders[folders.length-1];
        var nextPath = path.replace(lastFolder+"/", "");

        if(find(path) == null)
            start = buildFoldersByPath(nextPath);
        else
            return start;

        var token = Token.builder()
                .type(TokenType.FOLDER)
                .name(lastFolder)
                .path(nextPath)
                .build();

        var node = PathNode.builder()
                .token(token)
                .children(new ArrayList<>())
                .build();

        start.addChild(node);
        return start;
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
            queue.addAll(path.children);

            return path;
        }
    }
}
