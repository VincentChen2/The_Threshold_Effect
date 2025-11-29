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

public abstract class AbstractChartPanel extends JPanel {
    protected final GraphController controller;
    protected ChartPanel chartPanel;
    protected JFreeChart chart;

    public AbstractChartPanel(GraphController controller) {
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
                getChartTitle(),
                getXAxisLabel(),
                getYAxisLabel(),
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
    }

    public void updateChart() {
        List<Graph> generatedGraphs = controller.getGeneratedGraphs();

        //Skip Chart Update if No Graphs
        if (generatedGraphs.isEmpty()) {
            return;
        }

        //Input Values from Graph Controller
        InputPanel inputPanel = controller.getInputPanel();
        int n = inputPanel.getNValue();
        double xAxisStart = inputPanel.getPStart();
        double xAxisEnd = inputPanel.getPEnd();

        //Group Graphs by p
        Map<Double, List<Graph>> graphsByP = new HashMap<>();
        for (Graph graph : generatedGraphs) {
            double p = graph.getProbability();
            graphsByP.computeIfAbsent(p, k -> new ArrayList<>()).add(graph);
        }

        //Calculate Proportions
        double[] pValues = new double[graphsByP.size()];
        double[] proportions = new double[graphsByP.size()];

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

            int countWithProperty = 0;
            for (Graph graph : graphs) {
                if (hasProperty(graph)) {
                    countWithProperty++;
                }
            }

            double proportion = (double) countWithProperty / graphs.size();
            pValues[index] = p;
            proportions[index] = proportion;

            //Scaling Vertical Axis
            if (proportion < minProportion) minProportion = proportion;
            if (proportion > maxProportion) maxProportion = proportion;

            index++;
        }

        //True Proportions Dataset
        XYSeries trueProportionsSeries = new XYSeries("True Proportion");
        for (int i = 0; i < pValues.length; i++) {
            trueProportionsSeries.add(pValues[i], proportions[i]);
        }

        XYSeriesCollection trueProportionsDataset = new XYSeriesCollection();
        trueProportionsDataset.addSeries(trueProportionsSeries);

        //Create Chart
        chart = ChartFactory.createXYLineChart(
                getChartTitle() + " for G(" + n + ", p)",
                getXAxisLabel(),
                getYAxisLabel(),
                trueProportionsDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        //CONFIGURING PLOT
        XYPlot plot = (XYPlot) chart.getPlot();

        //Add Theoretical Curve if Available
        XYSeries theoreticalSeries = createTheoreticalSeries(n, xAxisStart, xAxisEnd);
        if (theoreticalSeries != null && theoreticalSeries.getItemCount() > 0) {
            XYSeriesCollection theoreticalDataset = new XYSeriesCollection();
            theoreticalDataset.addSeries(theoreticalSeries);

            plot.setDataset(1, theoreticalDataset);
            XYLineAndShapeRenderer theoreticalRenderer = new XYLineAndShapeRenderer(true, false);
            theoreticalRenderer.setSeriesPaint(0, Color.RED);
            plot.setRenderer(1, theoreticalRenderer);
        }

        //Plot Data Points of True Proportions
        XYLineAndShapeRenderer TrueRenderer = new XYLineAndShapeRenderer(false, true);
        TrueRenderer.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));
        TrueRenderer.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(0, TrueRenderer);

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

    //Abstract Methods
    protected abstract String getChartTitle();
    protected abstract String getXAxisLabel();
    protected abstract String getYAxisLabel();
    protected abstract boolean hasProperty(Graph graph);

    //Optional Abstract Method (probably will make this mandatory later)
    protected XYSeries createTheoreticalSeries(int n, double xAxisStart, double xAxisEnd) {
        return null;
    }
}