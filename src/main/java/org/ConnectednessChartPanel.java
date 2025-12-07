package org;

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
    protected Double calculateThresholdValue(int n) {
        return Math.log(n)/n;
    }
}