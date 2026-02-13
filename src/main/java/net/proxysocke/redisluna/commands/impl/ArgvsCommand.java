package net.proxysocke.redisluna.commands.impl;

import net.proxysocke.redisluna.App;
import net.proxysocke.redisluna.commands.CommandExecutor;
import net.proxysocke.redisluna.commands.CommandSender;
import net.proxysocke.redisluna.session.ScriptSession;

public final class ArgvsCommand implements CommandExecutor {

    private final App app;

    public ArgvsCommand(App app) {
        this.app = app;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2){
            sender.sendMessage("Invalid syntax: argvs <SessionName> <arg1,arg2...>");
            return;
        }
        ScriptSession session = app.getScriptSessionManager().getSession(args[0]);
        if(session == null){
            sender.sendMessage(String.format("Failed to set argvs. Session '%s' not found.", args[0]));
            return;
        }
        if(!args[1].matches("[\\w%]+")
                && !args[1].matches("[\\w%]+(,[\\w%]+)+")){
            sender.sendMessage("Invalid syntax for argvs: Args must be seperated by a comma. Example: arg1,arg2");
            return;
        }
        String[] argvs = args[1].split(",");
        session.setArgvs(argvs);
        sender.sendMessage(String.format("Successfully set argvs for session '%s': %s",
                args[0], String.join(",", argvs)));
    }
}