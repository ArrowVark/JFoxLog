package io.github.hudsoncrisp.jfoxlog.foxglove.enums;

public sealed interface FoxgloveEnums permits
        LineType,
        LogLevel,
        NumericType,
        PointsAnnotationType,
        PositionCovarianceType,
        SceneEntityDeletionType
{
    public int getCode();
}
