package org;

import org.jfree.data.xy.XYSeries;

public class ConnectednessChartPanel extends AbstractChartPanel {

    public ConnectednessChartPanel(GraphController controller) {
        super(controller);
    }

    @Override
    protected String getChartTitle() {
        return "Graph Connectedness Scatter Plot";
    }

    @Override
    protected String getXAxisLabel() {
        return "p value";
    }

    @Override
    protected String getYAxisLabel() {
        return "Proportion of Connected Graphs";
    }

    @Override
    protected boolean hasProperty(Graph graph) {
        return graph.isConnected();
    }

    @Override
    protected XYSeries createTheoreticalSeries(int n, double xAxisStart, double xAxisEnd) {
        // TODO: Implement theoretical probability for graph connectedness
        return null;
    }
}