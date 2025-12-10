package org.Panels.ChartPanels;

import org.GraphComponents.Graph;
import org.GraphComponents.GraphController;

public class EdgeChartPanel extends AbstractChartPanel {

    public EdgeChartPanel(GraphController controller) {
        super(controller);
    }

    @Override
    protected String getChartTitle() {
        return "Edge Existence Scatter Plot";
    }

    @Override
    protected String getXAxisLabel() {
        return "p value";
    }

    @Override
    protected String getYAxisLabel() {
        return "Proportion of Graphs with Edges";
    }

    @Override
    protected boolean hasProperty(Graph graph) {
        return graph.hasAnyEdge();
    }

    @Override
    protected Double calculateThresholdValue(int n) {
        if (n < 2) return 1.0;
        double nChoose2 = (double) (n * (n - 1)) / 2.0;
        if (nChoose2 <= 0) return 1.0;

        return 1.0 / nChoose2;
    }
}