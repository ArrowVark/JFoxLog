package io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket;

import io.github.hudsoncrisp.jfoxlog.debug.FoxgloveDebugLogSeverity;
import io.github.hudsoncrisp.jfoxlog.debug.FoxgloveDebugPanel;
import io.github.hudsoncrisp.jfoxlog.foxglove.FoxgloveLoggable;

import java.util.Date;

public class FoxgloveChannel<T extends FoxgloveLoggable> {

    public enum LoggingType {
        SERVER_DRIVEN,
        USER_DRIVEN,
        LOW_FREQUENCY_SERVER_DRIVEN,
        LOW_FREQUENCY_SERVER_DRIVEN_STATIC
    }

    // Loggable
    public T getLoggable() {
        return loggable;
    }

    // Id
    public int getId() {
        return id;
    }

    // Topic
    public String getTopic() {
        return topic;
    }


    // Logging Type
    public LoggingType getLoggingType() {
        return loggingType;
    }

    public FoxgloveChannel<T> setLoggingType(LoggingType loggingType) {
        this.loggingType = loggingType;
        return this;
    }

    // Cache

    public String getCacheWithoutUpdate() {
        return cache == null ? "" : cache;
    }

    // Fetch
    public Runnable getFetchMethod() {
        return fetch;
    }

    public FoxgloveChannel<T> setFetchMethod(Runnable fetchMethod) {
        this.fetch = fetchMethod;
        return this;
    }

    // Active
    public boolean isActive() {
        return active;
    }

    public FoxgloveChannel<T> start() {
        active = true;
        return this;
    }

    public FoxgloveChannel<T> stop() {
        active = false;
        return this;
    }

    // Last Message Date
    public Date getLastDataAccessDate() {
        return lastDataAccessDate;
    }

    public Date getLastCacheAccessDate() {
        return lastCacheAccessDate;
    }

    public FoxgloveChannel<T> logCacheDataAccessDates(boolean value) {
        logAccessDate = value;
        if (value) {
            FoxgloveDebugPanel.log("\"" + topic + "\" has been set to log cache and data access dates", FoxgloveDebugLogSeverity.INFO);
        } else {
            FoxgloveDebugPanel.log("\"" + topic + "\" has been set to not log cache and data access dates", FoxgloveDebugLogSeverity.INFO);
        }
        return this;
    }

    // Vars
    private final T loggable;
    private final int id;
    private final String topic;
    private LoggingType loggingType;
    private String cache;
    private Runnable fetch;
    private boolean active = true;
    private Date lastDataAccessDate;
    private Date lastCacheAccessDate;
    private boolean logAccessDate;

    public FoxgloveChannel(int id, String topic, LoggingType loggingType, T loggable) {
        this.id = id;
        this.topic = topic;
        this.loggingType = loggingType;
        this.loggable = loggable;

        FoxgloveDebugPanel.addChannelDebug(topic, this);
    }

    public String requestData() {
        if (logAccessDate) lastDataAccessDate = new Date();
        if (fetch != null) {
            fetch.run(); // Fetch most up-to-date data
        }
        String data = loggable.toJson(); // Serialize
        cache = data; // Set cache to most up-to-date data
        return data; // Return serialized data
    }

    public String requestCache() {
        if (logAccessDate) lastCacheAccessDate = new Date();
        if (cache == null) { // Check if the cache exists
            return requestData(); // Request data normally if it does not exist
        }

        return cache; // Return the cached data
    }

    public void free() {
        FoxgloveWebSocketServer.freeChannel(this);
    }
}
