package io.github.arrowvark.jfoxlog.defaults.topics;

import io.github.arrowvark.jfoxlog.foxglove.servers.http.FoxgloveHttpServer;
import io.github.arrowvark.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;
import io.github.arrowvark.jfoxlog.foxglove.servers.websocket.FoxgloveWebSocketServer;
import io.github.arrowvark.jfoxlog.foxglove.types.geometry.Quaternion;
import io.github.arrowvark.jfoxlog.foxglove.types.geometry.pose.Pose;
import io.github.arrowvark.jfoxlog.foxglove.types.geometry.vector.Vector3;
import io.github.arrowvark.jfoxlog.foxglove.types.misc.Color;
import io.github.arrowvark.jfoxlog.foxglove.types.misc.KeyValuePair;
import io.github.arrowvark.jfoxlog.foxglove.types.primitive.*;
import io.github.arrowvark.jfoxlog.foxglove.types.scene.SceneUpdate;
import io.github.arrowvark.jfoxlog.foxglove.types.scene.sceneEntity.SceneEntity;
import io.github.arrowvark.jfoxlog.foxglove.types.scene.sceneEntity.SceneEntityDeletion;
import io.github.arrowvark.jfoxlog.foxglove.types.time.Duration;
import io.github.arrowvark.jfoxlog.foxglove.types.time.Timestamp;

public class FieldLogger {
    public static void init() {}
    static {
        String url = FoxgloveHttpServer.serveFile("/field.glb", "/org/prime/models/field.glb");
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
