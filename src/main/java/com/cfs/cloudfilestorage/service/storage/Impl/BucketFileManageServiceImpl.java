package com.cfs.cloudfilestorage.service.storage.Impl;

import com.cfs.cloudfilestorage.dto.FileDto;
import com.cfs.cloudfilestorage.service.storage.FileManageService;
import com.cfs.cloudfilestorage.util.MinioUtility;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.cfs.cloudfilestorage.service.storage.Impl.BaseDBManager.BUCKET_NAME;

@Service("BucketFileManager")
public class BucketFileManageServiceImpl implements FileManageService {

    private final DBFileManageService dbFileManageService;

    public BucketFileManageServiceImpl(DBFileManageService dbFileManageService){
        this.dbFileManageService = dbFileManageService;
    }

    @Override
    public void create(String name) {

    }

    @Override
    public void upload(FileDto item) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try{
            dbFileManageService.upload(item);
            var client = MinioUtility.getClient();

            checkBucketOrCreate(client);

            client.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(item.getName())
                            .filename(item.getFileName())
                            .contentType(item.getContentType())
                            .build());

            MinioUtility.releaseClient(client);

        }catch (MinioException e){
            System.err.println("Error occurred: " + e);
            System.err.println("HTTP trace: " + e.httpTrace());
        }
    }

    @Override
    public void rename(FileDto item, String newName) {

    }

    @Override
    public FileDto download(String name, String saveAsName) {
        return null;
    }

    @Override
    public void remove(FileDto item) {

    }

    @Override
    public void share(FileDto item) {

    }

    private void checkBucketOrCreate(MinioClient client) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try{
            boolean found =
                    client.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());

            if(!found){
                client.makeBucket(
                        MakeBucketArgs.builder().bucket(BUCKET_NAME).build()
                );
            }
        } catch (MinioException e){
            System.err.println("Error occurred: " + e);
            System.err.println("HTTP trace: " + e.httpTrace());
        }
    }
}
