package io.github.hudsoncrisp.jfoxlog.foxglove;

import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;
import io.github.hudsoncrisp.jfoxlog.foxglove.types.scene.SceneUpdate;

public interface ModelLink {
    byte[] getModelBytes();
    String getResourcePath();
    FoxgloveChannel.LoggingType getLoggingType();
    SceneUpdate getSceneUpdate();
}
