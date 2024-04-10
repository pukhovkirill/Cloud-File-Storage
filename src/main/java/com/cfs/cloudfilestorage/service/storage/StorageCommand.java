package com.cfs.cloudfilestorage.service.storage;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.util.PropertiesUtility;

public abstract class StorageCommand{
    protected final static String BUCKET_NAME;
    protected final static String BUCKET_TRASH_NAME;

    static{
        BUCKET_NAME = PropertiesUtility.getApplicationProperty("app.minio_bucket_name");
        BUCKET_TRASH_NAME = PropertiesUtility.getApplicationProperty("app.minio_trash_bucket_name");
    }

    protected StorageCommand next;

    public StorageCommand setNext(StorageCommand next){
        this.next = next;
        return next;
    }

    public void execute(StorageEntity entity, Object ... args) {
        action(entity, args);

        if (next != null) {
            next.execute(entity, args);
        }
    }

    protected abstract void action(StorageEntity entity, Object ... args);
}
