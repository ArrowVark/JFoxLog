package io.github.hudsoncrisp.jfoxlog.foxglove.types.scene.sceneEntity;

import io.github.hudsoncrisp.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.Util;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.misc.KeyValuePair;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.primitive.*;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.time.Duration;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.time.Timestamp;
//import io.github.hudsoncrisp.jfoxlog.foxglove.util.UnfinishedBuilderComponent;


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

    public SceneEntity() {}

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

//    public static class Builder<Ctx> extends FoxgloveLoggableBuilder<SceneEntity, Builder<Ctx>> {
//        private final Ctx ctx;
//        private UnfinishedBuilderComponent<Timestamp, Timestamp.Builder<Builder<Ctx>>> timestamp;
//        private Supplier<String> frame_id;
//        private Supplier<String> id;
//        private Supplier<Duration> lifetime;
//        private Supplier<Boolean> frame_locked;
//        private List<UnfinishedBuilderComponent<KeyValuePair, KeyValuePair.Builder<Builder<Ctx>>>> metadata;
////        private List<UnfinishedBuilderComponent<ArrowPrimitive, ArrowPrimitive.Builder<Builder<Ctx>>>> arrows;
////        private List<UnfinishedBuilderComponent<CubePrimitive, CubePrimitive.Builder<Builder<Ctx>>>> cubes;
////        private List<UnfinishedBuilderComponent<SpherePrimitive, SpherePrimitive.Builder<Builder<Ctx>>>> spheres;
////        private List<UnfinishedBuilderComponent<CylinderPrimitive, CylinderPrimitive.Builder<Builder<Ctx>>>> cylinders;
////        private List<UnfinishedBuilderComponent<LinePrimitive, LinePrimitive.Builder<Builder<Ctx>>>> lines;
////        private List<UnfinishedBuilderComponent<TriangleListPrimitive, TriangleListPrimitive.Builder<Builder<Ctx>>>> triangles;
////        private List<UnfinishedBuilderComponent<TextPrimitive, TextPrimitive.Builder<Builder<Ctx>>>> texts;
////        private List<UnfinishedBuilderComponent<ModelPrimitive, ModelPrimitive.Builder<Builder<Ctx>>>> models;
//
//        public Builder(Ctx ctx) {
//            super(new SceneEntity());
//            this.ctx = ctx;
//        }
//
//        public Builder timestamp(Timestamp timestamp) {
//            this.timestamp = () -> timestamp;
//            return this;
//        }
//
//        public Builder timestamp(Supplier<Timestamp> timestampSupplier) {
//            this.timestamp = timestampSupplier;
//            return this;
//        }
//
//        public Builder frameId(String frameId) {
//            this.frame_id = () -> frameId;
//            return this;
//        }
//
//        public Builder frameId(Supplier<String> frameIdSupplier) {
//            this.frame_id = frameIdSupplier;
//            return this;
//        }
//
//        public Builder id(String id) {
//            this.id = () -> id;
//            return this;
//        }
//
//        public Builder id(Supplier<String> idSupplier) {
//            this.id = idSupplier;
//            return this;
//        }
//
//        public Builder lifetime(Duration lifetime) {
//            this.lifetime = () -> lifetime;
//            return this;
//        }
//
//        public Builder lifetime(Supplier<Duration> lifetimeSupplier) {
//            this.lifetime = lifetimeSupplier;
//            return this;
//        }
//
//        public Builder frameLocked(boolean frameLocked) {
//            this.frame_locked = () -> frameLocked;
//            return this;
//        }
//
//        public Builder frameLocked(Supplier<Boolean> frameLockedSupplier) {
//            this.frame_locked = frameLockedSupplier;
//            return this;
//        }
//
//        public Builder metadata() {
//            KeyValuePair.Builder builder = new KeyValuePair.Builder(this);
//            this.metadata.add(builder);
//            return this;
//        }
//
//        public Builder arrow() {
//            ArrowPrimitive.Builder builder = new ArrowPrimitive.Builder(this);
//            this.arrows.add(builder);
//            return this;
//        }
//
//        public Builder cube() {
//            CubePrimitive.Builder builder = new CubePrimitive.Builder(this);
//            this.cubes.add(builder);
//            return this;
//        }
//
//        public Builder sphere() {
//            SpherePrimitive.Builder builder = new SpherePrimitive.Builder(this);
//            this.spheres.add(builder);
//            return this;
//        }
//
//        public Builder cylinder() {
//            CylinderPrimitive.Builder builder = new CylinderPrimitive.Builder(this);
//            this.cylinders.add(builder);
//            return this;
//        }
//
//        public Builder line() {
//            LinePrimitive.Builder builder = new LinePrimitive.Builder(this);
//            this.lines.add(builder);
//            return this;
//        }
//
//        public Builder triangleList() {
//            TriangleListPrimitive.Builder builder = new TriangleListPrimitive.Builder(this);
//            this.triangles.add(builder);
//            return this;
//        }
//
//        public Builder text() {
//            TextPrimitive.Builder builder = new TextPrimitive.Builder(this);
//            this.texts.add(builder);
//            return this;
//        }
//
//        public Builder model() {
//            ModelPrimitive.Builder builder = new ModelPrimitive.Builder(this);
//            this.models.add(builder);
//            return this;
//        }
//
//        public Ctx done() {
//            return ctx;
//        }
//
//        @Override
//        protected SceneEntity constructLoggable() {
//            return null;
//        }
//
//        @Override
//        protected DynamicFoxgloveLoggable<SceneEntity> constructDynamicLoggable() {
//            return null;
//        }
//    }
}
