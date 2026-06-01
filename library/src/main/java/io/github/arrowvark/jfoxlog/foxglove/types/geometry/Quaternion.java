package io.github.arrowvark.jfoxlog.foxglove.types.geometry;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;

import java.util.Optional;

public class Quaternion implements FoxgloveLoggable {

    public double x;
    public double y;
    public double z;
    public double w;

    public static Quaternion kIdentity() {
        return new Quaternion(0, 0, 0, 1);
    }

    public Quaternion(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
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

    public Quaternion mutate(Optional<Double> x, Optional<Double> y, Optional<Double> z, Optional<Double> w) {
        x.ifPresent(aDouble -> this.x = aDouble);
        y.ifPresent(aDouble -> this.y = aDouble);
        z.ifPresent(aDouble -> this.z = aDouble);
        w.ifPresent(aDouble -> this.w = aDouble);
        return this;
    }
}
