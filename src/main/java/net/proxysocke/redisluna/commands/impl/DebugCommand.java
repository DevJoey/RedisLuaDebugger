package net.proxysocke.redisluna.commands.impl;

import net.proxysocke.redisluna.App;
import net.proxysocke.redisluna.commands.CommandExecutor;
import net.proxysocke.redisluna.commands.CommandSender;
import net.proxysocke.redisluna.session.ScriptSession;
import net.proxysocke.redisluna.session.ScriptSessionManager;
import redis.clients.jedis.RedisClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class DebugCommand implements CommandExecutor {

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
            return;
        }
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
            String[] scriptArgumentsMerged = mergeArrays(keys, argvs);
            Object redisResponse = redis.eval(scriptContent, keys.length, scriptArgumentsMerged);
            sender.sendMessage(String.format("[Redis]: %s", redisResponse));
        } catch (IOException ioe) {
            app.getLogger().log(Level.SEVERE, String.format("Failed to load script: %s", scriptPath), ioe);
        }
    }

    private static String[] mergeArrays(String[] array1, String[] array2){
        String[] merged = new String[array1.length + array2.length];
        for(int i = 0; i < array1.length; i++){
            merged[i] = array1[i];
        }
        int array2Start = array1.length;
        for(int i = 0; i < array2.length; i++){
            merged[array2Start] = array2[i];
            array2Start++;
        }
        return merged;
    }
}