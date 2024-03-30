package com.cfs.cloudfilestorage.service.storage.Impl.command.file;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.repository.ItemRepository;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;

public class DBRemoveFileCommand extends StorageCommand {

    private final ItemRepository itemRepository;

    public DBRemoveFileCommand(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @Override
    protected void action(StorageEntity entity, Object ... args) {
        itemRepository.deleteById(entity.getId());
    }
}
