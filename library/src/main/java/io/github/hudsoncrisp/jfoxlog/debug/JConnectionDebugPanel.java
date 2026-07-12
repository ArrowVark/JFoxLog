package io.github.hudsoncrisp.jfoxlog.debug;

import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveWebSocketConnection;

import javax.swing.*;
import java.awt.*;

public class JConnectionDebugPanel extends JPanel {
    private final FoxgloveWebSocketConnection connection;

    public JConnectionDebugPanel(FoxgloveWebSocketConnection connection) {
        super(new BorderLayout(12, 0));
        this.connection = connection;
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 0),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(),
                        BorderFactory.createEmptyBorder(2, 0, 2, 0)
                )
        ));
        this.setPreferredSize(new Dimension(0, 100));

        this.add(new JLabel("Test"));
    }
}
