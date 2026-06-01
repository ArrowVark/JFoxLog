package io.github.arrowvark.jfoxlog.foxglove.types.scene.sceneEntity;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.arrowvark.jfoxlog.foxglove.types.Util;
import io.github.arrowvark.jfoxlog.foxglove.types.misc.KeyValuePair;
import io.github.arrowvark.jfoxlog.foxglove.types.primitive.*;
import io.github.arrowvark.jfoxlog.foxglove.types.time.Duration;
import io.github.arrowvark.jfoxlog.foxglove.types.time.Timestamp;

public class SceneEntity implements FoxgloveLoggable {

    public Timestamp timestamp;
    public String frame_id;
    public String id;
    public Duration lifetime;
    public boolean frame_locked;
    public KeyValuePair[] metadata;
    public ArrowPrimitive[] arrows;
    public CubePrimitive[] cubes;
    public SpherePrimitive[] spheres;
    public CylinderPrimitive[] cylinders;
    public LinePrimitive[] lines;
    public TriangleListPrimitive[] triangles;
    public TextPrimitive[] texts;
    public ModelPrimitive[] models;

    public SceneEntity(
            Timestamp timestamp,
            String frame_id,
            String id,
            Duration lifetime,
            boolean frame_locked,
            KeyValuePair[] metadata,
            ArrowPrimitive[] arrows,
            CubePrimitive[] cubes,
            SpherePrimitive[] spheres,
            CylinderPrimitive[] cylinders,
            LinePrimitive[] lines,
            TriangleListPrimitive[] triangles,
            TextPrimitive[] texts,
            ModelPrimitive[] models
    ) {
        this.timestamp = timestamp;
        this.frame_id = frame_id;
        this.id = id;
        this.lifetime = lifetime;
        this.frame_locked = frame_locked;
        this.metadata = metadata;
        this.arrows = arrows;
        this.cubes = cubes;
        this.spheres = spheres;
        this.cylinders = cylinders;
        this.lines = lines;
        this.triangles = triangles;
        this.texts = texts;
        this.models = models;
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
