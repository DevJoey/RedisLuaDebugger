# RedisLuna (RedisLuaDebugger)

RedisLuna ist ein Java-Terminalprogramm zum Testen und Ausführen von 
Lua-Skripten auf deinem Redis-Server via des `EVAL`-Befehls.  
Es ermöglicht das Verwalten von Sessions, in denen Lua-Skripte zusammen 
mit definierten `KEYS` und `ARGV`-Parametern gespeichert und wiederverwendet 
werden können.

Du kannst nun Lua-Skripte für Redis schnell, reproduzierbar und 
komfortabel testen.

---

## Features

- Erstellung und Verwaltung von temporären Sessions
- Definition von `KEYS` und `ARGV` pro Session
- Platzhalter-Unterstützung in `KEYS` und `ARGV`
- Löschen von Sessions
- Terminalbasierte Bedienung (kein GUI)

---

## Funktionsweise

RedisLuna sendet das in einer Session gespeicherte Lua-Skript 
mittels des Redis-Befehls: `EVAL`

## Platzhalter

In `KEYS` und `ARGV` können dynamische Platzhalter verwendet werden. Diese werden vor dem Absenden an Redis ersetzt.

### Unterstützte Platzhalter

|Platzhalter|Beschreibung|
|--------|------------|
|`%uuid%`|Generiert eine zufällige UUID|
|`%millis%`|Aktuelle Zeit in Millisekunden (Unix Epoch)|

### Konfiguration
Die Konfiguration erfolgt über die Datei `redis.properties` welche sich im selben
Verzeichnis befinden muss, wie die `.jar`

**Beispielkonfiguration:**
```properties
host=127.0.0.1
port=6379
database=0
username=example_user
password=example_password
```

## Build
> Ich ergänze die Build-Informationen noch.