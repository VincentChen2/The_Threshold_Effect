package org;

import org.jfree.data.xy.XYSeries;
import javax.swing.JLabel;

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
        warningLabel.setForeground(java.awt.Color.RED);
        warningLabel.setVisible(false);
        add(warningLabel, java.awt.BorderLayout.NORTH);
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
    protected XYSeries createTheoreticalSeries(int n, double xAxisStart, double xAxisEnd) {
        if (n > MAX_N_FOR_HAMILTONIAN) {
            return null;
        }
        // TODO: Implement theoretical probability for Hamiltonian cycle existence
        return null;
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