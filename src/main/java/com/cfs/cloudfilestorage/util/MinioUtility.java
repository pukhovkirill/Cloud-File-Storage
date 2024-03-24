package com.cfs.cloudfilestorage.util;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.Data;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;


public class MinioUtility {
    private static final int COUNT_OF_CONNECTIONS = 10;

    private static final Set<MinioConnection> connections;

    @Data
    private static class MinioConnection{
        private MinioClient client;
        private boolean inUse = false;
    }

    static{
        connections = new HashSet<>();
        for(int i = 0; i < COUNT_OF_CONNECTIONS; i++){
            var minioConnection = new MinioConnection();
            minioConnection.client = buildClient();
            connections.add(minioConnection);
        }

        var initiator = getClient();
        initWorkspace(initiator);
        releaseClient(initiator);
    }

    private static MinioClient buildClient(){
        return MinioClient.builder()
                        .endpoint("play.min.io", 9000, false)
                        .credentials("Q3AM3UQ867SPQQA43P2F", "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG")
                        .build();
    }

    private static void initWorkspace(MinioClient client){
        try {
            if(client.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket("user_files")
                            .build())
            ){
                client.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket("user_files")
                                .build()
                );
            }
        } catch (ErrorResponseException    |
                 InsufficientDataException |
                 InternalException         |
                 InvalidKeyException       |
                 InvalidResponseException  |
                 IOException               |
                 NoSuchAlgorithmException  |
                 ServerException           |
                 XmlParserException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static MinioClient getClient(){
        for(var connection : connections){
            if(!connection.inUse){
                connection.inUse = true;
                return connection.client;
            }
        }
        return null;
    }

    public static void releaseClient(MinioClient client){
        for(var connection : connections){
            if(connection.client.equals(client)){
                connection.inUse = false;
                return;
            }
        }
    }
}
