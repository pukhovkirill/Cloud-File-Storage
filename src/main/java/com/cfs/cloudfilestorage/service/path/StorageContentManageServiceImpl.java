package com.cfs.cloudfilestorage.service.path;

import com.cfs.cloudfilestorage.aps.APS;
import com.cfs.cloudfilestorage.aps.AbstractPathTree;
import com.cfs.cloudfilestorage.aps.TokenType;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.StorageItem;
import com.cfs.cloudfilestorage.repository.SharedItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class StorageContentManageServiceImpl implements StorageContentManageService {

    private final PathStringRefactorService pathStrService;

    private final SharedItemRepository sharedItemRepository;

    private APS tree;

    public StorageContentManageServiceImpl(SharedItemRepository sharedItemRepository,
                                           PathStringRefactorService pathStringRefactorService) {
        this.pathStrService = pathStringRefactorService;
        this.sharedItemRepository = sharedItemRepository;
    }

    @Override
    public void buildStoragePath(List<StorageItem> files) {
        this.tree = new AbstractPathTree(pathStrService.getPersonRootFolder());
        List<StorageEntity> entities = new ArrayList<>();

        for(var item : files){
            var access = checkAvailability(item);
            var entity = new StorageEntity(item);
            entity.setIsAvailable(access);

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

    @Override
    public List<StorageEntity> goToRoot() {
        var iterator = this.tree.getRoot();
        return iteratorToList(iterator);
    }

    @Override
    public boolean pathExists(String path) {
        return tree.pathExists(path);
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

    private boolean checkAvailability(StorageItem item){
        var sharedAccess = sharedItemRepository.findByItem(item);

        if(sharedAccess == null){
            return false;
        }

        return sharedAccess.getIsShared();
    }
}
