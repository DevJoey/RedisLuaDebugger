package net.proxysocke.redisluna.commands.impl;

import net.proxysocke.redisluna.App;
import net.proxysocke.redisluna.commands.CommandExecutor;
import net.proxysocke.redisluna.commands.CommandSender;
import net.proxysocke.redisluna.session.ScriptSession;

public final class KeysCommand implements CommandExecutor {

    private final App app;

    public KeysCommand(App app) {
        this.app = app;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2){
            sender.sendMessage("Invalid syntax: keys <SessionName> <key1,key2...>");
            return;
        }
        ScriptSession session = app.getScriptSessionManager().getSession(args[0]);
        if(session == null){
            sender.sendMessage(String.format("Failed to set keys. Session '%s' not found.", args[0]));
            return;
        }
        if(!args[1].matches("[a-zA-Z0-9]+")
                && !args[1].matches("([a-zA-Z0-9]+,[a-zA-Z0-9]+)+")){
            sender.sendMessage("Invalid syntax for keys: Keys must be seperated by a comma. Example: key1,key2");
            return;
        }
        String[] keys = args[1].split(",");
        session.setKeys(keys);
        sender.sendMessage(String.format("Successfully set key for session '%s': %s",
                args[0], String.join(",", keys)));
    }
}