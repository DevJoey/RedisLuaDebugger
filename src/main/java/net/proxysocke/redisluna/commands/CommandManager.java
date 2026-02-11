package net.proxysocke.redisluna.commands;

import java.util.HashMap;
import java.util.Map;

public final class CommandManager {

    private final Map<String, CommandExecutor> commandMap = new HashMap<>();

    public void registerCommand(String alias, CommandExecutor executor){
        commandMap.put(alias.toLowerCase(), executor);
    }

    public CommandExecutor getCommand(String alias){
        return commandMap.get(alias.toLowerCase());
    }
}