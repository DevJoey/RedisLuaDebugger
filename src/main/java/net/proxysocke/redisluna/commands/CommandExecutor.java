package net.proxysocke.redisluna.commands;

public interface CommandExecutor {

    void execute(CommandSender sender, String[] args);

}