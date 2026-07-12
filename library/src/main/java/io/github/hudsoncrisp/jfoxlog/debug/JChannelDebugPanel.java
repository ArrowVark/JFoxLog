package io.github.hudsoncrisp.jfoxlog.debug;

import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;

public class JChannelDebugPanel extends JComponent {
    private final String name;
    private final FoxgloveChannel<?> channel;
    private JPanel content;
    private DefaultTableModel contentTableModel;
    private final JLabel arrow = new JLabel("▼");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    private JTextArea cacheTextArea;

    public JChannelDebugPanel(String name, FoxgloveChannel<?> channel) {
//        super(new BorderLayout(12, 0));
        super();
        this.name = name;
        this.channel = channel;

        channel.logCacheDataAccessDates(true);

        buildContentPanel();
        arrow.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
        arrow.setBorder(new EmptyBorder(0, 0, 0, 7));
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 0),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(),
                        BorderFactory.createEmptyBorder(2, 0, 2, 0)
                )
        ));
        this.add(buildHeaderButton(), BorderLayout.NORTH);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }

    private JToggleButton buildHeaderButton() {
        JToggleButton button = new JToggleButton();
        button.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                this.add(content, BorderLayout.CENTER);
                arrow.setText("▲");
            } else {
                this.remove(content);
                arrow.setText("▼");
            }
            this.revalidate();
            this.repaint();
        });

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);

        JPanel label = new JPanel();
        label.add(arrow, BorderLayout.WEST);
        label.add(new JLabel(name), BorderLayout.EAST);

        button.setLayout(new BorderLayout(12, 0));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.add(label, BorderLayout.WEST);
        button.add(buildButtonPanel(), BorderLayout.EAST);
        return button;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));

        JToggleButton soloButton = new JToggleButton("Solo");
        soloButton.setToolTipText("Only log this and other soloed channels");

        JToggleButton suspendButton = new JToggleButton("Suspend");
        suspendButton.setToolTipText("Temporarily prevent logging on this channel");

        JButton freeButton = new JButton("Free");
        freeButton.setToolTipText(
                "<html>Permanently prevent logging on this channel<br>Removes this channel from the server completely</html>"
        );
        freeButton.addActionListener(e -> {
            channel.free();
            Container parent = this.getParent();
            if (parent != null) {
                parent.remove(this);
                parent.revalidate();
                parent.repaint();
            }
        });

        panel.add(soloButton);
        panel.add(suspendButton);
        panel.add(freeButton);
        return panel;
    }

    private JPanel buildContentPanel() {
        content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(14, 8, 8, 8));
        buildContentTable();
        content.add(buildCachePanel("Schema:", channel.getLoggable().getSchema()), BorderLayout.CENTER);
        content.add(buildCacheComponent(), BorderLayout.SOUTH);
        return content;
    }

    private JComponent buildCacheComponent() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel cache = buildCachePanel(channel::getCacheWithoutUpdate);
        panel.add(cache, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        buttonPanel.setBorder(new EmptyBorder(3, 0, 0, 0));

        JButton reloadCache = new JButton("Reload Cache");
        reloadCache.addActionListener(e -> {
            cacheTextArea.setText(channel.requestCache());
            FoxgloveDebugPanel.log("Reloaded cache for \"" + channel.getTopic() + "\"", FoxgloveDebugLogSeverity.INFO);
        });
        buttonPanel.add(reloadCache);

        JButton fetchData = new JButton("Fetch Data");
        fetchData.addActionListener(e -> {
            channel.requestData();
            FoxgloveDebugPanel.log("Fetched new data for \"" + channel.getTopic() + "\"", FoxgloveDebugLogSeverity.INFO);
        });
        buttonPanel.add(fetchData);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildCachePanel(String label, String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setEditable(false);
        textArea.setBorder(new EmptyBorder(4, 4, 4, 4));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(0, 150));

        JLabel jlabel = new JLabel(label, JLabel.LEFT);

        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        panel.add(jlabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildCachePanel(Supplier<String> text) {
        cacheTextArea = new JTextArea(text.get());
        cacheTextArea.setEditable(false);
        cacheTextArea.setBorder(new EmptyBorder(4, 4, 4, 4));
        DefaultCaret caret = (DefaultCaret) cacheTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

        JScrollPane scrollPane = new JScrollPane(cacheTextArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(0, 150));

        JLabel jlabel = new JLabel("Cache:", JLabel.LEFT);

        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));
        panel.add(jlabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        Timer timer = new Timer(20, e -> {
            cacheTextArea.setText(text.get());
            panel.revalidate();
            panel.repaint();
        });
        timer.setInitialDelay(1);
        timer.start();

        return panel;
    }

    private void buildContentTable() {
        contentTableModel = new DefaultTableModel(0, 2) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(contentTableModel);
        table.setOpaque(false);
        table.setShowGrid(false);
        table.setRowHeight(26);
        table.getColumnModel().getColumn(0).setMinWidth(140);
        table.getColumnModel().getColumn(0).setMaxWidth(140);
        table.setFont(new Font(
                Font.SANS_SERIF,
                Font.BOLD,
                12
        ));

        setupTypeEntry();
        setupIdEntry();
        setupLoggingTypeEntry();
        setupLastDataAccessEntry();
        setupLastCacheAccessEntry();
        content.add(table, BorderLayout.NORTH);
    }

    private void setupTypeEntry() {
        contentTableModel.addRow(new Object[]{"Type:", channel.getLoggable().getSchemaName()});
    }

    private void setupLoggingTypeEntry() {
        contentTableModel.addRow(new Object[]{"Logging Type:", channel.getLoggingType()});
    }

    private void setupIdEntry() {
        contentTableModel.addRow(new Object[]{"Id:", channel.getId()});
    }

    private void setupLastDataAccessEntry() {
        contentTableModel.addRow(new Object[]{"Last Data Access:", ""});
        int row = contentTableModel.getRowCount() - 1;
        Timer timer = new Timer(20, e -> {
            Date date = channel.getLastDataAccessDate();
            String time = date == null ? "No previous access" : timeFormat.format(date);
            contentTableModel.setValueAt(
                    time,
                    row, 1
            );
            this.revalidate();
            this.repaint();
        });
        timer.setInitialDelay(1);
        timer.start();
    }

    private void setupLastCacheAccessEntry() {
        contentTableModel.addRow(new Object[]{"Last Cache Access:", ""});
        int row = contentTableModel.getRowCount() - 1;
        Timer timer = new Timer(20, e -> {
            Date date = channel.getLastCacheAccessDate();
            String time = date == null ? "No previous access" : timeFormat.format(date);
            contentTableModel.setValueAt(
                    time,
                    row, 1
            );
            this.revalidate();
            this.repaint();
        });
        timer.setInitialDelay(1);
        timer.start();
    }

    public String getName() {
        return name;
    }
}
