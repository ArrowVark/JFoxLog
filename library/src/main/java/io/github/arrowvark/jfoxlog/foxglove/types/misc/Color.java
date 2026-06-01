package io.github.arrowvark.jfoxlog.foxglove.types.misc;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.arrowvark.jfoxlog.foxglove.types.Util;

public class Color implements FoxgloveLoggable {

    public double r;
    public double g;
    public double b;
    public double a;

    public static final Color kRed = new Color(1, 0, 0, 1);
    public static final Color kGreen = new Color(0, 1, 0, 1);
    public static final Color kBlue = new Color(0, 0, 1, 1);
    public static final Color kBlack = new Color(0, 0, 0, 1);
    public static final Color kWhite = new Color(1, 1, 1, 1);

    public Color(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
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
}
