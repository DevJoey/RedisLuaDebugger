package net.proxysocke.redisluna.commands.impl;

import net.proxysocke.redisluna.commands.CommandExecutor;
import net.proxysocke.redisluna.commands.CommandSender;

public final class HelpCommand implements CommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("""
                ################ Help ################
                
                Redis Lua-Script debugger v1.0.0 by Joey
                
                Commands:
                ~ create <SessionName> <ScriptName> - Creates a session
                ~ remove <SessionName> - Removes a session
                ~ debug <SessionName> - Runs the session
                ~ keys <SessionName> <key1,key2...> - Sets the keys for a session
                ~ argvs <SessionName> <arg1,arg2...> - Sets the argvs for a session
                
                Placeholders:
                You can use placeholders in keys and argvs.
                ~ %uuid% - A random UUID
                ~ %millis% - Current time in milliseconds
                
                Log-Channel:
                You can send messages from your lua script
                to the program.
                
                Use: 'redis.call("PUBLISH", "rl:logs", "Your message")'
                
                ################ Help ################""");
    }
}
