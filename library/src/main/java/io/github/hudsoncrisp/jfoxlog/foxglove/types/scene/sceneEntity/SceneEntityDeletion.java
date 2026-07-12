package io.github.hudsoncrisp.jfoxlog.foxglove.types.scene.sceneEntity;

import io.github.hudsoncrisp.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.hudsoncrisp.jfoxlog.foxglove.enums.SceneEntityDeletionType;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.time.Timestamp;
import io.github.hudsoncrisp.jfoxlog.foxglove.util.DynamicFoxgloveLoggable;
import io.github.hudsoncrisp.jfoxlog.foxglove.util.FoxgloveLoggableBuilder;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SceneEntityDeletion implements FoxgloveLoggable {

    public Timestamp timestamp;
    public SceneEntityDeletionType type;
    public String id;

    public SceneEntityDeletion(
            Timestamp timestamp,
            SceneEntityDeletionType type,
            String id
    ) {
        this.timestamp = timestamp;
        this.type = type;
        this.id = id;
    }

    public SceneEntityDeletion() {}

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

    public static class Builder<Ctx> extends FoxgloveLoggableBuilder<SceneEntityDeletion, Builder<Ctx>> {
        private final Ctx ctx;
        private Supplier<Timestamp> timestamp;
        private Supplier<SceneEntityDeletionType> type;
        private Supplier<String> id;

        public Builder(Ctx ctx) {
            super(new SceneEntityDeletion());
            this.ctx = ctx;
        }

        public Builder<Ctx> timestamp(Timestamp timestamp) {
            this.timestamp = () -> timestamp;
            bind(t -> instance.timestamp = t, timestamp);
            return this;
        }

        public Builder<Ctx> timestamp(Supplier<Timestamp> timestampSupplier) {
            this.timestamp = timestampSupplier;
            bind((Consumer<Timestamp>) t -> instance.timestamp = t, timestamp);
            return this;
        }

        public Builder<Ctx> type(SceneEntityDeletionType type) {
            this.type = () -> type;
            bind(t -> instance.type = t, type);
            return this;
        }

        public Builder<Ctx> type(Supplier<SceneEntityDeletionType> typeSupplier) {
            this.type = typeSupplier;
            bind((Consumer<SceneEntityDeletionType>) t -> instance.type = t, typeSupplier);
            return this;
        }

        public Builder<Ctx> id(String id) {
            this.id = () -> id;
            bind(t -> instance.id = t, id);
            return this;
        }

        public Builder<Ctx> id(Supplier<String> idSupplier) {
            this.id = idSupplier;
            bind((Consumer<String>) t -> instance.id = t, idSupplier);
            return this;
        }

        public Ctx done() {
            return ctx;
        }

        public SceneEntityDeletion finalizeBuildStatically() {
            return new SceneEntityDeletion(
                    timestamp.get(),
                    type.get(),
                    id.get()
            );
        }

        public DynamicFoxgloveLoggable<SceneEntityDeletion> finalizeBuildDynamically() {
            return null;
        }

        @Override
        protected SceneEntityDeletion constructLoggable() {
            return null;
        }

        @Override
        protected DynamicFoxgloveLoggable<SceneEntityDeletion> constructDynamicLoggable() {
            return null;
        }
    }
}
