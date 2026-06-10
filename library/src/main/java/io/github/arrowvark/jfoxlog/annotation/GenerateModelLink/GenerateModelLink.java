package io.github.arrowvark.jfoxlog.annotation.GenerateModelLink;

import io.github.arrowvark.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;

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
