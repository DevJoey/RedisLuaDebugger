package net.proxysocke.redisluna.commands.impl;

import net.proxysocke.redisluna.commands.CommandExecutor;
import net.proxysocke.redisluna.commands.CommandSender;

public final class HelpCommand implements CommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("""
                ################ Help ################
                > create <SessionName> <ScriptName> - Creates a session
                > remove <SessionName> - Removes a session
                > keys <SessionName> <key1,key2...> - Sets the keys for a session
                > argvs <SessionName> <arg1,arg2...> - Sets the argvs for a session
                ################ Help ################""");
    }
}
