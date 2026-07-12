package io.github.hudsoncrisp.jfoxlog.foxglove.types.scene;

import io.github.hudsoncrisp.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.hudsoncrisp.jfoxlog.foxglove.schemas.scene.GridSchema;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.Util;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.geometry.pose.Pose;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.geometry.vector.Vector2;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.time.Timestamp;

public class Grid implements FoxgloveLoggable {

    public Timestamp timestamp;
    public String frame_id;
    public Pose pose;
    public int column_count;
    public Vector2 cell_size;
    public int row_stride;
    public int cell_stride;
    public PackedElementField[] fields;
    public byte[] data;

    @Override
    public String getSchema() {
        return GridSchema.SCHEMA;
    }

    @Override
    public String getSchemaName() {
        return GridSchema.NAME;
    }

    @Override
    public String toJson() {
        return Util.GSON.toJson(this);
    }
}
