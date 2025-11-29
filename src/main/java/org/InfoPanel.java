package org;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private final GraphController controller;
    private EdgeChartPanel edgeChartPanel;
    private TriangleChartPanel triangleChartPanel;

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
        triangleChartPanel = new TriangleChartPanel(controller);
        JPanel connectednessPanel = new JPanel();
        JPanel k4SubgraphPanel = new JPanel();
        JPanel hamiltonianCyclePanel = new JPanel();

        //Tabs
        tabbedPane.addTab("Edge", edgeChartPanel);
        tabbedPane.addTab("Triangle Subgraph", triangleChartPanel);
        tabbedPane.addTab("Connectedness", connectednessPanel);
        tabbedPane.addTab("K4 Subgraph", k4SubgraphPanel);
        tabbedPane.addTab("Hamiltonian Cycle", hamiltonianCyclePanel);

        //Default Tabs
        tabbedPane.setSelectedIndex(0);

        add(tabbedPane, BorderLayout.CENTER);
    }

    public void updateCharts() {
        edgeChartPanel.updateChart();
        triangleChartPanel.updateChart();
    }
}