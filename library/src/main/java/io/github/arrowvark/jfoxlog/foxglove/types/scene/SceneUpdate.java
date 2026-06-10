package io.github.arrowvark.jfoxlog.foxglove.types.scene;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.arrowvark.jfoxlog.foxglove.schemas.scene.SceneUpdateSchema;
import io.github.arrowvark.jfoxlog.foxglove.types.Util;
import io.github.arrowvark.jfoxlog.foxglove.types.scene.sceneEntity.SceneEntity;
import io.github.arrowvark.jfoxlog.foxglove.types.scene.sceneEntity.SceneEntityDeletion;
import io.github.arrowvark.jfoxlog.foxglove.util.DynamicFoxgloveLoggable;
import io.github.arrowvark.jfoxlog.foxglove.util.FoxgloveLoggableBuilder;
import io.github.arrowvark.jfoxlog.foxglove.util.UnfinishedBuilderComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SceneUpdate implements FoxgloveLoggable {

    public SceneEntityDeletion[] deletions;
    public SceneEntity[] entities;

    public SceneUpdate(SceneEntityDeletion[] deletions, SceneEntity[] entities) {
        this.deletions = deletions;
        this.entities = entities;
    }

    public SceneUpdate() {}

    @Override
    public String getSchema() {
        return SceneUpdateSchema.SCHEMA;
    }

    @Override
    public String getSchemaName() {
        return SceneUpdateSchema.NAME;
    }

    @Override
    public String toJson() {
        return Util.GSON.toJson(this);
    }

    public static class Builder extends FoxgloveLoggableBuilder<SceneUpdate, Builder> {
        private final List<UnfinishedBuilderComponent<SceneEntity, SceneEntity.Builder<Builder>>> entities = new ArrayList<>();
        private final List<UnfinishedBuilderComponent<SceneEntityDeletion, SceneEntityDeletion.Builder<Builder>>> deletions = new ArrayList<>();

        public Builder() {
            super(new SceneUpdate());
        }

        public SceneEntity.Builder<Builder> entity() {
            SceneEntity.Builder<Builder> builder = new SceneEntity.Builder<>(this);
            entities.add(new UnfinishedBuilderComponent<>(builder));
            return builder;
        }

        public Builder entity(SceneEntity entity) {
            entities.add(new UnfinishedBuilderComponent<>(entity));
            return this;
        }

        public Builder entity(Supplier<SceneEntity> entitySupplier) {
            entities.add(new UnfinishedBuilderComponent<>(new DynamicFoxgloveLoggable<>(entitySupplier)));
            return this;
        }

        public SceneEntityDeletion.Builder<Builder> deletion() {
            SceneEntityDeletion.Builder<Builder> builder = new SceneEntityDeletion.Builder<>(this);
            deletions.add(new UnfinishedBuilderComponent<>(builder));
            return builder;
        }

        public Builder deletion(SceneEntityDeletion deletion) {
            deletions.add(new UnfinishedBuilderComponent<>(deletion));
            return this;
        }

        public Builder deletion(Supplier<SceneEntityDeletion> deletionSupplier) {
            deletions.add(new UnfinishedBuilderComponent<>(new DynamicFoxgloveLoggable<>(deletionSupplier)));
            return this;
        }

        @Override
        protected SceneUpdate constructLoggable() {
            instance.entities = (SceneEntity[]) entities.stream().map(UnfinishedBuilderComponent::finish).toArray();
            instance.deletions = (SceneEntityDeletion[]) deletions.stream().map(UnfinishedBuilderComponent::finish).toArray();
            return instance;
        }

        @Override
        protected DynamicFoxgloveLoggable<SceneUpdate> constructDynamicLoggable() {
            List<Runnable> bindings = getBindings();
            entities.forEach((entity) ->
                 entity.collectBindings(bindings)
            );
            deletions.forEach((deletion) ->
                    deletion.collectBindings(bindings)
            );
            return new DynamicFoxgloveLoggable<>(instance, bindings);
        }
    }
}
