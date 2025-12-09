package org.Panels.ChartPanels;

import org.GraphComponents.Graph;
import org.GraphComponents.GraphController;

public class K4ChartPanel extends AbstractChartPanel {

    public K4ChartPanel(GraphController controller) {
        super(controller);
    }

    @Override
    protected String getChartTitle() {
        return "K4 Subgraph Existence Scatter Plot";
    }

    @Override
    protected String getXAxisLabel() {
        return "p value";
    }

    @Override
    protected String getYAxisLabel() {
        return "Proportion of Graphs with K4 Subgraphs";
    }

    @Override
    protected boolean hasProperty(Graph graph) {
        return graph.hasK4();
    }

    @Override
    protected Double calculateThresholdValue(int n) {
        if (n < 4) return 1.0;
        double nChoose4 = n * (n-1) * (n-2) * (n-3) / 24.0;
        return Math.pow(1.0 / nChoose4, 1.0/6.0);
    }


    @Override
    protected Double calculateLowerBoundValue(int n) {
        if (n < 4) return 1.0;
        double nChoose4 = n * (n-1) * (n-2) * (n-3) / 24.0;
        return Math.pow(0.1 / nChoose4, 1.0/6.0);
    }

    @Override
    protected Double calculateUpperBoundValue(int n) {
        if (n < 4) return 1.0;
        double nChoose4 = n * (n-1) * (n-2) * (n-3) / 24.0;
        return Math.pow(5.0 / nChoose4, 1.0/6.0);
    }
}