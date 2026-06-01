package io.github.arrowvark.jfoxlog.foxglove.types.scene.sceneEntity;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.arrowvark.jfoxlog.foxglove.enums.SceneEntityDeletionType;
import io.github.arrowvark.jfoxlog.foxglove.types.time.Timestamp;

public class SceneEntityDeletion implements FoxgloveLoggable {

    public Timestamp timestamp;
    public SceneEntityDeletionType type;
    public String id;

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
}
