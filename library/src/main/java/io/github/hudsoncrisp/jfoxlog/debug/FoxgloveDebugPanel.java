package io.github.hudsoncrisp.jfoxlog.debug;

import com.formdev.flatlaf.FlatDarkLaf;
import io.github.hudsoncrisp.jfoxlog.JFoxLog;
import io.github.hudsoncrisp.jfoxlog.foxglove.servers.Util;
import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;
import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveWebSocketConnection;
import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveWebSocketServer;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import com.formdev.flatlaf.FlatLightLaf;

public class FoxgloveDebugPanel {
    private static boolean debug = false;

    private static JFrame frame;
    private static JPanel channelsPanel;
    private static JTextField channelsFilterField;
    private static JPanel connectionsPanel;
    private static DefaultTableModel statsModel;
    private static JPanel filesPanel;

    private static JTextPane logArea;
    private static StyledDocument logAreaStyleDoc;

    private static final SimpleAttributeSet emptyAttributeSet = new SimpleAttributeSet();
    private static final SimpleAttributeSet prerunAttributeSet = new SimpleAttributeSet();
    private static final SimpleAttributeSet debugAttributeSet = new SimpleAttributeSet();
    private static final SimpleAttributeSet expectedAttributeSet = emptyAttributeSet;
    private static final SimpleAttributeSet infoAttributeSet = new SimpleAttributeSet();
    private static final SimpleAttributeSet warnAttributeSet = new SimpleAttributeSet();
    private static final SimpleAttributeSet errorAttributeSet = new SimpleAttributeSet();
    private static final SimpleAttributeSet unrecoverableAttributeSet = new SimpleAttributeSet();
    private static final SimpleAttributeSet fatalAttributeSet = new SimpleAttributeSet();
    static {
        StyleConstants.setForeground(prerunAttributeSet, Color.MAGENTA);
        StyleConstants.setItalic(prerunAttributeSet, true);

        StyleConstants.setForeground(debugAttributeSet, Color.GREEN);

        StyleConstants.setForeground(infoAttributeSet, Color.BLUE);
        StyleConstants.setItalic(infoAttributeSet, true);

        StyleConstants.setForeground(warnAttributeSet, Color.YELLOW);

        StyleConstants.setForeground(errorAttributeSet, Color.RED);

        StyleConstants.setForeground(unrecoverableAttributeSet, Color.RED);
        StyleConstants.setBold(unrecoverableAttributeSet, true);

        StyleConstants.setForeground(fatalAttributeSet, Color.RED);
        StyleConstants.setBold(fatalAttributeSet, true);
    }
    private static final String resetANSI = "\u001B[0m";
    private static final String emptyANSI = "";
    private static final String prerunANSI = "\u001B[3;35m";
    private static final String debugANSI = "\u001B[32m";
    private static final String expectedANSI = emptyANSI;
    private static final String infoANSI = "\u001B[3;36m";
    private static final String warnANSI = "\u001B[33m";
    private static final String errorANSI = "\u001B[31m";
    private static final String unrecoverableANSI = "\u001B[1;31m";
    private static final String fatalANSI = "\u001B[1;31m";

    private static final FlatLightLaf lightLaf = new FlatLightLaf();
    private static final FlatDarkLaf darkLaf = new FlatDarkLaf();

    record StyledLog(String text, AttributeSet attributeSet) {}
    private static final CopyOnWriteArrayList<StyledLog> pendingLogs = new CopyOnWriteArrayList<>();
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    private static final List<JChannelDebugPanel> channelDebugPanels = new ArrayList<>();
    private static final List<JConnectionDebugPanel> connectionDebugPanels = new ArrayList<>();
    private static final List<JFileDebugPanel> fileDebugPanels = new ArrayList<>();

    private static final Font mainFont = new Font(
            Font.MONOSPACED,
            Font.PLAIN,
            12
    );

    public static void show() {
        if (SwingUtilities.isEventDispatchThread()) {
            build();
            log("Debug panel shown", FoxgloveDebugLogMeta.Severity.INFO);
        } else {
            SwingUtilities.invokeLater(() -> {
                build();
                log("Debug panel shown", FoxgloveDebugLogMeta.Severity.INFO);
            });
        }
    }

    public static void hide() {
        if (frame != null) {
            SwingUtilities.invokeLater(() -> frame.setVisible(false));
        }
        log("Debug panel hidden", FoxgloveDebugLogMeta.Severity.INFO);
    }

    private static void build() {
        debug = true;
        log("JFoxLog debugging started", FoxgloveDebugLogMeta.Severity.INFO);
        if (frame != null) {
            frame.setVisible(true);
            frame.toFront();
            return;
        }

        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
//            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            UIManager.setLookAndFeel(lightLaf);
        } catch (Exception e) {
            log("Debug panel look and feel failed to be set", FoxgloveDebugLogMeta.Severity.ERROR);
        }

        JFoxLog.logLibraryComputeTime(true);
        JFoxLog.logPeriodicLoopTime(true);

        frame = new JFrame(
                "JFoxLog Debug - Logging at ws://" +
                        Util.getLocalIp() + ":" + FoxgloveWebSocketServer.getStaticPort()
        );
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(850, 480);
        frame.setLocationByPlatform(true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buildSplitPane());
        panel.setBorder(new EmptyBorder(7, 7, 7, 7));

        frame.add(panel);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                log("JFoxLog debugging ended", FoxgloveDebugLogMeta.Severity.INFO);
                debug = false;
            }
        });
        flushPendingLogs();

        log("Built debug panel", FoxgloveDebugLogMeta.Severity.INFO, FoxgloveDebugLogMeta.Tag.EXPECTED);
    }

    private static JSplitPane buildSplitPane() {
        JSplitPane pane = new JSplitPane();
        pane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        pane.setLeftComponent(buildTabPane());
        pane.setRightComponent(buildLogComponent());

        log("Built split pane", FoxgloveDebugLogMeta.Severity.INFO, FoxgloveDebugLogMeta.Tag.EXPECTED);
        return pane;
    }

    private static JTabbedPane buildTabPane() {
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("Channels", buildChannelsComponent());
        pane.addTab("Connections", buildConnectionsComponent());
        pane.addTab("Files", buildFilesComponent());
        pane.addTab("Statistics", buildStatsComponent());

        log("Built tab pane", FoxgloveDebugLogMeta.Severity.INFO, FoxgloveDebugLogMeta.Tag.EXPECTED);
        return pane;
    }

    private static JComponent buildChannelsComponent() {
        channelsPanel = new JPanel();
        channelsPanel.setLayout(new BoxLayout(channelsPanel, BoxLayout.Y_AXIS));
        channelsPanel.setBorder(new EmptyBorder(8, 8, 8, 8));

        channelsFilterField = new JTextField();
        channelsFilterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterChannels(channelsFilterField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterChannels(channelsFilterField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterChannels(channelsFilterField.getText());
            }
        });

        JPanel searchBarPanel = new JPanel(new BorderLayout(6, 0));
        searchBarPanel.add(new JLabel("Filter:"), BorderLayout.WEST);
        searchBarPanel.add(channelsFilterField, BorderLayout.CENTER);
        searchBarPanel.setBorder(new EmptyBorder(8, 8, 4, 8));

        JScrollPane scrollPane = new JScrollPane(channelsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new BevelBorder(BevelBorder.RAISED));
        panel.add(searchBarPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        filterChannels("");
        log("Built channels component", FoxgloveDebugLogMeta.Severity.INFO, FoxgloveDebugLogMeta.Tag.EXPECTED);
        return panel;
    }

    private static JComponent buildConnectionsComponent() {
        connectionsPanel = new JPanel();
        connectionsPanel.setLayout(new BoxLayout(connectionsPanel, BoxLayout.Y_AXIS));
        connectionsPanel.setBorder(new CompoundBorder(
                new BevelBorder(BevelBorder.RAISED),
                new EmptyBorder(8, 8, 8, 8)
        ));
        JScrollPane scrollPane = new JScrollPane(connectionsPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        reloadConnections();
        log("Built connections component", FoxgloveDebugLogMeta.Severity.INFO, FoxgloveDebugLogMeta.Tag.EXPECTED);
        addConnectionDebug(null);
        return scrollPane;
    }

    public static void addConnectionDebug(FoxgloveWebSocketConnection connection) {
        connectionDebugPanels.add(new JConnectionDebugPanel(connection));
        reloadConnections();
    }

    private static void reloadConnections() {
        if (connectionsPanel == null) return;

        connectionsPanel.removeAll();
        for (JConnectionDebugPanel connection : connectionDebugPanels) {
            connectionsPanel.add(connection);
        }
        if (connectionsPanel.getComponentCount() == 0) {
            JLabel emptyLabel = new JLabel("<html>No connections<br>You can connect to Foxglove at ws://" + Util.getLocalIp() + ":" + FoxgloveWebSocketServer.getStaticPort() + "</html>");
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(8, 4, 0, 0));
            connectionsPanel.add(emptyLabel);
        }
        connectionsPanel.revalidate();
        connectionsPanel.repaint();
    }

    public static void addFileDebug(File file) {
        fileDebugPanels.add(new JFileDebugPanel(file));
        reloadFiles();
    }

    private static void reloadFiles() {
        if (filesPanel == null) return;

        filesPanel.removeAll();
        for (JFileDebugPanel file : fileDebugPanels) {
            filesPanel.add(file);
        }
        if (filesPanel.getComponentCount() == 0) {
            JLabel emptyLabel = new JLabel("No hosted files");
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(8, 4, 0, 0));
            filesPanel.add(emptyLabel);
        }
        filesPanel.revalidate();
        filesPanel.repaint();
    }

    private static JComponent buildFilesComponent() {
        filesPanel = new JPanel();
        filesPanel.setLayout(new BoxLayout(filesPanel, BoxLayout.Y_AXIS));
        filesPanel.setBorder(new CompoundBorder(
                new BevelBorder(BevelBorder.RAISED),
                new EmptyBorder(8, 8, 8, 8)
        ));
        JScrollPane scrollPane = new JScrollPane(filesPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        reloadConnections();
        log("Built files component", FoxgloveDebugLogMeta.Severity.INFO, FoxgloveDebugLogMeta.Tag.EXPECTED);
        addFileDebug(null);
        return scrollPane;
    }

    private static JComponent buildLogComponent() {
        logArea = new JTextPane();
        logArea.setEditable(false);
        logArea.setFont(mainFont);
//        logArea.setLineWrap(true);
        logAreaStyleDoc = logArea.getStyledDocument();

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> logArea.setText(""));

        JButton lightDarkButton = new JButton("Switch Light/Dark");
        lightDarkButton.addActionListener(e -> {
            try {
                if (UIManager.getLookAndFeel() == lightLaf) {
                    UIManager.setLookAndFeel(darkLaf);
                    log("Switched debug panel look and feel to dark", FoxgloveDebugLogMeta.Severity.INFO);
                } else {
                    UIManager.setLookAndFeel(lightLaf);
                    log("Switched debug panel look and feel to light", FoxgloveDebugLogMeta.Severity.INFO);
                }
                SwingUtilities.updateComponentTreeUI(frame);
            } catch (Exception _e) {
                log("Failed to switch look and feel", FoxgloveDebugLogMeta.Severity.ERROR);
            }
        });

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(new EmptyBorder(7, 7, 7, 7));
        topBar.add(clearButton, BorderLayout.WEST);
        topBar.add(lightDarkButton, BorderLayout.EAST);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(topBar, BorderLayout.NORTH);
        panel.add(new JScrollPane(logArea), BorderLayout.CENTER);

        log("Built log component", FoxgloveDebugLogMeta.Severity.INFO, FoxgloveDebugLogMeta.Tag.EXPECTED);
        return panel;
    }

    private static void filterChannels(String query) {
        if (channelsPanel == null) return;
        String modifiedQuery = (query == null ? "" : query.trim().toLowerCase());

        channelsPanel.removeAll();
        for (JChannelDebugPanel panel : channelDebugPanels) {
            if (modifiedQuery.isEmpty() || panel.getName().toLowerCase().contains(modifiedQuery)) {
                channelsPanel.add(panel);
                channelsPanel.add(Box.createVerticalStrut(6));
            }
        }
        if (channelsPanel.getComponentCount() == 0) {
            String trimmedQuery = (query == null ? "" : query.trim());
            JLabel emptyLabel = new JLabel(channelDebugPanels.isEmpty()
                    ? "No channels registered yet"
                    : "No channels match \"" + trimmedQuery + "\"");
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(8, 4, 0, 0));
            channelsPanel.add(emptyLabel);
        }
        channelsPanel.revalidate();
        channelsPanel.repaint();
    }

    public static void log(String message, FoxgloveDebugLogMeta.Severity severity, FoxgloveDebugLogMeta.Tag... tags) {

        AttributeSet attributeSet;

        switch (severity) {
//            case PRERUN -> attributeSet = prerunAttributeSet;
            case DEBUG -> attributeSet = debugAttributeSet;
//            case EXPECTED -> attributeSet = expectedAttributeSet;
            case INFO -> attributeSet = infoAttributeSet;
            case WARN -> attributeSet = warnAttributeSet;
            case ERROR -> attributeSet = errorAttributeSet;
//            case UNRECOVERABLE -> attributeSet = unrecoverableAttributeSet;
            case FATAL -> attributeSet = fatalAttributeSet;
            default -> attributeSet = emptyAttributeSet;
        }

        StringBuilder tagsString = new StringBuilder();

        for (FoxgloveDebugLogMeta.Tag tag : tags) {
            tagsString.append("[").append(tag).append("] ");
        }

        log("[" + severity + "] " + tagsString + message, attributeSet, severity);
    }

    private static void log(String message, AttributeSet attributeSet, FoxgloveDebugLogMeta.Severity severity) {

        String prefix;

        switch (severity) {
//            case PRERUN -> prefix = prerunANSI;
            case DEBUG -> prefix = debugANSI;
//            case EXPECTED -> prefix = expectedANSI;
            case INFO -> prefix = infoANSI;
            case WARN -> prefix = warnANSI;
            case ERROR -> prefix = errorANSI;
//            case UNRECOVERABLE -> prefix = unrecoverableANSI;
            case FATAL -> prefix = fatalANSI;
            default -> prefix = emptyANSI;
        }

        String line = "[" + timeFormat.format(new Date()) + "] " + message;
        if (debug) System.out.println(prefix + "[JFoxLog Debug] " + line + resetANSI);
        if (logArea == null || logAreaStyleDoc == null) {
            pendingLogs.add(new StyledLog(line, attributeSet));
            return;
        }
        SwingUtilities.invokeLater(() -> {
            try {
                logAreaStyleDoc.insertString(logAreaStyleDoc.getLength(), line + "\n", attributeSet);
                logArea.setCaretPosition(logArea.getDocument().getLength());
            } catch (Exception e) {

            }
        });
    }

    private static void flushPendingLogs() {
        if (pendingLogs.isEmpty()) return;
        for (StyledLog log : pendingLogs) {
            try {
                logAreaStyleDoc.insertString(logAreaStyleDoc.getLength(), log.text + "\n", log.attributeSet);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        pendingLogs.clear();
        logArea.setCaretPosition(logArea.getDocument().getLength());
        log("Flushed pending logs", FoxgloveDebugLogMeta.Severity.INFO);
    }

    public static void addChannelDebug(String name, FoxgloveChannel<?> channel) {
        JChannelDebugPanel panel = new JChannelDebugPanel(name, channel);
        registerChannelDebug(panel);
        log("Registered channel \"" + name + "\"", FoxgloveDebugLogMeta.Severity.INFO);
    }

    private static void registerChannelDebug(JChannelDebugPanel channelPanel) {
        channelDebugPanels.add(channelPanel);
        if (channelsPanel != null) {
            SwingUtilities.invokeLater(() ->
                    filterChannels(channelsFilterField != null ? channelsFilterField.getText() : ""));
        }
    }

    private static JComponent buildStatsComponent() {
//        JLabel label = new JLabel("Stats panel");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new CompoundBorder(
                new BevelBorder(BevelBorder.RAISED),
                new EmptyBorder(15, 15, 15, 15)
        ));

        statsModel = new DefaultTableModel(0,2) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(statsModel);
        table.setOpaque(false);
        table.setShowGrid(false);
        table.setRowHeight(26);
        table.setFont(new Font(
                Font.SANS_SERIF,
                Font.BOLD,
                12
        ));

        setupIpStat();
        setupPortStat();
        setupUptimeStat(panel);
        setupChannelsStat(panel);
        setupConnectionsStat(panel);
        setupLibraryComputeTimeMillisStat(panel);
        setupLibraryComputeTimeNanoStat(panel);
        setupPeriodicLoopTimeStat(panel);
        panel.add(table);

        log("Built statistics panel", FoxgloveDebugLogMeta.Severity.INFO, FoxgloveDebugLogMeta.Tag.EXPECTED);
        return panel;
    }

    private static void setupUptimeStat(JPanel panel) {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        statsModel.addRow(new Object[]{"Uptime:", ""});
        int row = statsModel.getRowCount() - 1;
        Timer timer = new Timer(1000, e -> {
            long uptimeMillis = runtimeMXBean.getUptime();
            long uptimeSecondsShort = (uptimeMillis / 1000) % 60;
            long uptimeMinutesShort = (uptimeMillis / 60000) % 60;
            long uptimeHoursShort = (uptimeMillis / 3600000) % 60;

            statsModel.setValueAt(
                    uptimeHoursShort + " hr  "
                            + uptimeMinutesShort + " min  "
                            + uptimeSecondsShort + " sec",
                    row, 1);
            panel.revalidate();
            panel.repaint();
        });
        timer.setInitialDelay(1);
        timer.start();
    }

    private static void setupIpStat() {
        statsModel.addRow(new Object[]{"Logging IP:", Util.getLocalIp() + ":" + FoxgloveWebSocketServer.getStaticPort()});
    }

    private static void setupPortStat() {
        statsModel.addRow(new Object[]{"Logging Port:", FoxgloveWebSocketServer.getStaticPort()});
    }

    private static void setupChannelsStat(JPanel panel) {
        statsModel.addRow(new Object[]{"Number of Channels:", ""});
        int row = statsModel.getRowCount() - 1;
        Timer timer = new Timer(1000, e -> {
            statsModel.setValueAt(FoxgloveWebSocketServer.getChannels().size(), row, 1);
            panel.revalidate();
            panel.repaint();
        });
        timer.setInitialDelay(1);
        timer.start();
    }

    private static void setupConnectionsStat(JPanel panel) {
        statsModel.addRow(new Object[]{"Number of Connections:", ""});
        int row = statsModel.getRowCount() - 1;
        Timer timer = new Timer(1000, e -> {
            statsModel.setValueAt(FoxgloveWebSocketServer.getFoxgloveConnections().size(), row, 1);
            panel.revalidate();
            panel.repaint();
        });
        timer.setInitialDelay(1);
        timer.start();
    }

    private static void setupLibraryComputeTimeMillisStat(JPanel panel) {
        statsModel.addRow(new Object[]{"Library Compute Time (ms):", ""});
        int row = statsModel.getRowCount() - 1;
        Timer timer = new Timer(20, e -> {
            statsModel.setValueAt(JFoxLog.getLibraryComputeTimeMillis() + " ms", row, 1);
            panel.revalidate();
            panel.repaint();
        });
        timer.setInitialDelay(1);
        timer.start();
    }

    private static void setupLibraryComputeTimeNanoStat(JPanel panel) {
        statsModel.addRow(new Object[]{"Library Compute Time (nano):", ""});
        int row = statsModel.getRowCount() - 1;
        Timer timer = new Timer(20, e -> {
            statsModel.setValueAt(JFoxLog.getLibraryComputeTimeNano() + " nano", row, 1);
            panel.revalidate();
            panel.repaint();
        });
        timer.setInitialDelay(1);
        timer.start();
    }

    private static void setupPeriodicLoopTimeStat(JPanel panel) {
        statsModel.addRow(new Object[]{"Periodic Loop Time:", ""});
        int row = statsModel.getRowCount() - 1;
        Timer timer = new Timer(20, e -> {
            statsModel.setValueAt(JFoxLog.getPeriodicLoopTimeMillis() + " ms", row, 1);
            panel.revalidate();
            panel.repaint();
        });
        timer.setInitialDelay(1);
        timer.start();
    }
}
