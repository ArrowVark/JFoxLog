package io.github.arrowvark.jfoxlog.annotation;

import io.github.arrowvark.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;

public @interface LoggingParameters {
    String name() default "";
    FoxgloveChannel.LoggingType loggingType() default FoxgloveChannel.LoggingType.SERVER_DRIVEN;
}
