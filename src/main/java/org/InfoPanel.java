package org;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private final GraphController controller;
    private EdgeChartPanel edgeChartPanel;

    public InfoPanel(GraphController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Graph Information"));

        JTabbedPane tabbedPane = new JTabbedPane();

        //Panels
        edgeChartPanel = new EdgeChartPanel(controller);
        JPanel triangleSubgraphPanel = new JPanel(); // Empty for now
        JPanel connectednessPanel = new JPanel(); // Empty for now
        JPanel k4SubgraphPanel = new JPanel(); // Empty for now
        JPanel hamiltonianCyclePanel = new JPanel(); // Empty for now

        //Tabs
        tabbedPane.addTab("Edge", edgeChartPanel);
        tabbedPane.addTab("Triangle Subgraph", triangleSubgraphPanel);
        tabbedPane.addTab("Connectedness", connectednessPanel);
        tabbedPane.addTab("K4 Subgraph", k4SubgraphPanel);
        tabbedPane.addTab("Hamiltonian Cycle", hamiltonianCyclePanel);

        //Default Tabs
        tabbedPane.setSelectedIndex(0);

        add(tabbedPane, BorderLayout.CENTER);
    }

    public void updateCharts() {
        edgeChartPanel.updateChart();
    }
}