package org;

import org.jfree.data.xy.XYSeries;

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
    protected XYSeries createTheoreticalSeries(int n, double xAxisStart, double xAxisEnd) {
        double nChoose2 = (double) (n * (n - 1)) / 2;
        XYSeries theoreticalSeries = new XYSeries("Theoretical Probability");

        double theoryPadding = (xAxisEnd - xAxisStart) * 0.05;
        double theoryStart = Math.max(0, xAxisStart - theoryPadding);
        double theoryEnd = Math.min(1.0, xAxisEnd + theoryPadding);

        for (double p = theoryStart; p <= theoryEnd; p += 0.001) {
            double probability = 1 - Math.pow(1 - p, nChoose2);
            theoreticalSeries.add(p, probability);
        }

        return theoreticalSeries;
    }

    @Override
    protected Double calculateThresholdValue(int n) {
        if (n < 2) return 1.0;
        double nChoose2 = (double) (n * (n - 1)) / 2.0;
        if (nChoose2 <= 0) return 1.0;

        return 1.0 - Math.pow(0.5, 1.0 / nChoose2);
    }
}