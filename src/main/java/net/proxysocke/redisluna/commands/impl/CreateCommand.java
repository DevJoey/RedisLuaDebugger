package net.proxysocke.redisluna.commands.impl;

import net.proxysocke.redisluna.App;
import net.proxysocke.redisluna.commands.CommandExecutor;
import net.proxysocke.redisluna.commands.CommandSender;
import net.proxysocke.redisluna.session.ScriptSessionManager;

public final class CreateCommand implements CommandExecutor {

    private final App app;

    public CreateCommand(App app) {
        this.app = app;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2){
            sender.sendMessage("Invalid syntax. Use: create <SessionName> <ScriptName>");
            return;
        }
        String sessionNameArg = args[0];
        String scriptNameArg = collectArgs(args, 1);
        ScriptSessionManager scriptSessionManager = app.getScriptSessionManager();
        switch (scriptSessionManager.createSession(sessionNameArg, scriptNameArg)){
            case CREATED -> sender.sendMessage(String.format("Session successfully created: '%s' with script '%s'",
                    sessionNameArg, scriptNameArg));
            case ALREADY_EXISTS -> sender.sendMessage(String.format("Session '%s' already exists.",
                    sessionNameArg));
            case SCRIPT_NOT_FOUND -> sender.sendMessage(String.format("Script '%s' could not be found.",
                    scriptNameArg));
        }
    }

    private String collectArgs(String[] args, int begin) {
        String[] collected = new String[args.length - begin];
        int index = 0;
        for(int i = begin; i < args.length; i++){
            collected[index] = args[i];
            index++;
        }
        return String.join(" ", collected);
    }
}