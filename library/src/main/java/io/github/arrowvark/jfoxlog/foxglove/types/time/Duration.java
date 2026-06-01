package io.github.arrowvark.jfoxlog.foxglove.types.time;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;

import java.util.Optional;

public class Duration implements FoxgloveLoggable {

    public long sec;
    public long nsec;

    public static Duration kZero() {
        return new Duration(0, 0);
    }

    public Duration(long sec, long nsec) {
        this.sec = sec;
        this.nsec = nsec;
    }

    @Override
    public String getSchema() {
        return "";
    }

    @Override
    public String getSchemaName() {
        return "";
    }

    @Override
    public String toJson() {
        return "";
    }

    public Duration mutate(Optional<Long> sec, Optional<Long> nsec) {
        sec.ifPresent(aLong -> this.sec = aLong);
        nsec.ifPresent(aLong -> this.nsec = aLong);
        return this;
    }
}
