package org;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    private JLabel statusLabel;
    private JProgressBar progressBar;

    public StatusPanel(GraphController controller) {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Status"));

        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 5, 5));

        //Status Label
        statusLabel = new JLabel("Ready to generate graphs");
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(statusLabel);

        //Progress Bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        contentPanel.add(progressBar);

        add(contentPanel, BorderLayout.CENTER);
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public void setProgress(int progress) {
        progressBar.setValue(progress);
    }

    public void setProgressVisible(boolean visible) {
        progressBar.setVisible(visible);
    }
}