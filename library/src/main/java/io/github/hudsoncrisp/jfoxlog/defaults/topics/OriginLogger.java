package io.github.hudsoncrisp.jfoxlog.defaults.topics;

import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;
import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveWebSocketServer;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.geometry.Quaternion;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.geometry.vector.Vector3;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.scene.frameTransform.FrameTransform;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.time.Timestamp;

public class OriginLogger {
    public static void init() {}

    private static final FoxgloveChannel<FrameTransform> channel = FoxgloveWebSocketServer.requestNewChannel(
            "Server/Scene/Origin",
            FoxgloveChannel.LoggingType.LOW_FREQUENCY_SERVER_DRIVEN_STATIC,
            new FrameTransform(
                    Timestamp.now(),
                    "Root",
                    "Origin",
                    new Vector3(8.27, 4.105, 0),
                    Quaternion.kIdentity()
            )
    );
}
