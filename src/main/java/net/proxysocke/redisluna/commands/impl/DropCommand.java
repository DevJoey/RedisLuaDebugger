package net.proxysocke.redisluna.commands.impl;

import net.proxysocke.redisluna.App;
import net.proxysocke.redisluna.commands.CommandExecutor;
import net.proxysocke.redisluna.commands.CommandSender;


public final class DropCommand implements CommandExecutor {

    private final App app;

    public DropCommand(App app) {
        this.app = app;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        app.getScriptSessionManager().dropSessions();
        sender.sendMessage("Successfully deleted all sessions.");
    }
}
