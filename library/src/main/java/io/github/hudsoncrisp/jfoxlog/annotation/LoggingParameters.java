package io.github.hudsoncrisp.jfoxlog.annotation;

import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;
import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveLoggingFrequencyInfo;

public @interface LoggingParameters {
    String name() default "";
    double millisecondLead() default 0;
    FoxgloveLoggingFrequencyInfo.DataRetrieveType dataRetrieveType() default FoxgloveLoggingFrequencyInfo.DataRetrieveType.NEW_DATA;
}
