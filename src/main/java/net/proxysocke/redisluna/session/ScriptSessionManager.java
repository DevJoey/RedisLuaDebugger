package net.proxysocke.redisluna.session;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class ScriptSessionManager {

    private final Map<String, ScriptSession> sessions = new HashMap<>();

    private ScriptSession lastSession;

    public SessionCreateResult createSession(String sessionName, String pathInput) {
        String sessionNameLower = sessionName.toLowerCase();
        if (sessions.containsKey(sessionNameLower)) {
            return SessionCreateResult.ALREADY_EXISTS;
        }
        Path scriptPath = Path.of(pathInput);
        if(scriptPath.getNameCount() == 1){
            scriptPath = Path.of("scripts/", pathInput + ".lua");
        }
        if (Files.notExists(scriptPath)) {
            return SessionCreateResult.SCRIPT_NOT_FOUND;
        }
        ScriptSession session = new ScriptSession(scriptPath);
        sessions.put(sessionNameLower, session);
        return SessionCreateResult.CREATED;
    }

    public void setLastSession(ScriptSession lastSession) {
        this.lastSession = lastSession;
    }

    public ScriptSession getSession(String sessionName) {
        return sessions.get(sessionName.toLowerCase());
    }

    public ScriptSession getLastSession() {
        return lastSession;
    }

    public ScriptSession removeSession(String sessionName){
        return sessions.remove(sessionName.toLowerCase());
    }

    public void dropSessions(){
        sessions.clear();
        lastSession = null;
    }

    public enum SessionCreateResult {
        CREATED,
        ALREADY_EXISTS,
        SCRIPT_NOT_FOUND
    }
}
