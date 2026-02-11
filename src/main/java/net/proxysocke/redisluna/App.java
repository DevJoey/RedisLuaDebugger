package net.proxysocke.redisluna;

import net.proxysocke.redisluna.commands.CommandExecutor;
import net.proxysocke.redisluna.commands.CommandManager;
import net.proxysocke.redisluna.commands.CommandSender;
import net.proxysocke.redisluna.commands.impl.CreateCommand;
import net.proxysocke.redisluna.commands.impl.ScriptCommand;
import net.proxysocke.redisluna.commands.session.ScriptSessionManager;
import net.proxysocke.redisluna.config.RedisConfig;
import net.proxysocke.redisluna.redis.RedisProvider;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import javax.swing.*;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class App {

    private final RedisConfig redisConfig = new RedisConfig();
    private final CommandManager commandManager = new CommandManager();
    private final RedisProvider redisProvider = new RedisProvider();
    private final ScriptSessionManager scriptSessionManager = new ScriptSessionManager();

    private Logger logger;
    private Terminal terminal;
    private LineReader lineReader;

    public App() {
        setupFiles();
        setupTerminal();
        setupLogger();
        setupRedisProvider();
        commandManager.registerCommand("create", new CreateCommand(this));
    }

    public void start() {
        try {
            while (true) {
                String line = lineReader.readLine("> ");
                if (line == null || line.isBlank()) {
                    continue;
                }
                String[] commandArgs = line.split(" ");
                String[] executorArgs = new String[commandArgs.length - 1];
                System.arraycopy(commandArgs, 1, executorArgs, 0, commandArgs.length - 1);
                CommandExecutor executor = commandManager.getCommand(commandArgs[0]);
                if (executor == null) {
                    terminal.writer().println(String.format("Der Befehl '%s' wurde nicht gefunden.", commandArgs[0]));
                    continue;
                }
                CommandSender sender = new CommandSender(terminal);
                try {
                    executor.execute(sender, executorArgs);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Command failed", e);
                }
            }
        } catch (Exception e) {

        }
    }

    private void setupFiles() {
        try {
            redisConfig.load();
            System.out.println(redisConfig.getProperties().getProperty("database"));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void setupTerminal() {
        try {
            terminal = TerminalBuilder.builder().system(true).build();
            lineReader = LineReaderBuilder.builder().terminal(terminal).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupLogger() {
        logger = Logger.getLogger("RedisLunaLogger");
        Handler handler = new Handler() {
            @Override
            public void publish(LogRecord record) {
                String message = String.format("[%s]: %s", record.getLevel(), record.getMessage());
                terminal.writer().println(message);
                terminal.flush();
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        };
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
    }

    private void setupRedisProvider() {
        try {
            redisProvider.connect();
            logger.log(Level.SEVERE, "Redis connection successful");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Redis connection failed", e);
        }
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public LineReader getLineReader() {
        return lineReader;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ScriptSessionManager getScriptSessionManager() {
        return scriptSessionManager;
    }

    public RedisProvider getRedisProvider() {
        return redisProvider;
    }

    public Logger getLogger() {
        return logger;
    }
}