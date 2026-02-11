package net.proxysocke.redisluna.session;

import java.nio.file.Path;

public final class ScriptSession {

    private final Path scriptFilePath;

    private String[] keys;
    private String[] argvs;

    public ScriptSession(Path scriptFilePath) {
        this.scriptFilePath = scriptFilePath;
    }

    public Path getScriptFilePath() {
        return scriptFilePath;
    }

    public void setKeys(String... keys){
        this.keys = keys;
    }

    public void setArgvs(String... argvs){
        this.argvs = argvs;
    }

    public String[] getKeys() {
        return keys;
    }

    public String[] getArgvs() {
        return argvs;
    }
}