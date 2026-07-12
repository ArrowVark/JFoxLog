package io.github.hudsoncrisp.jfoxlog.annotation;

import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;

public @interface LoggingParameters {
    String name() default "";
    FoxgloveChannel.LoggingType loggingType() default FoxgloveChannel.LoggingType.SERVER_DRIVEN;
}
