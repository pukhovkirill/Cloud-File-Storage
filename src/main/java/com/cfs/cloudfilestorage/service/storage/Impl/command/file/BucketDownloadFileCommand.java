package com.cfs.cloudfilestorage.service.storage.Impl.command.file;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import com.cfs.cloudfilestorage.util.MinioUtility;
import io.minio.GetObjectArgs;
import io.minio.errors.MinioException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class BucketDownloadFileCommand extends StorageCommand {

    @Override
    protected void action(StorageEntity entity, Object ... args) {
        try{
            var client = MinioUtility.getClient();

            var out = new ByteArrayOutputStream();

            InputStream in = client.getObject(
                    GetObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(entity.getPath())
                            .build());

            byte[] buff = new byte[1024];

            int count;
            while ((count = in.read(buff)) >= 0){
                out.write(buff, 0, count);
            }

            entity.setBytes(out.toByteArray());

            out.close();
            in.close();

            MinioUtility.releaseClient(client);

        }catch (MinioException e){
            System.err.println("Error occurred: " + e);
            System.err.println("HTTP trace: " + e.httpTrace());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
