package io.github.arrowvark.jfoxlog.foxglove.types.scene.frameTransform;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.arrowvark.jfoxlog.foxglove.schemas.scene.frameTransform.FrameTransformSchema;
import io.github.arrowvark.jfoxlog.foxglove.types.Util;
import io.github.arrowvark.jfoxlog.foxglove.types.geometry.Quaternion;
import io.github.arrowvark.jfoxlog.foxglove.types.geometry.vector.Vector3;
import io.github.arrowvark.jfoxlog.foxglove.types.time.Timestamp;

import java.util.Optional;

public class FrameTransform implements FoxgloveLoggable {

    public Timestamp timestamp;
    public String parent_frame_id;
    public String child_frame_id;
    public Vector3 translation;
    public Quaternion rotation;

    public FrameTransform(
            Timestamp timestamp,
            String parent_frame_id,
            String child_frame_id,
            Vector3 translation,
            Quaternion rotation
    ) {
        this.timestamp = timestamp;
        this.parent_frame_id = parent_frame_id;
        this.child_frame_id = child_frame_id;
        this.translation = translation;
        this.rotation = rotation;
    }

    @Override
    public String getSchema() {
        return FrameTransformSchema.SCHEMA;
    }

    @Override
    public String getSchemaName() {
        return FrameTransformSchema.NAME;
    }

    @Override
    public String toJson() {
        return Util.GSON.toJson(this);
    }

    public FrameTransform mutate(Pose2d pose) {
        this.timestamp = Timestamp.now();

        edu.wpi.first.math.geometry.Quaternion wpiQuaternion = new Rotation3d(pose.getRotation()).getQuaternion();
        this.translation.mutate(
                Optional.of(pose.getX()),
                Optional.of(pose.getY()),
                Optional.empty()
        );
        this.rotation.mutate(
                Optional.of(wpiQuaternion.getX()),
                Optional.of(wpiQuaternion.getY()),
                Optional.of(wpiQuaternion.getZ()),
                Optional.of(wpiQuaternion.getW())
        );
        return this;
    }

    public FrameTransform mutate(Pose3d pose) {
        this.timestamp = Timestamp.now();
        
        edu.wpi.first.math.geometry.Quaternion wpiQuaternion = pose.getRotation().getQuaternion();
        this.translation.mutate(
                Optional.of(pose.getX()),
                Optional.of(pose.getY()),
                Optional.of(pose.getZ())
        );
        this.rotation.mutate(
                Optional.of(wpiQuaternion.getX()),
                Optional.of(wpiQuaternion.getY()),
                Optional.of(wpiQuaternion.getZ()),
                Optional.of(wpiQuaternion.getW())
        );
        return this;
    }
}
