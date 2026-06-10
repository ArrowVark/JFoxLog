package io.github.arrowvark.jfoxlog.foxglove;

import io.github.arrowvark.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;
import io.github.arrowvark.jfoxlog.foxglove.types.scene.SceneUpdate;

public interface ModelLink {
    byte[] getModelBytes();
    String getResourcePath();
    FoxgloveChannel.LoggingType getLoggingType();
    SceneUpdate getSceneUpdate();
}
