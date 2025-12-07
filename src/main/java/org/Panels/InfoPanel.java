package org.Panels;

import org.GraphComponents.GraphController;
import org.Panels.ChartPanels.*;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private final GraphController controller;
    private EdgeChartPanel edgeChartPanel;
    private TriangleChartPanel triangleChartPanel;
    private K4ChartPanel k4ChartPanel;
    private ConnectednessChartPanel connectednessChartPanel;
    private HamiltonianChartPanel hamiltonianChartPanel;

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
        connectednessChartPanel  = new ConnectednessChartPanel(controller);
        k4ChartPanel = new K4ChartPanel(controller);
        hamiltonianChartPanel = new HamiltonianChartPanel(controller);

        //Tabs
        tabbedPane.addTab("Edge", edgeChartPanel);
        tabbedPane.addTab("Triangle Subgraph", triangleChartPanel);
        tabbedPane.addTab("Connectedness", connectednessChartPanel);
        tabbedPane.addTab("K4 Subgraph", k4ChartPanel);
        tabbedPane.addTab("Hamiltonian Cycle", hamiltonianChartPanel);

        //Default Tab
        tabbedPane.setSelectedIndex(0);

        add(tabbedPane, BorderLayout.CENTER);
    }

    public void updateCharts() {
        edgeChartPanel.updateChart();
        triangleChartPanel.updateChart();
        k4ChartPanel.updateChart();
        connectednessChartPanel.updateChart();
        hamiltonianChartPanel.updateChart();
    }
}