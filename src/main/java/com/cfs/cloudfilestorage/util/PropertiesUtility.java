package com.cfs.cloudfilestorage.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtility {

    private static Properties applicationProperties;

    public static String getApplicationProperty(String key){
        if(applicationProperties == null)
            readApplicationProperties();

        return applicationProperties.getProperty(key);
    }

    private static void readApplicationProperties(){
        try{
            applicationProperties = new Properties();
            var fis = new FileInputStream(getResourcePath());
            applicationProperties.load(fis);
        }catch(FileNotFoundException ex){
            ex.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getResourcePath(){
        var url = PropertiesUtility.class.getClassLoader().getResource("app.properties");
        if(url == null)
            return "";
        else return url.getPath();
    }
}
