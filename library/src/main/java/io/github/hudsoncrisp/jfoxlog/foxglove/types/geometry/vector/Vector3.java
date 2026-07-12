package io.github.hudsoncrisp.jfoxlog.foxglove.types.geometry.vector;

import io.github.hudsoncrisp.jfoxlog.foxglove.FoxgloveLoggable;

import java.util.Optional;

public class Vector3 implements FoxgloveLoggable {

    public double x;
    public double y;
    public double z;

    public static Vector3 kZero() {
        return new Vector3(0, 0, 0);
    }
    public static Vector3 kUnit() {
        return new Vector3(1, 1, 1);
    }


    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public Vector3 mutate(Optional<Double> x, Optional<Double> y, Optional<Double> z) {
        x.ifPresent(aDouble -> this.x = aDouble);
        y.ifPresent(aDouble -> this.y = aDouble);
        z.ifPresent(aDouble -> this.z = aDouble);
        return this;
    }
}
