package io.github.hudsoncrisp.jfoxlog.foxglove.types.time;

import io.github.hudsoncrisp.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.hudsoncrisp.jfoxlog.foxglove.util.DynamicFoxgloveLoggable;
import io.github.hudsoncrisp.jfoxlog.foxglove.util.FoxgloveLoggableBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Timestamp implements FoxgloveLoggable {

    public long sec;
    public long nsec;

    public Timestamp(long sec, long nsec) {
        this.sec = sec;
        this.nsec = nsec;
    }

    public static Timestamp defaultObject() {
        return new Timestamp(0, 0);
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
        return () -> {
            Instant nowS = Instant.now();
            long secS = nowS.getEpochSecond();
            long nsecS = nowS.getNano();

            return timestamp.mutate(Optional.of(secS), Optional.of(nsecS));
        };
    }

    public static DynamicFoxgloveLoggable<Timestamp> dynamicNow() {
        Instant now = Instant.now();
        long sec = now.getEpochSecond();
        long nsec = now.getNano();

        Timestamp timestamp = new Timestamp(sec, nsec);
        return new DynamicFoxgloveLoggable<>(timestamp, List.of(
                () -> {
                    Instant nowS = Instant.now();
                    long secS = nowS.getEpochSecond();
                    long nsecS = nowS.getNano();

                    timestamp.mutate(Optional.of(secS), Optional.of(nsecS));
                }
        ));
    }

    public static class Builder<Ctx> extends FoxgloveLoggableBuilder<Timestamp, Timestamp.Builder<Ctx>> {
        private final Ctx ctx;
        private Supplier<Long> sec;
        private Supplier<Long> nsec;

        protected Builder(Ctx ctx) {
            super(Timestamp.defaultObject());
            this.ctx = ctx;
        }

        public Builder sec(long sec) {
            this.sec = () -> sec;
            return this;
        }

        public Builder sec(Supplier<Long> secSupplier) {
            this.sec = secSupplier;
            return this;
        }

        public Builder nsec(long nsec) {
            this.nsec = () -> nsec;
            return this;
        }

        public Builder nsec(Supplier<Long> nsecSupplier) {
            this.nsec = nsecSupplier;
            return this;
        }

        public Ctx done() {
            return ctx;
        }

        @Override
        protected Timestamp constructLoggable() {
            instance.sec = sec.get();
            instance.nsec = nsec.get();
            return instance;
        }

        @Override
        protected DynamicFoxgloveLoggable<Timestamp> constructDynamicLoggable() {
            return new DynamicFoxgloveLoggable<>(instance, List.of(
                    () -> instance.mutate(
                                Optional.of(sec.get()),
                                Optional.of(nsec.get())
                    )
            ));
        }
    }
}
