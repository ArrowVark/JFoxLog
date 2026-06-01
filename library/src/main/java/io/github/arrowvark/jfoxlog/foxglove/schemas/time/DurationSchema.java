package io.github.arrowvark.jfoxlog.foxglove.schemas.time;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;

import java.util.Optional;

public class DurationSchema implements FoxgloveLoggable {

    public long sec;
    public long nsec;

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

    public DurationSchema mutate(Optional<Long> sec, Optional<Long> nsec) {
        sec.ifPresent(aLong -> this.sec = aLong);
        nsec.ifPresent(aLong -> this.nsec = aLong);
        return this;
    }
}
