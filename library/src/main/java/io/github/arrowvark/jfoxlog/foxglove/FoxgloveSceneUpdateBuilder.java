package io.github.arrowvark.jfoxlog.foxglove;//package io.github.arrowvark.jfoxlog.foxglove.websocket;
//
//import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;
//import io.github.arrowvark.jfoxlog.foxglove.types.primitive.*;
//import io.github.arrowvark.jfoxlog.foxglove.types.scene.SceneUpdate;
//import io.github.arrowvark.jfoxlog.foxglove.types.time.Duration;
//import io.github.arrowvark.jfoxlog.foxglove.types.time.Timestamp;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.logging.Logger;
//
//public class FoxgloveSceneUpdateBuilder {
//
//    private Timestamp timestamp;
//    private String frame_id;
//    private String id;
//    private Duration lifetime;
//    private boolean frame_locked;
//
//    private final List<ArrowPrimitive> arrows = new ArrayList<>();
//    private final List<CubePrimitive> cubes = new ArrayList<>();
//    private final List<SpherePrimitive> spheres = new ArrayList<>();
//    private final List<CylinderPrimitive> cylinders = new ArrayList<>();
//    private final List<LinePrimitive> lines = new ArrayList<>();
//    private final List<TriangleListPrimitive> triangleLists = new ArrayList<>();
//    private final List<TextPrimitive> texts = new ArrayList<>();
//    private final List<ModelPrimitive> models = new ArrayList<>();
//
//    private final List<ArrowPrimitive> arrowsDel = new ArrayList<>();
//    private final List<CubePrimitive> cubesDel = new ArrayList<>();
//    private final List<SpherePrimitive> spheresDel = new ArrayList<>();
//    private final List<CylinderPrimitive> cylindersDel = new ArrayList<>();
//    private final List<LinePrimitive> linesDel = new ArrayList<>();
//    private final List<TriangleListPrimitive> triangleListsDel = new ArrayList<>();
//    private final List<TextPrimitive> textsDel = new ArrayList<>();
//    private final List<ModelPrimitive> modelsDel = new ArrayList<>();
//
//    private FoxgloveSceneUpdateBuilder(
//            Optional<Timestamp> timestamp,
//            String frame_id,
//            String id,
//            Duration lifetime,
//            boolean frame_locked
//    ) {
//        timestamp.ifPresent(aTimestamp -> this.timestamp = aTimestamp);
//    }
//
//    public static FoxgloveSceneUpdateBuilder setupBuilder() {
//        return new FoxgloveSceneUpdateBuilder();
//    }
//
//    public FoxgloveSceneUpdateBuilder add(FoxgloveLoggable... loggables) {
//        for (FoxgloveLoggable loggable : loggables) {
//            if (loggable instanceof ArrowPrimitive) {
//                arrows.add((ArrowPrimitive) loggable);
//            } else if (loggable instanceof CubePrimitive) {
//                cubes.add((CubePrimitive) loggable);
//            } else if (loggable instanceof SpherePrimitive) {
//                spheres.add((SpherePrimitive) loggable);
//            } else if (loggable instanceof CylinderPrimitive) {
//                cylinders.add((CylinderPrimitive) loggable);
//            } else if(loggable instanceof LinePrimitive) {
//                lines.add((LinePrimitive) loggable);
//            } else if (loggable instanceof TriangleListPrimitive) {
//                triangleLists.add((TriangleListPrimitive) loggable);
//            } else if (loggable instanceof TextPrimitive) {
//                texts.add((TextPrimitive) loggable);
//            } else if (loggable instanceof ModelPrimitive) {
//                models.add((ModelPrimitive) loggable);
//            } else {
//                Logger.getGlobal().warning(() -> "Type passed to FoxgloveSceneBuilder that can't be" +
//                        " added to a SceneUpdate (" + loggable.getClass().getName() + ")");
//            }
//        }
//
//        return this;
//    }
//
//    public FoxgloveSceneUpdateBuilder remove(FoxgloveLoggable... loggables) {
//        for (FoxgloveLoggable loggable : loggables) {
//            if (loggable instanceof ArrowPrimitive) {
//                arrowsDel.add((ArrowPrimitive) loggable);
//            } else if (loggable instanceof CubePrimitive) {
//                cubesDel.add((CubePrimitive) loggable);
//            } else if (loggable instanceof SpherePrimitive) {
//                spheresDel.add((SpherePrimitive) loggable);
//            } else if (loggable instanceof CylinderPrimitive) {
//                cylindersDel.add((CylinderPrimitive) loggable);
//            } else if(loggable instanceof LinePrimitive) {
//                linesDel.add((LinePrimitive) loggable);
//            } else if (loggable instanceof TriangleListPrimitive) {
//                triangleListsDel.add((TriangleListPrimitive) loggable);
//            } else if (loggable instanceof TextPrimitive) {
//                textsDel.add((TextPrimitive) loggable);
//            } else if (loggable instanceof ModelPrimitive) {
//                modelsDel.add((ModelPrimitive) loggable);
//            } else {
//                Logger.getGlobal().warning(() -> "Type passed to FoxgloveSceneBuilder that can't be" +
//                        " added to a SceneUpdate deletion (" + loggable.getClass().getName() + ")");
//            }
//        }
//
//        return this;
//    }
//
//    public SceneUpdate build() {
//
//    }
//}
