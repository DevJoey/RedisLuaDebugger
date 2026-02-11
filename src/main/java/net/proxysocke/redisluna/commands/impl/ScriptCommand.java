package net.proxysocke.redisluna.commands.impl;

import net.proxysocke.redisluna.App;
import net.proxysocke.redisluna.commands.CommandExecutor;
import net.proxysocke.redisluna.commands.CommandSender;
import redis.clients.jedis.RedisClient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.logging.Level;

public class ScriptCommand implements CommandExecutor {

    private final App app;

    public ScriptCommand(App app) {
        this.app = app;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage("Ungültiger Syntax -> Nutze: script <Skript-Name> [key1,key2]");
            return;
        }
        String scriptName = args[0];
        Path scriptPath = Path.of("scripts/", scriptName + ".lua");
        if(Files.notExists(scriptPath)){
            sender.sendMessage(String.format("Das Skript '%s.lua' konnte nicht gefunden werden.", scriptName));
            return;
        }

        RedisClient redis = app.getRedisProvider().getRedisClient();





        Path scriptDir = Path.of("scripts/");
        app.getLogger().log(Level.INFO, String.valueOf(Files.exists(scriptDir)));
    }
}
