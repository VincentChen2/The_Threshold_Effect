package org;

import org.GraphComponents.GraphController;
import org.Panels.InfoPanel;
import org.Panels.InputPanel;
import org.Panels.StatusPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Graph Generator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //Initialize Components
        GraphController graphController = new GraphController();
        InputPanel inputPanel = new InputPanel(graphController);
        InfoPanel infoPanel = new InfoPanel(graphController);
        StatusPanel statusPanel = new StatusPanel(graphController);

        //Register Components with Controller
        graphController.setInputPanel(inputPanel);
        graphController.setInfoPanel(infoPanel);
        graphController.setStatusPanel(statusPanel);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
    }
}