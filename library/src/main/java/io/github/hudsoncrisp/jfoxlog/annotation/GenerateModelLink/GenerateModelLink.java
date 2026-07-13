package io.github.hudsoncrisp.jfoxlog.annotation.GenerateModelLink;

import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;
import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveLoggingFrequencyInfo;

public @interface GenerateModelLink {
    /**
     * The location of the model in {@code src/main/resources/}.
     */
    String resourcePath();

    /**
     * The logging type which
     */
//    FoxgloveLoggingFrequencyInfo loggingFrequencyInfo();
}
