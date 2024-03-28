package com.cfs.cloudfilestorage.service.storage.Impl.command.folder;

import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import com.cfs.cloudfilestorage.util.MinioUtility;
import io.minio.DownloadObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class BucketDownloadFolderCommand extends StorageCommand<FolderDto> {
    @Override
    protected <E extends StorageEntity> void action(E entity, Object... args) {
        try{
            if(entity instanceof FolderDto item){
                var client = MinioUtility.getClient();

                Iterable<Result<Item>> results = client.listObjects(
                        ListObjectsArgs.builder()
                                .bucket(BUCKET_NAME)
                                .prefix(item.getName())
                                .build());

                for(var result : results){
                    var folderItem = result.get();
                    var itemName = folderItem.objectName();
                    client.downloadObject(
                            DownloadObjectArgs.builder()
                                    .bucket(BUCKET_NAME)
                                    .object(itemName)
                                    .filename(item.getPath()+"/"+item.getName()+"/"+itemName)
                                    .build());
                }

                MinioUtility.releaseClient(client);
            }

        }catch (MinioException e){
            System.err.println("Error occurred: " + e);
            System.err.println("HTTP trace: " + e.httpTrace());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
