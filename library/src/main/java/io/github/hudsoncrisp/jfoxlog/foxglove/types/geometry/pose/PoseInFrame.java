package io.github.hudsoncrisp.jfoxlog.foxglove.types.geometry.pose;

import io.github.hudsoncrisp.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.hudsoncrisp.jfoxlog.foxglove.schemas.geometry.pose.PoseInFrameSchema;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.Util;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.time.Timestamp;

import java.util.Optional;

public class PoseInFrame implements FoxgloveLoggable {

    public Timestamp timestamp;
    public String frame_id;
    public Pose pose;

    @Override
    public String getSchema() {
        return PoseInFrameSchema.SCHEMA;
    }

    @Override
    public String getSchemaName() {
        return PoseInFrameSchema.NAME;
    }

    @Override
    public String toJson() {
        return Util.GSON.toJson(this);
    }

    public PoseInFrame mutate(Optional<Timestamp> timestamp, Optional<String> frame_id, Optional<Pose> pose) {
        timestamp.ifPresent(aTimestamp -> this.timestamp = aTimestamp);
        frame_id.ifPresent(aString -> this.frame_id = aString);
        pose.ifPresent(aPose -> this.pose = aPose);

        return this;
    }
}
