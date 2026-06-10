package io.github.arrowvark.jfoxlog.foxglove.types.primitive;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.arrowvark.jfoxlog.foxglove.types.Util;
import io.github.arrowvark.jfoxlog.foxglove.types.geometry.pose.Pose;
import io.github.arrowvark.jfoxlog.foxglove.types.geometry.vector.Vector3;
import io.github.arrowvark.jfoxlog.foxglove.types.misc.Color;
import io.github.arrowvark.jfoxlog.foxglove.types.scene.sceneEntity.SceneEntity;
import io.github.arrowvark.jfoxlog.foxglove.util.DynamicValue;

import java.util.function.Supplier;

public class ModelPrimitive implements FoxgloveLoggable {

    public Pose pose;
    public Vector3 scale;
    public Color color;
    public boolean override_color;
    public String url;
    public String media_type;
    public String data;

    public ModelPrimitive(
            Pose pose,
            Vector3 scale,
            Color color,
            boolean override_color,
            String url,
            String media_type,
            String data
    ) {
        this.pose = pose;
        this.scale = scale;
        this.color = color;
        this.override_color = override_color;
        this.url = url;
        this.media_type = media_type;
        this.data = data;
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
        return Util.GSON.toJson(this);
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
