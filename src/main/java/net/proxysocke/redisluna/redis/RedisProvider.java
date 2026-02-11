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
                .password("49245fa8f04954da833d7ee5800060111a80c85f6024c1271a7518aee677dd83")
                .build();

        redisClient = RedisClient.builder()
                .poolConfig(poolConfig)
                .clientConfig(clientConfig)
                .hostAndPort("91.218.67.149", 6379)
                .build();
    }

    public void connect() {
        redisClient.ping();
    }

    public RedisClient getRedisClient() {
        return redisClient;
    }
}