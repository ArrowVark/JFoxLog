package io.github.hudsoncrisp.jfoxlog.foxglove.schemas.time;

import io.github.hudsoncrisp.jfoxlog.foxglove.FoxgloveLoggable;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;

public class TimestampSchema implements FoxgloveLoggable {

    public long sec;
    public long nsec;

    public TimestampSchema(long sec, long nsec) {
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

    public TimestampSchema mutate(Optional<Long> sec, Optional<Long> nsec) {
        sec.ifPresent(aLong -> this.sec = aLong);
        nsec.ifPresent(aLong -> this.nsec = aLong);
        return this;
    }

    public TimestampSchema mutateNow() {
        Instant now = Instant.now();
        long sec = now.getEpochSecond();
        long nsec = now.getNano();

        return this.mutate(Optional.of(sec), Optional.of(nsec));
    }

    public static TimestampSchema now() {
        Instant now = Instant.now();
        long sec = now.getEpochSecond();
        long nsec = now.getNano();

        return new TimestampSchema(sec, nsec);
    }

    public static Supplier<TimestampSchema> nowSupplier() {
        Instant now = Instant.now();
        long sec = now.getEpochSecond();
        long nsec = now.getNano();

        TimestampSchema timestamp = new TimestampSchema(sec, nsec);
        return () -> timestamp.mutate(Optional.of(sec), Optional.of(nsec));
    }
}
