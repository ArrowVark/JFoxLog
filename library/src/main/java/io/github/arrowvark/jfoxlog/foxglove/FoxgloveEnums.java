package io.github.arrowvark.jfoxlog.foxglove;//package io.github.arrowvark.jfoxlog.foxglove;
//
//public class FoxgloveEnums {
//
//    private FoxgloveEnums() {}
//
//    public sealed interface FoxgloveEnum permits
//            LineType,
//            LogLevel,
//            NumericType,
//            PointsAnnotationType,
//            PositionCovarianceType,
//            SceneEntityDeletionType
//    {
//        public int getCode();
//    }
//
//    /**
//     * An enumeration indicating how input points should be interpreted to create lines
//     */
//    public enum LineType implements FoxgloveEnum {
//        /**
//         * Connected line segments: 0-1, 1-2, ..., (n-1)-n
//         */
//        LINE_STRIP(0),
//        /**
//         * Closed polygon: 0-1, 1-2, ..., (n-1)-n, n-0
//         */
//        LINE_LOOP(1),
//        /**
//         * Individual line segments: 0-1, 2-3, 4-5, ...
//         */
//        LINE_LIST(2);
//
//        private final int code;
//
//        LineType(int code) {
//            this.code = code;
//        }
//
//        @Override
//        public int getCode() {
//            return code;
//        }
//    }
//
//    /**
//     * Log level
//     */
//    public enum LogLevel implements FoxgloveEnum {
//        /**
//         * Unknown log level
//         */
//        UNKNOWN(0),
//        /**
//         * Debug log level
//         */
//        DEBUG(1),
//        /**
//         * Info log level
//         */
//        INFO(2),
//        /**
//         * Warning log level
//         */
//        WARNING(3),
//        /**
//         * Error log level
//         */
//        ERROR(4),
//        /**
//         * Fatal log level
//         */
//        FATAL(5);
//
//        private final int code;
//
//        LogLevel(int code) {
//            this.code = code;
//        }
//
//        @Override
//        public int getCode() {
//            return code;
//        }
//    }
//
//    /**
//     * Numeric type
//     */
//    public enum NumericType implements FoxgloveEnum {
//        /**
//         * Unknown numeric type
//         */
//        UNKNOWN(0),
//        /**
//         * Unsigned 8-bit integer
//         */
//        UINT8(1),
//        /**
//         * Signed 8-bit integer
//         */
//        INT8(2),
//        /**
//         * Unsigned 16-bit integer
//         */
//        UINT16(3),
//        /**
//         * Signed 16-bit integer
//         */
//        INT16(4),
//        /**
//         * Unsigned 32-bit integer
//         */
//        UINT32(5),
//        /**
//         * Signed 32-bit integer
//         */
//        INT32(6),
//        /**
//         * 32-bit floating-point number
//         */
//        FLOAT32(7),
//        /**
//         * 64-bit floating-point number
//         */
//        FLOAT64(8);
//
//        private final int code;
//
//        NumericType(int code) {
//            this.code = code;
//        }
//
//        @Override
//        public int getCode() {
//            return code;
//        }
//    }
//
//    /**
//     * Type of points annotation
//     */
//    public enum PointsAnnotationType implements FoxgloveEnum {
//        /**
//         * Unknown points annotation type
//         */
//        UNKNOWN(0),
//        /**
//         * Individual points: 0, 1, 2, ...
//         */
//        POINTS(1),
//        /**
//         * Closed polygon: 0-1, 1-2, ..., (n-1)-n, n-0
//         */
//        LINE_LOOP(2),
//        /**
//         * Connected line segments: 0-1, 1-2, ..., (n-1)-n
//         */
//        LINE_STRIP(3),
//        /**
//         * Individual line segments: 0-1, 2-3, 4-5, ...
//         */
//        LINE_LIST(4);
//
//        private final int code;
//
//        PointsAnnotationType(int code) {
//            this.code = code;
//        }
//
//        @Override
//        public int getCode() {
//            return code;
//        }
//    }
//
//    /**
//     * Type of position covariance
//     */
//    public enum PositionCovarianceType implements FoxgloveEnum {
//        /**
//         * Unknown position covariance type
//         */
//        UNKNOWN(0),
//        /**
//         * Position covariance is approximated
//         */
//        APPROXIMATED(1),
//        /**
//         * Position covariance is per-axis, so put it along the diagonal
//         */
//        DIAGONAL_KNOWN(2),
//        /**
//         * Position covariance of the fix is known
//         */
//        KNOWN(3);
//
//        private final int code;
//
//        PositionCovarianceType(int code) {
//            this.code = code;
//        }
//
//        @Override
//        public int getCode() {
//            return code;
//        }
//    }
//
//    /**
//     * An enumeration indicating which entities should match a SceneEntityDeletion command
//     */
//    public enum SceneEntityDeletionType implements FoxgloveEnum {
//        /**
//         * Delete the existing entity on the same topic that has the provided id
//         */
//        MATCHING_ID(0),
//        /**
//         * Delete all existing entities on the same topic
//         */
//        ALL(1);
//
//        private final int code;
//
//        SceneEntityDeletionType(int code) {
//            this.code = code;
//        }
//
//        @Override
//        public int getCode() {
//            return code;
//        }
//    }
//}
