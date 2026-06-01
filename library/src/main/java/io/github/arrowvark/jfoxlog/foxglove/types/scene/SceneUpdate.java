package io.github.arrowvark.jfoxlog.foxglove.types.scene;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.arrowvark.jfoxlog.foxglove.schemas.scene.SceneUpdateSchema;
import io.github.arrowvark.jfoxlog.foxglove.types.Util;
import io.github.arrowvark.jfoxlog.foxglove.types.scene.sceneEntity.SceneEntity;
import io.github.arrowvark.jfoxlog.foxglove.types.scene.sceneEntity.SceneEntityDeletion;

public class SceneUpdate implements FoxgloveLoggable {

    public SceneEntityDeletion[] deletions;
    public SceneEntity[] entities;

    public SceneUpdate(SceneEntityDeletion[] deletions, SceneEntity[] entities) {
        this.deletions = deletions;
        this.entities = entities;
    }

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
}
