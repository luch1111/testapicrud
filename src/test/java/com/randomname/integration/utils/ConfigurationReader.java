package com.randomname.integration.utils;

import com.sun.istack.NotNull;

import java.io.InputStream;
import java.util.Properties;

public class ConfigurationReader {

    private Properties configs = new Properties();

    public ConfigurationReader(@NotNull String propertyName) {
        try {
            String configPath = propertyName + ".properties";
            InputStream is = ConfigurationReader.class.getClassLoader().getResourceAsStream(configPath);
            if(is == null) {
                throw new RuntimeException("Cannot execute tests with undefined configuration: " + configPath);
            }
            configs.load(is);
            configs.entrySet();
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    public String getConfig(String key){
        return configs.get(key).toString();
    }
}
