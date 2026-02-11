package net.proxysocke.redisluna.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class RedisConfig {

    private final Properties properties = new Properties();

    public void load() throws IOException {
        properties.clear();
        File propertiesFile = new File("redis.properties");
        properties.load(new FileReader(propertiesFile));

    }

    public Properties getProperties() {
        return properties;
    }
}