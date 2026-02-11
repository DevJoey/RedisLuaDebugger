package net.proxysocke.redisluna.commands.session;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ScriptSessionManager {

    private final Map<String, ScriptSession> sessions = new HashMap<>();

    public SessionCreateResult createSession(String sessionName, String scriptName) {
        String sessionNameLower = sessionName.toLowerCase();
        if(sessions.containsKey(sessionNameLower)){
            return SessionCreateResult.ALREADY_EXISTS;
        }
        Path scriptFilePath = Path.of("scripts/", scriptName + ".lua");
        if(Files.notExists(scriptFilePath)){
            return SessionCreateResult.SCRIPT_NOT_FOUND;
        }
        ScriptSession session = new ScriptSession(scriptFilePath);
        sessions.put(sessionNameLower, session);
        return SessionCreateResult.CREATED;
    }

    public enum SessionCreateResult {
        CREATED,
        ALREADY_EXISTS,
        SCRIPT_NOT_FOUND
    }
}
