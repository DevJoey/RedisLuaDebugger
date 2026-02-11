package net.proxysocke.redisluna.config.sections;

public record RedisCredentials(String host, int port, int db, String username, String password) {}