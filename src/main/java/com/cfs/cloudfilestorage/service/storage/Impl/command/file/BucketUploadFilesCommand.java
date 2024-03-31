package com.cfs.cloudfilestorage.service.storage.Impl.command.file;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import com.cfs.cloudfilestorage.util.MinioUtility;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class BucketUploadFilesCommand extends StorageCommand {
    @Override
    protected void action(StorageEntity entity, Object... args) {
        if(args[0] instanceof StorageEntity[] entities){
            try{
                var client = MinioUtility.getClient();

                for(var item : entities){
                    byte[] buff = item.getBytes();

                    client.putObject(
                            PutObjectArgs.builder()
                                    .bucket(BUCKET_NAME)
                                    .object(item.getPath())
                                    .stream(new ByteArrayInputStream(buff), item.getSize(), -1)
                                    .contentType(item.getContentType())
                                    .build());
                }

                MinioUtility.releaseClient(client);
            }catch (MinioException e){
                System.err.println("Error occurred: " + e);
                System.err.println("HTTP trace: " + e.httpTrace());
            } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
