package org;

import org.jfree.data.xy.XYSeries;

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
    protected XYSeries createTheoreticalSeries(int n, double xAxisStart, double xAxisEnd) {
        //TODO: Implement theoretical probability for triangle existence
        return null;
    }
}