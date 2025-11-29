package org;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EdgeChartPanel extends JPanel {
    private final GraphController controller;
    private ChartPanel chartPanel;
    private JFreeChart chart;

    public EdgeChartPanel(GraphController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        //Initialize Empty Chart
        chart = createEmptyChart();
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(chartPanel, BorderLayout.CENTER);
    }

    private JFreeChart createEmptyChart() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        return ChartFactory.createXYLineChart(
                "Edge Existence Histogram",
                "p value",
                "Proportion of Graphs with Edges",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    public void updateChart() {
        List<Graph> generatedGraphs = controller.getGeneratedGraphs();

        //Skip Graph Update if No Graphs
        if (generatedGraphs.isEmpty()) {
            return;
        }

        //Input Values from Graph Controller
        InputPanel inputPanel = controller.getInputPanel();
        int n = inputPanel.getNValue();

        //Group Graphs by p
        Map<Double, List<Graph>> graphsByP = new HashMap<>();
        for (Graph graph : generatedGraphs) {
            double p = graph.getProbability();
            graphsByP.computeIfAbsent(p, k -> new ArrayList<>()).add(graph);
        }

        //Calculates Proportions for Graph
        double[] pValues = new double[graphsByP.size()];
        double[] proportionsWithEdges = new double[graphsByP.size()];

        int index = 0;
        double minP = Double.MAX_VALUE;
        double maxP = Double.MIN_VALUE;
        double minProportion = Double.MAX_VALUE;
        double maxProportion = Double.MIN_VALUE;

        for (Map.Entry<Double, List<Graph>> entry : graphsByP.entrySet()) {
            double p = entry.getKey();
            List<Graph> graphs = entry.getValue();

            //Scale Horizontal Axis
            if (p < minP) minP = p;
            if (p > maxP) maxP = p;

            int countWithEdges = 0;
            for (Graph graph : graphs) {
                if (graph.hasAnyEdge()) {
                    countWithEdges++;
                }
            }

            double proportion = (double) countWithEdges / graphs.size();
            pValues[index] = p;
            proportionsWithEdges[index] = proportion;

            //Scaling Vertical Axis
            if (proportion < minProportion) minProportion = proportion;
            if (proportion > maxProportion) maxProportion = proportion;

            index++;
        }

        //True Proportions Dataset
        XYSeries trueProportionsSeries = new XYSeries("True Proportion");
        for (int i = 0; i < pValues.length; i++) {
            trueProportionsSeries.add(pValues[i], proportionsWithEdges[i]);
        }

        XYSeriesCollection trueProportionsDataset = new XYSeriesCollection();
        trueProportionsDataset.addSeries(trueProportionsSeries);

        //Theoretical Probability Curve
        double nChoose2 = (double) (n * (n - 1)) / 2;
        XYSeries theoreticalSeries = new XYSeries("Theoretical Probability");

        //Sets Range of Probability Curve
        //TODO: Fix range offset
        double xAxisStart = inputPanel.getPStart();
        double xAxisEnd = inputPanel.getPEnd();

        //FIXME: Fix this padding
        double theoryPadding = (xAxisEnd - xAxisStart) * 0.05;
        double theoryStart = Math.max(0, xAxisStart - theoryPadding);
        double theoryEnd = Math.min(1.0, xAxisEnd + theoryPadding);

        for (double p = theoryStart; p <= theoryEnd; p += 0.001) {
            double probability = 1 - Math.pow(1 - p, nChoose2);
            theoreticalSeries.add(p, probability);

            if (probability < minProportion) minProportion = probability;
            if (probability > maxProportion) maxProportion = probability;
        }

        XYSeriesCollection theoreticalDataset = new XYSeriesCollection();
        theoreticalDataset.addSeries(theoreticalSeries);

        //Creates Chart
        chart = ChartFactory.createXYLineChart(
                "Edge Existence for G(" + n + ", p)",
                "p value",
                "Proportion of Graphs with Edges",
                trueProportionsDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        //CONFIGURING PLOT
        XYPlot plot = (XYPlot) chart.getPlot();

        //Theoretical Probability Curve
        plot.setDataset(1, theoreticalDataset);
        plot.setRenderer(1, new XYLineAndShapeRenderer(true, false));
        plot.getRenderer(1).setSeriesPaint(0, Color.RED);

        //Plot Data Points of True Proportions
        XYLineAndShapeRenderer empiricalRenderer = new XYLineAndShapeRenderer(false, true);
        empiricalRenderer.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));
        empiricalRenderer.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(0, empiricalRenderer);

        //Horizontal Axis Scaling
        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        double xPadding = (xAxisEnd - xAxisStart) * 0.05;
        xAxis.setRange(xAxisStart - xPadding, xAxisEnd + xPadding);

        //Vertical Axis Scaling
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        double yPadding = (maxProportion - minProportion) * 0.1;
        double yMin = Math.max(0, minProportion - yPadding);
        double yMax = Math.min(1.0, maxProportion + yPadding);

        if (yMin == yMax) {
            yMin = Math.max(0, yMin - 0.05);
            yMax = Math.min(1.0, yMax + 0.05);
        }

        yAxis.setRange(yMin, yMax);

        chartPanel.setChart(chart);
    }
}