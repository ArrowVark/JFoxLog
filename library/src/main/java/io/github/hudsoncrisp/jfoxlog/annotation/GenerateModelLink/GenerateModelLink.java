package io.github.hudsoncrisp.jfoxlog.annotation.GenerateModelLink;

import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;

public @interface GenerateModelLink {
    /**
     * The location of the model in {@code src/main/resources/}.
     */
    String resourcePath();

    /**
     * The logging type which
     */
    FoxgloveChannel.LoggingType loggingType() default FoxgloveChannel.LoggingType.LOW_FREQUENCY_SERVER_DRIVEN_STATIC;
}
