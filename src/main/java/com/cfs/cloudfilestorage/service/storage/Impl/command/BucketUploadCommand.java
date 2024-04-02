package com.cfs.cloudfilestorage.service.storage.Impl.command;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import com.cfs.cloudfilestorage.util.MinioUtility;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class BucketUploadCommand extends StorageCommand {

    @Override
    protected void action(StorageEntity entity, Object ... args){
        try{
            var client = MinioUtility.getClient();

            byte[] buff = entity.getBytes();

            var putBuilder = PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(entity.getPath())
                    .stream(new ByteArrayInputStream(buff), entity.getSize(), -1);

            if(!entity.getContentType().equals("folder"))
                putBuilder.contentType(entity.getContentType());

            client.putObject(
                    putBuilder.build()
            );

            MinioUtility.releaseClient(client);

        }catch (MinioException e){
            System.err.println("Error occurred: " + e);
            System.err.println("HTTP trace: " + e.httpTrace());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}

