package com.cfs.cloudfilestorage.service.storage.Impl.command.folder;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.dto.FolderDto;
import com.cfs.cloudfilestorage.dto.StorageEntity;
import com.cfs.cloudfilestorage.model.File;
import com.cfs.cloudfilestorage.service.storage.StorageCommand;
import com.cfs.cloudfilestorage.util.MinioUtility;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class BucketUploadFolderCommand extends StorageCommand<FolderDto> {
    @Override
    protected <E extends StorageEntity> void action(E entity, Object... args) {
        if(entity instanceof FileDto item){
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(item.getPath()))) {
                var folderName = Paths.get(item.getPath()).getFileName();
                for (Path path : stream) {
                    if (Files.isDirectory(path))
                        continue;

                    var fileName = path.getFileName();

                    var client = MinioUtility.getClient();

                    client.uploadObject(
                            UploadObjectArgs.builder()
                                    .bucket(BUCKET_NAME)
                                    .object(folderName+"/"+fileName)
                                    .filename(path.toUri().getPath())
                                    .contentType(Files.probeContentType(path))
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
}
