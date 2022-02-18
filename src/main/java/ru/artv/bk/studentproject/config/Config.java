package ru.artv.bk.studentproject.config;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static final String DB_URL = "db.url";
    public static final String DB_LOGIN =  "db.login";
    public static final String DB_PASSWORD = "db.password";
    public static final String DB_LIMIT = "db.limit";

    private static Properties properties = new Properties();
    public synchronized static String getProperty(String propertyName){
        if (properties.isEmpty()){
            try(InputStream is = Config.class.getClassLoader().getResourceAsStream("Dao.properties")) {
                properties.load(is);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }
        return properties.getProperty(propertyName);
    }

}
