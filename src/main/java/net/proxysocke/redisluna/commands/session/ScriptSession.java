package net.proxysocke.redisluna.commands.session;

import java.nio.file.Path;

public class ScriptSession {

    private final Path scriptFilePath;

    public ScriptSession(Path scriptFilePath) {
        this.scriptFilePath = scriptFilePath;
    }
}