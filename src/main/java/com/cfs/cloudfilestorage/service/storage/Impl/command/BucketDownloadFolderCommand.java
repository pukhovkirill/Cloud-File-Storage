package com.cfs.cloudfilestorage.service.storage.Impl.command;

import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import com.cfs.cloudfilestorage.util.MinioUtility;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class BucketDownloadFolderCommand extends StorageCommand {
    @Override
    protected void action(StorageEntity entity, Object... args) {

        if(!entity.getContentType().equals("folder"))
            return;

        try{
            var client = MinioUtility.getClient();

            Iterable<Result<Item>> results = client.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(BUCKET_NAME)
                            .prefix(entity.getName())
                            .build());

            int itemCount = folderItemCount(results);
            byte[][] folder = new byte[itemCount][];

            // check folder nesting

            int i = 0;
            for(var result : results){
                var out = new ByteArrayOutputStream();

                var folderItem = result.get();
                var itemName = folderItem.objectName();

                InputStream in = client.getObject(
                        GetObjectArgs.builder()
                                .bucket(BUCKET_NAME)
                                .object(itemName)
                                .build());

                byte[] buff = new byte[1024];

                int count;
                while ((count = in.read(buff)) >= 0){
                    out.write(buff, 0, count);
                }

                folder[i++] = out.toByteArray();

                out.close();
                in.close();
            }

            entity.setBytesMatrix(folder);
            MinioUtility.releaseClient(client);

        }catch (MinioException e){
            System.err.println("Error occurred: " + e);
            System.err.println("HTTP trace: " + e.httpTrace());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private int folderItemCount(Iterable<Result<Item>> folder){
        int count = 0;

        var iterator = folder.iterator();
        while(iterator.hasNext()){
            count++;
            iterator.next();
        }

        return count;
    }
}
