package io.github.hudsoncrisp.jfoxlog.defaults.topics;

import io.github.hudsoncrisp.jfoxlog.foxglove.servers.http.FoxgloveHttpServer;
import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;
import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveWebSocketServer;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.geometry.Quaternion;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.geometry.pose.Pose;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.geometry.vector.Vector3;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.misc.Color;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.misc.KeyValuePair;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.primitive.*;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.scene.SceneUpdate;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.scene.sceneEntity.SceneEntity;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.scene.sceneEntity.SceneEntityDeletion;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.time.Duration;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.time.Timestamp;

public class FieldLogger {
    public static void init() {}
    static {
        String url = FoxgloveHttpServer.serveFile("/field.glb", "/io/github/hudsoncrisp/jfoxlog/field.glb");
        FoxgloveWebSocketServer.requestNewChannel(
                "Server/Scene/Field",
                FoxgloveChannel.LoggingType.LOW_FREQUENCY_SERVER_DRIVEN_STATIC,
                new SceneUpdate(
                        new SceneEntityDeletion[0],
                        new SceneEntity[]{
                                new SceneEntity(
                                        Timestamp.now(),
                                        "Root",
                                        "Field",
                                        Duration.kZero(),
                                        false,
                                        new KeyValuePair[0],
                                        new ArrowPrimitive[0],
                                        new CubePrimitive[0],
                                        new SpherePrimitive[0],
                                        new CylinderPrimitive[0],
                                        new LinePrimitive[0],
                                        new TriangleListPrimitive[0],
                                        new TextPrimitive[0],
                                        new ModelPrimitive[]{
                                                new ModelPrimitive(
                                                        new Pose(
                                                                Vector3.kZero(),
                                                                Quaternion.kIdentity()
                                                        ),
                                                        Vector3.kUnit(),
                                                        new Color(0, 23, 255, 0.35),
                                                        true,
                                                        url,
                                                        "",
                                                        ""
                                                )
                                        }
                                )
                        }
                )
        );
    }
}
