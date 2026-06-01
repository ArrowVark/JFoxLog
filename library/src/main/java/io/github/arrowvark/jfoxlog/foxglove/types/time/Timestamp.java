package io.github.arrowvark.jfoxlog.foxglove.types.time;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;

public class Timestamp implements FoxgloveLoggable {

    public long sec;
    public long nsec;

    public Timestamp(long sec, long nsec) {
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

    public Timestamp mutate(Optional<Long> sec, Optional<Long> nsec) {
        sec.ifPresent(aLong -> this.sec = aLong);
        nsec.ifPresent(aLong -> this.nsec = aLong);
        return this;
    }

    public Timestamp mutateNow() {
        Instant now = Instant.now();
        long sec = now.getEpochSecond();
        long nsec = now.getNano();

        return this.mutate(Optional.of(sec), Optional.of(nsec));
    }

    public static Timestamp now() {
        Instant now = Instant.now();
        long sec = now.getEpochSecond();
        long nsec = now.getNano();

        return new Timestamp(sec, nsec);
    }

    public static Supplier<Timestamp> nowSupplier() {
        Instant now = Instant.now();
        long sec = now.getEpochSecond();
        long nsec = now.getNano();

        Timestamp timestamp = new Timestamp(sec, nsec);
        return () -> timestamp.mutate(Optional.of(sec), Optional.of(nsec));
    }
}
