package io.github.arrowvark.jfoxlog.foxglove;

import io.github.arrowvark.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;
import io.github.arrowvark.jfoxlog.foxglove.servers.websocket.FoxgloveWebSocketServer;
import io.github.arrowvark.jfoxlog.foxglove.types.time.Duration;
import io.github.arrowvark.jfoxlog.foxglove.util.DynamicFoxgloveLoggable;

public interface FoxgloveLoggable {
    /**
     * Gets the schema representing the loggable object
     *
     * @return The schema as a String
     */
    String getSchema();

    /**
     * Gets the schema name
     *
     * @return The schema name as a String
     */
    String getSchemaName();

    /**
     * Gets the current JSON representation of the loggable object
     *
     * @return The current JSON as a String
     */
    String toJson();

    /**
     * Sets up all child objects of the loggable object. Does nothing by default and for native Foxglove types.
     * You likely will not want to use this alone, as it does not set up the parent object. {@code setupLogging}
     * automatically sets up both the parent object and all child objects.
     *
     * @param parentTopic The topic of the parent object
     */
    default void setupChildObjects(String parentTopic) {}

    /**
     * Automatically logs this loggable object on a new channel. Automatically updates over time.
     *
     * @param topic The topic which the loggable object should be logged to (ex. "Robot/Subsystems/Swerve")
     * @param loggingType How the channel should handle logging the object
     * @return This object
     */
    default FoxgloveLoggable setupLogging(String topic, FoxgloveChannel.LoggingType loggingType) {
        setupChildObjects(topic);
        FoxgloveWebSocketServer.requestNewChannel(topic, loggingType, this);
        return this;
    }

    /**
     * Automatically logs this loggable object on a new channel. Automatically updates over time.
     * When the logging type is not specified it defaults to {@code LoggingType.SERVER_DRIVEN}.
     *
     * @param topic The topic which the loggable object should be logged to (ex. "Robot/Subsystems/Swerve")
     * @return This object
     */
    default FoxgloveLoggable setupLogging(String topic) {
        return setupLogging(topic, FoxgloveChannel.LoggingType.SERVER_DRIVEN);
    }

    default DynamicFoxgloveLoggable<? extends FoxgloveLoggable> makeDynamic() {
        return new DynamicFoxgloveLoggable<>(this);
    }
}
