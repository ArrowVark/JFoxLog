package io.github.arrowvark.jfoxlog.foxglove.types.misc;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.arrowvark.jfoxlog.foxglove.util.DynamicFoxgloveLoggable;
import io.github.arrowvark.jfoxlog.foxglove.util.FoxgloveLoggableBuilder;

import java.util.function.Supplier;

public class KeyValuePair implements FoxgloveLoggable {
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

    public static class Builder<Ctx> extends FoxgloveLoggableBuilder<KeyValuePair, KeyValuePair.Builder<Ctx>> {
        private final Ctx ctx;

        public Builder(Ctx ctx) {
            super(new KeyValuePair());
            this.ctx = ctx;
        }

        public Ctx done() {
            return ctx;
        }

        @Override
        protected KeyValuePair constructLoggable() {
            return null;
        }

        @Override
        protected DynamicFoxgloveLoggable<KeyValuePair> constructDynamicLoggable() {
            return null;
        }
    }
}
