package org.Panels.ChartPanels;

import org.GraphComponents.Graph;
import org.GraphComponents.GraphController;

public class TriangleChartPanel extends AbstractChartPanel {

    public TriangleChartPanel(GraphController controller) {
        super(controller);
    }

    @Override
    protected String getChartTitle() {
        return "Triangle (K3) Existence Scatter Plot";
    }

    @Override
    protected String getXAxisLabel() {
        return "p value";
    }

    @Override
    protected String getYAxisLabel() {
        return "Proportion of Graphs with Triangles";
    }

    @Override
    protected boolean hasProperty(Graph graph) {
        return graph.hasTriangle();
    }

    @Override
    protected Double calculateThresholdValue(int n) {
        if (n < 3) return 1.0;
        double nChoose3 = n * (n-1) * (n-2) / 6.0;
        return Math.pow(1.0 / nChoose3, 1.0/3.0);
    }
}