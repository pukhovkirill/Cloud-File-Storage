package com.cfs.cloudfilestorage.service.storage.Impl;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;

import java.util.HashMap;
import java.util.Map;

public class StorageSwitch {
    private final Map<String, StorageCommand> commands = new HashMap<>();

    public void register(String name, StorageCommand command){
        commands.put(name, command);
    }

    public void execute(String name, StorageEntity entity, Object ... args){
        var command = commands.get(name);

        if(command == null){
            throw new IllegalStateException();
        }

        command.execute(entity, args);
    }
}

