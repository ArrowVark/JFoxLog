package io.github.arrowvark.jfoxlog.foxglove.types.primitive;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.arrowvark.jfoxlog.foxglove.types.geometry.pose.Pose;
import io.github.arrowvark.jfoxlog.foxglove.types.misc.Color;
import io.github.arrowvark.jfoxlog.foxglove.types.scene.sceneEntity.SceneEntity;

import java.util.Optional;

public class ArrowPrimitive implements FoxgloveLoggable {

    public Pose pose;
    public double shaft_length;
    public double shaft_diameter;
    public double head_length;
    public double head_diameter;
    public Color color;

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

    public ArrowPrimitive mutate(
            Optional<Pose> pose,
            Optional<Double> shaft_length,
            Optional<Double> shaft_diameter,
            Optional<Double> head_length,
            Optional<Double> head_diameter,
            Optional<Color> color
    ) {
        pose.ifPresent(aPose -> this.pose = aPose);
        shaft_length.ifPresent(aDouble -> this.shaft_length = aDouble);
        shaft_diameter.ifPresent(aDouble -> this.shaft_diameter = aDouble);
        head_length.ifPresent(aDouble -> this.head_length = aDouble);
        head_diameter.ifPresent(aDouble -> this.head_diameter = aDouble);
        color.ifPresent(aColor -> this.color = aColor);

        return this;
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
