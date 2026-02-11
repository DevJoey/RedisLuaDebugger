package net.proxysocke.redisluna.config;

import net.proxysocke.redisluna.config.sections.RedisCredentials;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public final class RedisConfig {

    private final Properties properties = new Properties();

    public void load() throws IOException {
        properties.clear();
        File propertiesFile = new File("redis.properties");
        properties.load(new FileReader(propertiesFile));
    }

    public RedisCredentials getCredentials(){
        String host = properties.getProperty("host");
        String user = properties.getProperty("username");
        String password = properties.getProperty("password");
        int port = Integer.parseInt(properties.getProperty("port"));
        int database = Integer.parseInt(properties.getProperty("database"));
        return new RedisCredentials(host, port, database, user, password);
    }
}