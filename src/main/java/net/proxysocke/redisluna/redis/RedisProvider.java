package net.proxysocke.redisluna.redis;

import redis.clients.jedis.*;

import java.time.Duration;

public class RedisProvider {

    private RedisClient redisClient;

    public RedisProvider() {
        ConnectionPoolConfig poolConfig = new ConnectionPoolConfig();
        poolConfig.setMaxTotal(5);
        poolConfig.setMaxIdle(3);
        poolConfig.setMaxWait(Duration.ofSeconds(5));

        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder()
                .clientName("RedisLuaDebugger")
                .user("minecraft")
                .password("test")
                .build();

        redisClient = RedisClient.builder()
                .poolConfig(poolConfig)
                .clientConfig(clientConfig)
                .hostAndPort("host", 6379)
                .build();
    }

    public void connect() {
        redisClient.ping();
    }

    public RedisClient getRedisClient() {
        return redisClient;
    }
}