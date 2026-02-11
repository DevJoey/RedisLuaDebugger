package net.proxysocke.redisluna.redis;

import net.proxysocke.redisluna.config.sections.RedisCredentials;

import redis.clients.jedis.*;

import java.time.Duration;

public final class RedisProvider {

    private final RedisClient redisClient;

    public RedisProvider(RedisCredentials credentials) {
        ConnectionPoolConfig poolConfig = new ConnectionPoolConfig();
        poolConfig.setMaxTotal(5);
        poolConfig.setMaxIdle(3);
        poolConfig.setMaxWait(Duration.ofSeconds(5));

        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder()
                .clientName("RedisLuaDebugger")
                .user(credentials.username())
                .password(credentials.password())
                .build();

        redisClient = RedisClient.builder()
                .poolConfig(poolConfig)
                .clientConfig(clientConfig)
                .hostAndPort(credentials.host(), credentials.port())
                .build();
    }

    public void connect() {
        redisClient.ping();
    }

    public RedisClient getRedisClient() {
        return redisClient;
    }
}