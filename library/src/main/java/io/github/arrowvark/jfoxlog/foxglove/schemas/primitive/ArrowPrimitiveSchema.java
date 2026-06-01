package io.github.arrowvark.jfoxlog.foxglove.schemas.primitive;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.arrowvark.jfoxlog.foxglove.schemas.geometry.pose.PoseSchema;
import io.github.arrowvark.jfoxlog.foxglove.schemas.misc.ColorSchema;

import java.util.Optional;

public class ArrowPrimitiveSchema implements FoxgloveLoggable {

    public PoseSchema pose;
    public double shaft_length;
    public double shaft_diameter;
    public double head_length;
    public double head_diameter;
    public ColorSchema color;

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

    public ArrowPrimitiveSchema mutate(
            Optional<PoseSchema> pose,
            Optional<Double> shaft_length,
            Optional<Double> shaft_diameter,
            Optional<Double> head_length,
            Optional<Double> head_diameter,
            Optional<ColorSchema> color
    ) {
        pose.ifPresent(aPose -> this.pose = aPose);
        shaft_length.ifPresent(aDouble -> this.shaft_length = aDouble);
        shaft_diameter.ifPresent(aDouble -> this.shaft_diameter = aDouble);
        head_length.ifPresent(aDouble -> this.head_length = aDouble);
        head_diameter.ifPresent(aDouble -> this.head_diameter = aDouble);
        color.ifPresent(aColor -> this.color = aColor);

        return this;
    }
}
