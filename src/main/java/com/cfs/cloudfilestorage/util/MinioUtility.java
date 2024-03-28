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

    private static final String host;
    private static final String port;
    private static final String accessKey;
    private static final String secreteKey;

    @Data
    private static class MinioConnection{
        private MinioClient client;
        private boolean inUse = false;
    }

    static{
        host = PropertiesUtility.getApplicationProperty("app.minio_host");
        port = PropertiesUtility.getApplicationProperty("app.minio_port_1");
        accessKey = PropertiesUtility.getApplicationProperty("app.minio_access_key");
        secreteKey = PropertiesUtility.getApplicationProperty("app.minio_secrete_key");

        connections = new HashSet<>();
        for(int i = 0; i < COUNT_OF_CONNECTIONS; i++){
            var minioConnection = new MinioConnection();
            minioConnection.client = buildClient();
            connections.add(minioConnection);
        }

        var initiator = getClient();
        initMinio(initiator);
        releaseClient(initiator);
    }

    private static void initMinio(MinioClient client){
        try{
            var bucketName = PropertiesUtility.getApplicationProperty("app.minio_bucket_name");
            boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

            if(!found){
                client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        }catch (MinioException e){
            System.err.println("Error occurred: " + e);
            System.err.println("HTTP trace: " + e.httpTrace());
        }catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

    }

    private static MinioClient buildClient(){

        return MinioClient.builder()
                        .endpoint(host, Integer.parseInt(port), false)
                        .credentials(accessKey, secreteKey)
                        .build();
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
