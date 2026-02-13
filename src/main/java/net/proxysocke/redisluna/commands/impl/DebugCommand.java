package net.proxysocke.redisluna.commands.impl;

import net.proxysocke.redisluna.App;
import net.proxysocke.redisluna.commands.CommandExecutor;
import net.proxysocke.redisluna.commands.CommandSender;
import net.proxysocke.redisluna.session.ScriptSession;
import net.proxysocke.redisluna.session.ScriptSessionManager;

import redis.clients.jedis.RedisClient;
import redis.clients.jedis.exceptions.JedisDataException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Level;

public final class DebugCommand implements CommandExecutor {

    private final App app;
    private final ScriptSessionManager scriptSessionManager;

    public DebugCommand(App app) {
        this.app = app;
        this.scriptSessionManager = app.getScriptSessionManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            // Letzte Session ausführen
            ScriptSession lastSession = scriptSessionManager.getLastSession();
            if (lastSession == null) {
                sender.sendMessage("There is no last session to debug. Use: debug <SessionName>");
                return;
            }
            runScript(sender, lastSession);
            return;
        }
        // Explizit angegebene Session ausführen
        ScriptSession scriptSession = app.getScriptSessionManager().getSession(args[0]);
        if (scriptSession == null) {
            sender.sendMessage(String.format("There is no session named '%s'", args[0]));
            return;
        }
        app.getScriptSessionManager().setLastSession(scriptSession);
        runScript(sender, scriptSession);
    }

    private void runScript(CommandSender sender, ScriptSession session) {
        Path scriptPath = session.getScriptFilePath();
        String[] keys = session.getKeys();
        String[] argvs = session.getArgvs();
        if (Files.notExists(scriptPath)) {
            sender.sendMessage(String.format("The script for this session does no longer exists at: %s",
                    scriptPath));
            return;
        }
        try {
            String scriptContent = Files.readString(scriptPath, StandardCharsets.UTF_8);
            RedisClient redis = app.getRedisProvider().getRedisClient();
            String[] keysAndArgvsMerged = mergeArrays(keys, argvs);
            String[] mergedKeysAndArgvsWithPlaceholders = parsePlaceholders(keysAndArgvsMerged);
            Object redisResponse = redis.eval(scriptContent, keys.length, mergedKeysAndArgvsWithPlaceholders);
            sender.sendMessage(String.format("[Redis]: %s", redisResponse));
        } catch (IOException ioe) {
            app.getLogger().log(Level.SEVERE, String.format("Failed to load script: %s", scriptPath), ioe);
        } catch (JedisDataException jde) {
            app.getLogger().log(Level.SEVERE, String.format("Redis error on EVAL: %s", jde.getMessage()), jde);
        }
    }

    private String[] mergeArrays(String[] array1, String[] array2) {
        String[] merged = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, merged, 0, array1.length);
        int array2Start = array1.length;
        for (int i = 0; i < array2.length; i++) {
            merged[array2Start] = array2[i];
            array2Start++;
        }
        return merged;
    }

    private String[] parsePlaceholders(String[] array) {
        String[] parsed = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            String value = array[i].replace("%millis%", String.valueOf(System.currentTimeMillis()))
                    .replace("%uuid%", UUID.randomUUID().toString());
            parsed[i] = value;
        }
        return parsed;
    }
}