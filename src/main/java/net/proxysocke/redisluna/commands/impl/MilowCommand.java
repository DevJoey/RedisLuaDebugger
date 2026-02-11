package net.proxysocke.redisluna.commands.impl;

import net.proxysocke.redisluna.commands.CommandExecutor;
import net.proxysocke.redisluna.commands.CommandSender;

public class MilowCommand implements CommandExecutor {

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(String.format("Du hast %s eingegeben.", args[1]));
        throw new IllegalArgumentException("Test");
    }
}
