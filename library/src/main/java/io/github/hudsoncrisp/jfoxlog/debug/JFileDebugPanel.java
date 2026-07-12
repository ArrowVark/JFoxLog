package io.github.hudsoncrisp.jfoxlog.debug;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class JFileDebugPanel extends JPanel {
    private final File file;
    public JFileDebugPanel(File file) {
        super(new BorderLayout(12, 0));
        this.file = file;
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 0),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(),
                        BorderFactory.createEmptyBorder(2, 0, 2, 0)
                )
        ));

        this.add(new JLabel("Test"));
    }
}
