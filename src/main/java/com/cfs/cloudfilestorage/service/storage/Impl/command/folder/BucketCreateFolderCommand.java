package com.cfs.cloudfilestorage.service.storage.Impl.command.folder;

import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import com.cfs.cloudfilestorage.util.MinioUtility;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class BucketCreateFolderCommand extends StorageCommand<FolderDto> {
    @Override
    protected <E extends StorageEntity> void action(E entity, Object... args) {
        try{
            if(entity instanceof FolderDto item){
                var client = MinioUtility.getClient();

                client.putObject(
                        PutObjectArgs.builder().bucket(BUCKET_NAME).object(item.getName()+"/").stream(
                                new ByteArrayInputStream(new byte[] {}), 0, -1).build());

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
