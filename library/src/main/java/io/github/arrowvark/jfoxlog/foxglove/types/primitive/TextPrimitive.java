package io.github.arrowvark.jfoxlog.foxglove.types.primitive;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.arrowvark.jfoxlog.foxglove.types.scene.sceneEntity.SceneEntity;

public class TextPrimitive implements FoxgloveLoggable {
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

    public static class Builder<C> {
        private final C ctx;

        public Builder(C ctx) {
            this.ctx = ctx;
        }

        public C done() {
            return ctx;
        }
    }
}
