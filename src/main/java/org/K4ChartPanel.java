package org;

import org.jfree.data.xy.XYSeries;

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
    protected XYSeries createTheoreticalSeries(int n, double xAxisStart, double xAxisEnd) {
        // TODO: Implement theoretical probability for K4 existence
        return null;
    }
}