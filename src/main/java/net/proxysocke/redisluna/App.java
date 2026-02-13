package net.proxysocke.redisluna;

import net.proxysocke.redisluna.commands.CommandExecutor;
import net.proxysocke.redisluna.commands.CommandManager;
import net.proxysocke.redisluna.commands.CommandSender;
import net.proxysocke.redisluna.commands.impl.*;
import net.proxysocke.redisluna.config.sections.RedisCredentials;
import net.proxysocke.redisluna.redis.PubSubHandler;
import net.proxysocke.redisluna.session.ScriptSessionManager;
import net.proxysocke.redisluna.config.RedisConfig;
import net.proxysocke.redisluna.redis.RedisProvider;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.logging.*;

public final class App {

    private final RedisConfig redisConfig = new RedisConfig();
    private final ScriptSessionManager scriptSessionManager = new ScriptSessionManager();
    private final CommandManager commandManager = new CommandManager();

    private Terminal terminal;
    private LineReader lineReader;
    private Logger logger;
    private RedisProvider redisProvider;
    private PubSubHandler pubSubHandler;

    public App() {
        setupFiles();
        setupTerminal();
        setupLogger();
        setupRedisProvider();
        commandManager.registerCommand("help", new HelpCommand());
        commandManager.registerCommand("drop", new DropCommand(this));
        commandManager.registerCommand("create", new CreateCommand(this));
        commandManager.registerCommand("remove", new RemoveCommand(this));
        commandManager.registerCommand("debug", new DebugCommand(this));
        commandManager.registerCommand("keys", new KeysCommand(this));
        commandManager.registerCommand("argvs", new ArgvsCommand(this));
    }

    public void start() {
        try {
            redisProvider.connect();
            logger.log(Level.INFO, "Successfully connected to redis.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to connect to redis.", e);
            return;
        }
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
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void setupTerminal() {
        try {
            terminal = TerminalBuilder.builder()
                    .system(true)
                    .dumb(true)
                    .build();
            // Parser damit Backslashes z. B. für pfade nicht entfernt werden.
            DefaultParser parser = new DefaultParser();
            parser.setEscapeChars(null);
            lineReader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .parser(parser)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupLogger() {
        logger = Logger.getLogger("net.proxysocke.redisluna");
        Handler handler = new Handler() {
            @Override
            public void publish(LogRecord record) {
                String message = String.format("[%s]: %s", record.getLevel(), record.getMessage());
                terminal.writer().println(message);
                Throwable thrown = record.getThrown();
                if (thrown != null) {
                    thrown.printStackTrace(terminal.writer());
                }
                terminal.flush();
            }

            @Override
            public void flush() {}

            @Override
            public void close() {}
        };
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
    }

    private void setupRedisProvider(){
        RedisCredentials credentials = redisConfig.getCredentials();
        redisProvider = new RedisProvider(credentials);
        pubSubHandler = new PubSubHandler(this);
        pubSubHandler.start();
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public LineReader getLineReader() {
        return lineReader;
    }

    public Logger getLogger() {
        return logger;
    }

    public RedisProvider getRedisProvider() {
        return redisProvider;
    }

    public PubSubHandler getPubSubHandler() {
        return pubSubHandler;
    }

    public ScriptSessionManager getScriptSessionManager() {
        return scriptSessionManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }
}