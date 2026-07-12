package io.github.hudsoncrisp.jfoxlog.foxglove.types.geometry.pose;

import edu.wpi.first.math.geometry.*;
import io.github.hudsoncrisp.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.hudsoncrisp.jfoxlog.foxglove.schemas.geometry.pose.PoseSchema;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.Util;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.geometry.Quaternion;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.geometry.vector.Vector3;

import java.util.Optional;

public class Pose implements FoxgloveLoggable {

    public Vector3 position;
    public Quaternion orientation;

    public static Pose kZero() {
        return new Pose(
                Vector3.kZero(),
                Quaternion.kIdentity()
        );
    }

    public Pose(Vector3 position, Quaternion orientation) {
        init(position, orientation);
    }

    private void init(Vector3 position, Quaternion orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    public Pose(Pose2d pose) {
        Translation2d translation = pose.getTranslation();
        edu.wpi.first.math.geometry.Quaternion wpiQuaternion = new Rotation3d(pose.getRotation()).getQuaternion();
        init(
                new Vector3(translation.getX(), translation.getY(), 0),
                new Quaternion(
                        wpiQuaternion.getX(),
                        wpiQuaternion.getY(),
                        wpiQuaternion.getZ(),
                        wpiQuaternion.getW()
                )
        );
    }

    public Pose(Pose3d pose) {
        Translation3d translation = pose.getTranslation();
        edu.wpi.first.math.geometry.Quaternion wpiQuaternion = pose.getRotation().getQuaternion();
        init(
                new Vector3(translation.getX(), translation.getY(), translation.getZ()),
                new Quaternion(
                        wpiQuaternion.getX(),
                        wpiQuaternion.getY(),
                        wpiQuaternion.getZ(),
                        wpiQuaternion.getW()
                )
        );
    }

    @Override
    public String getSchema() {
        return PoseSchema.SCHEMA;
    }

    @Override
    public String getSchemaName() {
        return PoseSchema.NAME;
    }

    @Override
    public String toJson() {
        return Util.GSON.toJson(this);
    }

    public Pose mutate(Optional<Vector3> position, Optional<Quaternion> orientation) {
        position.ifPresent(aVector3 -> this.position = aVector3);
        orientation.ifPresent(aQuaternion -> this.orientation = aQuaternion);
        return this;
    }

    public Pose mutate(Pose2d pose) {
        Translation2d translation = pose.getTranslation();
        edu.wpi.first.math.geometry.Quaternion wpiQuaternion = new Rotation3d(pose.getRotation()).getQuaternion();
        Vector3 position = new Vector3(translation.getX(), translation.getY(), 0);
        Quaternion orientation = new Quaternion(
                wpiQuaternion.getX(),
                wpiQuaternion.getY(),
                wpiQuaternion.getZ(),
                wpiQuaternion.getW()
        );

        return mutate(Optional.of(position), Optional.of(orientation));
    }

    public Pose mutate(Pose3d pose) {
        Translation3d translation = pose.getTranslation();
        edu.wpi.first.math.geometry.Quaternion wpiQuaternion = pose.getRotation().getQuaternion();
        Vector3 position = new Vector3(translation.getX(), translation.getY(), translation.getZ());
        Quaternion orientation = new Quaternion(
                wpiQuaternion.getX(),
                wpiQuaternion.getY(),
                wpiQuaternion.getZ(),
                wpiQuaternion.getW()
        );

        return mutate(Optional.of(position), Optional.of(orientation));
    }
}
