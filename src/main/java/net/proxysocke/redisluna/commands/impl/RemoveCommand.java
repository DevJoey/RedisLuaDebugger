package net.proxysocke.redisluna.commands.impl;

import net.proxysocke.redisluna.App;
import net.proxysocke.redisluna.commands.CommandExecutor;
import net.proxysocke.redisluna.commands.CommandSender;
import net.proxysocke.redisluna.session.ScriptSession;
import net.proxysocke.redisluna.session.ScriptSessionManager;

public final class RemoveCommand implements CommandExecutor {

    private final App app;

    public RemoveCommand(App app) {
        this.app = app;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage("Invalid syntax: remove <SessionName>");
            return;
        }
        ScriptSessionManager scriptSessionManager = app.getScriptSessionManager();
        ScriptSession scriptSession = scriptSessionManager.removeSession(args[0]);
        if(scriptSession == null){
            sender.sendMessage(String.format("There is no session named '%s'", args[0]));
            return;
        }
        if(scriptSessionManager.getLastSession() == scriptSession){
            // Die letzte Session ist die selbe, die gerade entfernt wurde
            scriptSessionManager.setLastSession(null);
        }
        sender.sendMessage(String.format("Session '%s' was successfully removed.", args[0]));
    }
}