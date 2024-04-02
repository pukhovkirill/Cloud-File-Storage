package com.cfs.cloudfilestorage.service.storage.Impl.command;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.repository.ItemRepository;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;

import java.io.FileNotFoundException;

public class DBRenameCommand extends StorageCommand {

    private final ItemRepository itemRepository;

    public DBRenameCommand(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @Override
    protected void action(StorageEntity entity, Object ... args) {
        String newFileName = (String) args[0];
        try {
            var optItem = itemRepository.findById(entity.getId());

            if(optItem.isEmpty()){
                throw new FileNotFoundException();
            }

            var item = optItem.get();
            item.setName(newFileName);

            itemRepository.save(item);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
