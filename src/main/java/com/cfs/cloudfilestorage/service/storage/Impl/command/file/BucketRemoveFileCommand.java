package com.cfs.cloudfilestorage.service.storage.Impl.command.file;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import com.cfs.cloudfilestorage.util.MinioUtility;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class BucketRemoveFileCommand extends StorageCommand<FileDto> {
    @Override
    protected <E extends StorageEntity> void action(E entity, Object ... args) {
        try{
            if(entity instanceof FileDto item){
                var client = MinioUtility.getClient();

                client.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(BUCKET_NAME)
                                .object(item.getPath())
                                .build());

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
