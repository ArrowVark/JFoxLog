package io.github.arrowvark.jfoxlog.foxglove;//package io.github.arrowvark.jfoxlog.foxglove;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import java.time.Instant;
//import java.util.Optional;
//import java.util.function.Consumer;
//
//public class FoxgloveTypes {
//
//    private FoxgloveTypes() {}
//
//
//
//    /**
//     * A primitive representing an arrow
//     *
//     * @param pose Position of the arrow's tail and orientation of the arrow. Identity orientation means the arrow points in the +x direction.
//     * @param shaft_length Length of the arrow shaft
//     * @param shaft_diameter Diameter of the arrow shaft
//     * @param head_length Length of the arrow head
//     * @param head_diameter Diameter of the arrow head
//     * @param color Color of the arrow
//     */
//    public record ArrowPrimitive(
//            Pose pose,
//            double shaft_length,
//            double shaft_diameter,
//            double head_length,
//            double head_diameter,
//            Color color
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return FoxgloveSchemas.ARROW_PRIMITIVE.schema();
//        }
//
//        @Override
//        public String getSchemaName() {
//            return FoxgloveSchemas.ARROW_PRIMITIVE.name();
//        }
//
//        @Override
//        public String toJson() {
//            return GSON.toJson(this);
//        }
//    }
//
//    /**
//     * Camera calibration parameters
//     *
//     * @param timestamp Timestamp of calibration data
//     * @param frame_id Frame of reference for the camera. The origin of the frame is the optical center of the camera. +x points to the right in the image, +y points down, and +z points into the plane of the image.
//     * @param width Image width
//     * @param height Image height
//     * @param distortion_model Name of distortion model\n\nSupported parameters: `plumb_bob` (k1, k2, p1, p2, k3), `rational_polynomial` (k1, k2, p1, p2, k3, k4, k5, k6), and `kannala_brandt` (k1, k2, k3, k4), and `fisheye62` (k0, k1, k2, k3, p0, p1, crit_theta [optional]). `plumb_bob` and `rational_polynomial` models are based on the pinhole model [OpenCV's](https://docs.opencv.org/4.11.0/d9/d0c/group__calib3d.html) [pinhole camera model](https://en.wikipedia.org/wiki/Distortion_%28optics%29#Software_correction). The `kannala_brandt` model matches the [OpenvCV fisheye](https://docs.opencv.org/4.11.0/db/d58/group__calib3d__fisheye.html) model. The `fisheye62` model matches the [Project Aria's Fisheye62 Model](https://facebookresearch.github.io/projectaria_tools/docs/tech_insights/camera_intrinsic_models).
//     * @param D Distortion parameters
//     * @param K Intrinsic camera matrix (3x3 row-major matrix)\n\nA 3x3 row-major matrix for the raw (distorted) image.\n\nProjects 3D points in the camera coordinate frame to 2D pixel coordinates using the focal lengths (fx, fy) and principal point (cx, cy).\n\n```\n    [fx  0 cx]\nK = [ 0 fy cy]\n    [ 0  0  1]\n```\n\n**Uncalibrated cameras:** Following ROS conventions for [CameraInfo](https://docs.ros.org/en/noetic/api/sensor_msgs/html/msg/CameraInfo.html), Foxglove also treats K[0] == 0.0 as indicating an uncalibrated camera, and calibration data will be ignored.
//     * @param R Rectification matrix (stereo cameras only, 3x3 row-major matrix)\n\nA rotation matrix aligning the camera coordinate system to the ideal stereo image plane so that epipolar lines in both stereo images are parallel.
//     * @param P Projection/camera matrix (3x4 row-major matrix)\n\n```\n    [fx'  0  cx' Tx]\nP = [ 0  fy' cy' Ty]\n    [ 0   0   1   0]\n```\n\nBy convention, this matrix specifies the intrinsic (camera) matrix of the processed (rectified) image. That is, the left 3x3 portion is the normal camera intrinsic matrix for the rectified image.\n\nIt projects 3D points in the camera coordinate frame to 2D pixel coordinates using the focal lengths (fx', fy') and principal point (cx', cy') - these may differ from the values in K.\n\nFor monocular cameras, Tx = Ty = 0. Normally, monocular cameras will also have R = the identity and P[1:3,1:3] = K.\n\nFoxglove currently does not support displaying stereo images, so Tx and Ty are ignored.\n\nGiven a 3D point [X Y Z]', the projection (x, y) of the point onto the rectified image is given by:\n\n```\n[u v w]' = P * [X Y Z 1]'\n       x = u / w\n       y = v / w\n```\n\nThis holds for both images of a stereo pair.\n
//     */
//    public record CameraCalibration(
//            Timestamp timestamp,
//            String frame_id,
//            int width,
//            int height,
//            String distortion_model,
//            double[] D,
//            double[] K,
//            double[] R,
//            double[] P
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return FoxgloveSchemas.CAMERA_CALIBRATION.schema();
//        }
//
//        @Override
//        public String getSchemaName() {
//            return FoxgloveSchemas.CAMERA_CALIBRATION.name();
//        }
//
//        @Override
//        public String toJson() {
//            return GSON.toJson(this);
//        }
//    }
//
//    /**
//     * A circle annotation on a 2D image
//     *
//     * @param timestamp Timestamp of circle
//     * @param position Center of the circle in 2D image coordinates (pixels).\nThe coordinate uses the top-left corner of the top-left pixel of the image as the origin.
//     * @param diameter Circle diameter in pixels
//     * @param thickness Line thickness in pixels
//     * @param fill_color Fill color
//     * @param outline_color Outline color
//     * @param metadata Additional user-provided metadata associated with this annotation. Keys must be unique.
//     */
//    public record CircleAnnotation(
//            Timestamp timestamp,
//            Point2 position,
//            double diameter,
//            double thickness,
//            Color fill_color,
//            Color outline_color,
//            Optional<KeyValuePair[]> metadata
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return FoxgloveSchemas.CIRCLE_ANNOTATION.schema();
//        }
//
//        @Override
//        public String getSchemaName() {
//            return FoxgloveSchemas.CIRCLE_ANNOTATION.name();
//        }
//
//        @Override
//        public String toJson() {
//            return GSON.toJson(this);
//        }
//    }
//
//    /**
//     * A color in RGBA format
//     *
//     * @param r Red value between 0 and 1
//     * @param g Green value between 0 and 1
//     * @param b Blue value between 0 and 1
//     * @param a Alpha value between 0 and 1
//     */
//    public record Color(
//            double r,
//            double g,
//            double b,
//            double a
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return FoxgloveSchemas.COLOR.schema();
//        }
//
//        @Override
//        public String getSchemaName() {
//            return FoxgloveSchemas.COLOR.name();
//        }
//
//        @Override
//        public String toJson() {
//            return GSON.toJson(this);
//        }
//    }
//
//    /**
//     * A compressed image
//     *
//     * @param timestamp Timestamp of image
//     * @param frame_id Frame of reference for the image. The origin of the frame is the optical center of the camera. +x points to the right in the image, +y points down, and +z points into the plane of the image.
//     * @param data Compressed image data in base64
//     * @param format Image format\n\nSupported values: `jpeg`, `png`, `webp`, `avif`
//     */
//    public record CompressedImage(
//            Timestamp timestamp,
//            String frame_id,
//            String data,
//            String format
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return FoxgloveSchemas.COMPRESSED_IMAGE.schema();
//        }
//
//        @Override
//        public String getSchemaName() {
//            return FoxgloveSchemas.COMPRESSED_IMAGE.name();
//        }
//
//        @Override
//        public String toJson() {
//            return GSON.toJson(this);
//        }
//    }
//
//    /**
//     * A compressed point cloud. A decoder for `format` must decompress `data`, using metadata stored in the compressed payload to recover point positions and any additional per-point attributes. The decoded point cloud must include at least 2 coordinate fields from `x`, `y`, and `z`; `red`, `green`, `blue`, and `alpha` are optional for customizing each point's color.
//     *
//     * @param timestamp Timestamp of point cloud
//     * @param frame_id Frame of reference
//     * @param pose The origin of the point cloud relative to the frame of reference
//     * @param data Compressed point cloud data in base64 for exactly one point cloud, including any format-specific metadata needed to describe the decoded point attributes.
//     * @param format Point cloud compression format.\n\nSupported values: `draco` ([Google Draco](https://google.github.io/draco/)).
//     */
//    public record CompressedPointCloud(
//            Timestamp timestamp,
//            String frame_id,
//            Pose pose,
//            String data,
//            String format
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return FoxgloveSchemas.COMPRESSED_POINT_CLOUD.schema();
//        }
//
//        @Override
//        public String getSchemaName() {
//            return FoxgloveSchemas.COMPRESSED_POINT_CLOUD.name();
//        }
//
//        @Override
//        public String toJson() {
//            return GSON.toJson(this);
//        }
//    }
//
//    /**
//     * A single frame of a compressed video bitstream
//     *
//     * @param timestamp Timestamp of video frame
//     * @param frame_id Frame of reference for the video.\n\nThe origin of the frame is the optical center of the camera. +x points to the right in the video, +y points down, and +z points into the plane of the video.
//     * @param data Compressed video frame data.\n\nFor packet-based video codecs this data must begin and end on packet boundaries (no partial packets), and must contain enough video packets to decode exactly one image (either a keyframe or delta frame). Note: Foxglove does not support video streams that include B frames because they require lookahead.\n\nSpecifically, the requirements for different `format` values are:\n\n- `h264`\n  - Use Annex B formatted data\n  - Each CompressedVideo message should contain enough NAL units to decode exactly one video frame\n  - Each message containing a key frame (IDR) must also include a SPS NAL unit\n\n- `h265` (HEVC)\n  - Use Annex B formatted data\n  - Each CompressedVideo message should contain enough NAL units to decode exactly one video frame\n  - Each message containing a key frame (IRAP) must also include relevant VPS/SPS/PPS NAL units\n\n- `vp9`\n  - Each CompressedVideo message should contain exactly one video frame\n\n- `av1`\n  - Use the \"Low overhead bitstream format\" (section 5.2)\n  - Each CompressedVideo message should contain enough OBUs to decode exactly one video frame\n  - Each message containing a key frame must also include a Sequence Header OBU
//     * @param format Video format.\n\nSupported values: `h264`, `h265`, `vp9`, `av1`.\n\nNote: compressed video support is subject to hardware limitations and patent licensing, so not all encodings may be supported on all platforms. See more about [H.265 support](https://caniuse.com/hevc), [VP9 support](https://caniuse.com/webm), and [AV1 support](https://caniuse.com/av1).
//     */
//    public record CompressedVideo(
//            Timestamp timestamp,
//            String frame_id,
//            String data,
//            String format
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return FoxgloveSchemas.COMPRESSED_VIDEO.schema();
//        }
//
//        @Override
//        public String getSchemaName() {
//            return FoxgloveSchemas.COMPRESSED_VIDEO.name();
//        }
//
//        @Override
//        public String toJson() {
//            return GSON.toJson(this);
//        }
//    }
//
//    /**
//     * A primitive representing a cube or rectangular prism
//     *
//     * @param pose Position of the center of the cube and orientation of the cube
//     * @param size Size of the cube along each axis
//     * @param color Color of the cube
//     */
//    public record CubePrimitive(
//            Pose pose,
//            Vector3 size,
//            Color color
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return FoxgloveSchemas.CUBE_PRIMITIVE.schema();
//        }
//
//        @Override
//        public String getSchemaName() {
//            return FoxgloveSchemas.CUBE_PRIMITIVE.name();
//        }
//
//        @Override
//        public String toJson() {
//            return GSON.toJson(this);
//        }
//    }
//
//    /**
//     * A primitive representing a cylinder, elliptic cylinder, or truncated cone
//     *
//     * @param pose Position of the center of the cylinder and orientation of the cylinder. The flat face(s) are perpendicular to the z-axis.
//     * @param size Size of the cylinder's bounding box
//     * @param bottom_scale 0-1, ratio of the diameter of the cylinder's bottom face (min z) to the bottom of the bounding box
//     * @param top_scale 0-1, ratio of the diameter of the cylinder's top face (max z) to the top of the bounding box
//     * @param color Color of the cylinder
//     */
//    public record CylinderPrimitive(
//            Pose pose,
//            Vector3 size,
//            double bottom_scale,
//            double top_scale,
//            Color color
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return FoxgloveSchemas.CYLINDER_PRIMITIVE.schema();
//        }
//
//        @Override
//        public String getSchemaName() {
//            return FoxgloveSchemas.CYLINDER_PRIMITIVE.name();
//        }
//
//        @Override
//        public String toJson() {
//            return GSON.toJson(this);
//        }
//    }
//
//    /**
//     * A duration of time, composed of seconds and nanoseconds
//     *
//     * @param sec The number of seconds in the duration
//     * @param nsec The number of nanoseconds in the positive direction
//     */
//    public record Duration(
//            int sec,
//            int nsec
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return FoxgloveSchemas.DURATION.schema();
//        }
//
//        @Override
//        public String getSchemaName() {
//            return FoxgloveSchemas.DURATION.name();
//        }
//
//        @Override
//        public String toJson() {
//            return GSON.toJson(this);
//        }
//    }
//
//    /**
//     * A transform between two reference frames in 3D space. The transform defines the position and orientation of a child frame within a parent frame. Translation moves the origin of the child frame relative to the parent origin. The rotation changes the orientation of the child frame around its origin.\n\nExamples:\n\n- With translation (x=1, y=0, z=0) and identity rotation (x=0, y=0, z=0, w=1), a point at (x=0, y=0, z=0) in the child frame maps to (x=1, y=0, z=0) in the parent frame.\n\n- With translation (x=1, y=2, z=0) and a 90-degree rotation around the z-axis (x=0, y=0, z=0.707, w=0.707), a point at (x=1, y=0, z=0) in the child frame maps to (x=-1, y=3, z=0) in the parent frame.
//     *
//     * @param timestamp Timestamp of transform
//     * @param parent_frame_id Name of the parent frame
//     * @param child_frame_id Name of the child frame
//     * @param translation Translation component of the transform, representing the position of the child frame's origin in the parent frame.
//     * @param rotation Rotation component of the transform, representing the orientation of the child frame in the parent frame
//     */
//    public record FrameTransform(
//            Timestamp timestamp,
//            String parent_frame_id,
//            String child_frame_id,
//            Vector3 translation,
//            Quaternion rotation
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return FoxgloveSchemas.FRAME_TRANSFORM.schema();
//        }
//
//        @Override
//        public String getSchemaName() {
//            return FoxgloveSchemas.FRAME_TRANSFORM.name();
//        }
//
//        @Override
//        public String toJson() {
//            return GSON.toJson(this);
//        }
//    }
//
//    /**
//     * An array of FrameTransform messages
//     *
//     * @param transforms Array of transforms
//     */
//    public record FrameTransforms(
//            FrameTransform[] transforms
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    /**
//     * GeoJSON data for annotating maps
//     *
//     * @param geojson GeoJSON data encoded as a UTF-8 string
//     */
//    public record GeoJSON(
//            String geojson
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    /**
//     * A 2D grid of data
//     *
//     * @param timestamp Timestamp of grid
//     * @param frame_id Frame of reference
//     * @param pose Origin of grid's corner relative to frame of reference; grid is positioned in the x-y plane relative to this origin
//     * @param column_count Number of grid columns
//     * @param cell_size Size of single grid cell along x and y axes, relative to `pose`
//     * @param row_stride Number of bytes between rows in `data`
//     * @param cell_stride Number of bytes between cells within a row in `data`
//     * @param fields Fields in `data`. `red`, `green`, `blue`, and `alpha` are optional for customizing the grid's color.\nTo enable RGB color visualization in the [3D panel](https://docs.foxglove.dev/docs/visualization/panels/3d#rgba-separate-fields-color-mode), include **all four** of these fields in your `fields` array:\n\n- `red` - Red channel value\n- `green` - Green channel value\n- `blue` - Blue channel value\n- `alpha` - Alpha/transparency channel value\n\n**note:** All four fields must be present with these exact names for RGB visualization to work. The order of fields doesn't matter, but the names must match exactly.\n\nRecommended type: `UINT8` (0-255 range) for standard 8-bit color channels.\n\nExample field definitions:\n\n**RGB color only:**\n\n```javascript\nfields: [\n { name: \"red\", offset: 0, type: NumericType.UINT8 },\n { name: \"green\", offset: 1, type: NumericType.UINT8 },\n { name: \"blue\", offset: 2, type: NumericType.UINT8 },\n { name: \"alpha\", offset: 3, type: NumericType.UINT8 },\n];\n```\n\n**RGB color with elevation (for 3D terrain visualization):**\n\n```javascript\nfields: [\n { name: \"red\", offset: 0, type: NumericType.UINT8 },\n { name: \"green\", offset: 1, type: NumericType.UINT8 },\n { name: \"blue\", offset: 2, type: NumericType.UINT8 },\n { name: \"alpha\", offset: 3, type: NumericType.UINT8 },\n { name: \"elevation\", offset: 4, type: NumericType.FLOAT32 },\n];\n```\n\nWhen these fields are present, the 3D panel will offer additional \"Color Mode\" options including \"RGBA (separate fields)\" to visualize the RGB data directly. For elevation visualization, set the \"Elevation field\" to your elevation layer name.
//     * @param data Grid cell data in base64, interpreted using `fields`, in row-major (y-major) order.\nFor the data element starting at byte offset i, the coordinates of its corner closest to the origin will be:\n\n- y = i / row_stride * cell_size.y\n- x = (i % row_stride) / cell_stride * cell_size.x
//     */
//    public record Grid(
//            Timestamp timestamp,
//            String frame_id,
//            Pose pose,
//            int column_count,
//            Vector2 cell_size,
//            int row_stride,
//            int cell_stride,
//            PackedElementField[] fields,
//            String data
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    /**
//     * Array of annotations for a 2D image
//     *
//     * @param timestamp Timestamp of the image annotations. When set, individual annotation timestamps will be ignored.
//     * @param circles Circle annotations
//     * @param points Points annotations
//     * @param texts Text annotations
//     * @param metadata Additional user-provided metadata associated with the image annotations. Keys must be unique within this object. Per-annotation metadata takes precedence over these values.
//     */
//    public record ImageAnnotations(
//            Optional<Timestamp> timestamp,
//            CircleAnnotation[] circles,
//            PointsAnnotation[] points,
//            TextAnnotation[] texts,
//            Optional<KeyValuePair[]> metadata
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    /**
//     * The state of a single joint (revolute or prismatic).
//     *
//     * @param name Joint name
//     * @param position Joint position. Radians for revolute joints, meters for prismatic joints.
//     * @param velocity Joint velocity. Rad/s for revolute joints, m/s for prismatic joints.
//     * @param acceleration Joint acceleration. Rad/s² for revolute joints, m/s² for prismatic joints.
//     * @param effort Joint effort (force or torque). Nm for revolute joints, N for prismatic joints.
//     */
//    public record JointState(
//            String name,
//            Optional<Double> position,
//            Optional<Double> velocity,
//            Optional<Double> acceleration,
//            Optional<Double> effort
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    /**
//     * The state of a set of joints at a given time.
//     *
//     * @param timestamp Timestamp of the joint states
//     * @param joints Joint states
//     */
//    public record JointStates(
//            Timestamp timestamp,
//            JointState[] joints
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    /**
//     * A key with its associated value
//     *
//     * @param key Key
//     * @param value Value
//     */
//    public record KeyValuePair(
//            String key,
//            String value
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    /**
//     * A single scan from a planar laser range-finder
//     *
//     * @param timestamp Timestamp of scan
//     * @param frame_id Frame of reference
//     * @param pose Origin of scan relative to frame of reference; points are positioned in the x-y plane relative to this origin; angles are interpreted as counterclockwise rotations around the z axis with 0 rad being in the +x direction
//     * @param start_angle Bearing of first point, in radians
//     * @param end_angle Bearing of last point, in radians
//     * @param ranges Distance of detections from origin; assumed to be at equally-spaced angles between start_angle and end_angle
//     * @param intensities Intensity of detections
//     */
//    public record LaserScan(
//            Timestamp timestamp,
//            String frame_id,
//            Pose pose,
//            double start_angle,
//            double end_angle,
//            double[] ranges,
//            double[] intensities
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    /**
//     * A primitive representing a series of points connected by lines
//     *
//     * @param type Drawing primitive to use for lines
//     * @param pose Origin of lines relative to reference frame
//     * @param thickness Line thickness
//     * @param scale_invariant Indicates whether thickness is a fixed size in screen pixels (true), or specified in world coordinates and scales with distance from the camera (false)
//     * @param points Points along the line
//     * @param color Solid color to use for the whole line. Ignored if colors is non-empty.
//     * @param colors Per-point colors (if non-empty, must have the same length as points).
//     * @param indices Indices into the points and colors attribute arrays, which can be used to avoid duplicating attribute data.
//     */
//    public record LinePrimitive(
//            FoxgloveEnums.LineType type,
//            Pose pose,
//            double thickness,
//            boolean scale_invariant,
//            Point3[] points,
//            Color color,
//            Color[] colors,
//            int[] indices
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record LocationFix(
//            Timestamp timestamp,
//            String frame_id,
//            double latitude,
//            double longitude,
//            double altitude,
//            double[] position_covariance,
//            FoxgloveEnums.PositionCovarianceType position_covariance_type,
//            Optional<Double> heading,
//            Optional<Velocity3> velocity,
//            Optional<Color> color,
//            Optional<KeyValuePair> metadata
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record LocationFixes(
//            LocationFix[] fixes
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public static class Log implements FoxgloveLoggable {
//        private Timestamp timestamp;
//        private int level;
//        private String message;
//        private final String name;
//        private String file;
//        private int line;
//
//        public Log(
//                Timestamp timestamp,
//                int level, // Temp fix, add a type adapter to change FoxgloveEnums to ints
//                String message,
//                String name,
//                String file,
//                int line
//        ) {
//            this.timestamp = timestamp;
//            this.level = level;
//            this.message = message;
//            this.name = name;
//            this.file = file;
//            this.line = line;
//        }
//
//        public Log(
//                int level,
//                String message
//        ) {
//            StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
//            String processName = ProcessHandle.current().info().command().orElse("Unknown");
//
//            this.timestamp = Timestamp.now();
//            this.level = level;
//            this.message = message;
//            this.name = processName;
//            this.file = caller.getFileName();
//            this.line = caller.getLineNumber();
//        }
//
//        public Log() {
//            name = ProcessHandle.current().info().command().orElse("Unknown");
//        }
//
//        public Timestamp timestamp() {
//            return timestamp;
//        }
//
//        public int level() {
//            return level;
//        }
//
//        public String message() {
//            return message;
//        }
//
//        public String name() {
//            return name;
//        }
//
//        public String file() {
//            return file;
//        }
//
//        public int line() {
//            return line;
//        }
//
//        public void message(int level, String message) {
//            StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
//            this.timestamp = Timestamp.now();
//            this.level = level;
//            this.message = message;
//            this.file = caller.getFileName();
//            this.line = caller.getLineNumber();
//        }
//
//        public void expire() {
//            this.level = -1;
//            this.message = null;
//        }
//
//        @Override
//        public String getSchema() {
//            return FoxgloveSchemas.LOG.schema();
//        }
//
//        @Override
//        public String getSchemaName() {
//            return FoxgloveSchemas.LOG.name();
//        }
//
//        @Override
//        public String toJson() {
//            // Add to make timestamp update every serialization
//            // timestamp = Timestamp.now();
//            return GSON.toJson(this);
//        }
//    }
//
//    public record ModelPrimitive(
//            Pose pose,
//            Vector3 scale,
//            Color color,
//            boolean override_color,
//            String url,
//            String media_type,
//            byte[] data
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record PackedElementField(
//            String name,
//            int offset,
//            FoxgloveEnums.NumericType type
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record Point2(
//            double x,
//            double y
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record Point3(
//            double x,
//            double y,
//            double z
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record Point3InFrame(
//            Timestamp timestamp,
//            String frame_id,
//            Point3 point
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record PointCloud(
//            Timestamp timestamp,
//            String frame_id,
//            Pose pose,
//            int point_stride,
//            PackedElementField[] fields,
//            byte[] data
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record PointsAnnotation(
//            Timestamp timestamp,
//            FoxgloveEnums.PointsAnnotationType type,
//            Point2[] points,
//            Color outline_color,
//            Color[] outline_colors,
//            Color fill_color,
//            double thickness,
//            Optional<KeyValuePair[]> metadata
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record Pose(
//            Vector3 position,
//            Quaternion orientation
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return FoxgloveSchemas.POSE.schema();
//        }
//
//        @Override
//        public String getSchemaName() {
//            return FoxgloveSchemas.POSE.name();
//        }
//
//        @Override
//        public String toJson() {
//            return GSON.toJson(this);
//        }
//    }
//
//    public record PoseInFrame(
//            Timestamp timestamp,
//            String frame_id,
//            Pose pose
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return FoxgloveSchemas.POSE_IN_FRAME.schema();
//        }
//
//        @Override
//        public String getSchemaName() {
//            return FoxgloveSchemas.POSE_IN_FRAME.name();
//        }
//
//        @Override
//        public String toJson() {
//            return GSON.toJson(this);
//        }
//    }
//
//    public record PosesInFrame(
//            Timestamp timestamp,
//            String frame_id,
//            Pose[] poses
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record Quaternion(
//            double x,
//            double y,
//            double z,
//            double w
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record RawAudio(
//            Timestamp timestamp,
//            byte[] data,
//            String format,
//            int sample_rate,
//            int number_of_channels
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record RawImage(
//            Timestamp timestamp,
//            String frame_id,
//            int width,
//            int height,
//            String encoding,
//            int step,
//            byte[] data
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record SceneEntity(
//            Timestamp timestamp,
//            String frame_id,
//            String id,
//            Duration lifetime,
//            boolean frame_locked,
//            KeyValuePair[] metadata,
//            ArrowPrimitive[] arrows,
//            CubePrimitive[] cubes,
//            SpherePrimitive[] spheres,
//            CylinderPrimitive[] cylinders,
//            LinePrimitive[] lines,
//            TriangleListPrimitive[] triangles,
//            TextPrimitive[] texts,
//            ModelPrimitive[] models
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record SceneEntityDeletion(
//            Timestamp timestamp,
//            FoxgloveEnums.SceneEntityDeletionType type,
//            String id
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record SceneUpdate(
//            SceneEntityDeletion[] deletions,
//            SceneEntity[] entities
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record SpherePrimitive(
//            Pose pose,
//            Vector3 size,
//            Color color
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record TextAnnotation(
//            Timestamp timestamp,
//            Point2 position,
//            String text,
//            double font_size,
//            Color text_color,
//            Color background_color,
//            Optional<KeyValuePair[]> metadata
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record TextPrimitive(
//            Pose pose,
//            boolean billboard,
//            double font_size,
//            boolean scale_invariant,
//            Color color,
//            String text
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record Timestamp(
//            long sec,
//            long nsec
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//
//        public static Timestamp now() {
//            Instant now = Instant.now();
//            long sec = now.getEpochSecond();
//            long nsec = now.getNano();
//
//            return new Timestamp(sec, nsec);
//        }
//    }
//
//    public record TriangleListPrimitive(
//            Pose pose,
//            Point3[] points,
//            Color color,
//            Color[] colors,
//            int[] indices
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record Vector2(
//            double x,
//            double y
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record Vector3(
//            double x,
//            double y,
//            double z
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record Velocity3(
//            double x,
//            double y,
//            double z
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//
//    public record VoxelGrid(
//            Timestamp timestamp,
//            String frame_id,
//            Pose pose,
//            int row_count,
//            int column_count,
//            Vector3 cell_size,
//            int slice_stride,
//            int row_stride,
//            int cell_stride,
//            PackedElementField[] fields,
//            byte[] data
//    ) implements FoxgloveLoggable {
//        @Override
//        public String getSchema() {
//            return "";
//        }
//
//        @Override
//        public String getSchemaName() {
//            return "";
//        }
//
//        @Override
//        public String toJson() {
//            return "";
//        }
//    }
//}
