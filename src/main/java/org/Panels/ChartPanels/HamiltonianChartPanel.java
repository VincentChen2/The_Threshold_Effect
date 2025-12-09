package org.Panels.ChartPanels;

import org.GraphComponents.Graph;
import org.GraphComponents.GraphController;
import org.Panels.InputPanel;
import javax.swing.JLabel;
import java.awt.*;

public class HamiltonianChartPanel extends AbstractChartPanel {
    private JLabel warningLabel;
    private static final int MAX_N_FOR_HAMILTONIAN = 15;

    public HamiltonianChartPanel(GraphController controller) {
        super(controller);
        addWarningLabel();
    }

    private void addWarningLabel() {
        warningLabel = new JLabel("Note: Hamiltonian cycle detection disabled for n > " + MAX_N_FOR_HAMILTONIAN);
        warningLabel.setHorizontalAlignment(JLabel.CENTER);
        warningLabel.setForeground(Color.RED);
        warningLabel.setVisible(false);
        add(warningLabel, BorderLayout.NORTH);
    }

    @Override
    protected String getChartTitle() {
        return "Hamiltonian Cycle Existence Scatter Plot";
    }

    @Override
    protected String getXAxisLabel() {
        return "p value";
    }

    @Override
    protected String getYAxisLabel() {
        return "Proportion of Graphs with Hamiltonian Cycles";
    }

    @Override
    protected boolean hasProperty(Graph graph) {
        if (graph.getVertexCount() > MAX_N_FOR_HAMILTONIAN) {
            return false;
        }
        return graph.hasHamiltonianCycle();
    }

    @Override
    public void updateChart() {
        InputPanel inputPanel = controller.getInputPanel();
        int n = inputPanel != null ? inputPanel.getNValue() : 0;

        if (n > MAX_N_FOR_HAMILTONIAN) {
            warningLabel.setVisible(true);
            chart = createEmptyChart();
            chart.setTitle(getChartTitle() + " (Disabled: n=" + n + " > " + MAX_N_FOR_HAMILTONIAN + ")");
            chartPanel.setChart(chart);
        } else {
            warningLabel.setVisible(false);
            super.updateChart();
        }
    }
}