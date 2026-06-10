//package io.github.arrowvark.jfoxlog.foxglove.helpers;
//
//import io.github.arrowvark.jfoxlog.foxglove.types.misc.KeyValuePair;
//import io.github.arrowvark.jfoxlog.foxglove.types.primitive.*;
//import io.github.arrowvark.jfoxlog.foxglove.types.scene.sceneEntity.SceneEntity;
//import io.github.arrowvark.jfoxlog.foxglove.types.scene.sceneEntity.SceneEntityDeletion;
//import io.github.arrowvark.jfoxlog.foxglove.types.time.Duration;
//import io.github.arrowvark.jfoxlog.foxglove.types.time.Timestamp;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SceneBuilder {
//    private List<SceneEntity.Builder<SceneBuilder>> entities = new ArrayList<>();
//    private List<SceneEntityDeletion> deletions = new ArrayList<>();
//
//    private SceneBuilder() {}
//
//    public static SceneBuilder create() {
//        return new SceneBuilder();
//    }
//
//    public SceneEntity.Builder<SceneBuilder> entity(String frame_id, String id) {
//        SceneEntity.Builder<SceneBuilder> builder = new SceneEntity.Builder<>(this, frame_id, id);
//        entities.add(builder);
//        return builder;
//    }
//}
