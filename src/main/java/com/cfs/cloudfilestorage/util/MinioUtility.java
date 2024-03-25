package com.cfs.cloudfilestorage.util;

import io.minio.MinioClient;
import lombok.Data;
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
    }

    private static MinioClient buildClient(){

        return MinioClient.builder()
                        .endpoint(host, Integer.parseInt(port), true)
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
