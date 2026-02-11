package net.proxysocke.redisluna.commands;

import org.jline.terminal.Terminal;

public class CommandSender {

    private final Terminal terminal;

    public CommandSender(Terminal terminal) {
        this.terminal = terminal;
    }

    public void sendMessage(String string) {
        terminal.writer().println(string);
        terminal.flush();
    }
}