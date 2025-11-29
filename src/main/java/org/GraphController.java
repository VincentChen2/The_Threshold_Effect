package org;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GraphController {
    private InputPanel inputPanel;
    private InfoPanel infoPanel;
    private StatusPanel statusPanel;
    private final List<Graph> generatedGraphs;
    private Graph currentGraph;

    public GraphController() {
        generatedGraphs = new ArrayList<>();
    }

    public void setInputPanel(InputPanel inputPanel) {
        this.inputPanel = inputPanel;
    }

    public void setInfoPanel(InfoPanel infoPanel) {
        this.infoPanel = infoPanel;
    }

    public void setStatusPanel(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
    }

    public InputPanel getInputPanel() {
        return inputPanel;
    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }

    public StatusPanel getStatusPanel() {
        return statusPanel;
    }

    public List<Graph> getGeneratedGraphs() {
        return generatedGraphs;
    }

    public Graph getCurrentGraph() {
        return currentGraph;
    }

    public void setCurrentGraph(Graph graph) {
        this.currentGraph = graph;
    }

    public void clearGeneratedGraphs() {
        generatedGraphs.clear();
    }

    public void addGraph(Graph graph) {
        generatedGraphs.add(graph);
    }

    public void generateGraphs() {
        if (inputPanel == null || statusPanel == null) return;

        inputPanel.setGenerateButtonEnabled(false);
        statusPanel.setProgress(0);
        statusPanel.setProgressVisible(true);
        clearGeneratedGraphs();

        generatePRangeGraphs();
    }

    private void generatePRangeGraphs() {
        double pStart = inputPanel.getPStart();
        double pEnd = inputPanel.getPEnd();
        int pSteps = inputPanel.getPSteps();
        int graphsPerP = inputPanel.getGraphsPerP();

        statusPanel.setStatus(String.format("Generating graphs for p range [%.3f, %.3f] with %d steps...",
                pStart, pEnd, pSteps));

        //Run graph generation in a separate thread to keep UI responsive
        SwingWorker<Void, GraphProgressUpdate> worker = new SwingWorker<Void, GraphProgressUpdate>() {
            @Override
            protected Void doInBackground() {
                int n = inputPanel.getNValue();
                int totalGraphs = pSteps * graphsPerP;
                double pStepSize = (pEnd - pStart) / (pSteps - 1);

                for (int pIndex = 0; pIndex < pSteps; pIndex++) {
                    double currentP = pStart + (pIndex * pStepSize);

                    for (int graphIndex = 0; graphIndex < graphsPerP; graphIndex++) {
                        final int currentPIndex = pIndex;
                        final int currentGraphIndex = graphIndex;
                        Graph graph = new Graph(n, currentP, progress -> {
                            String pProgress = String.format("p=%.3f (%d/%d): %s",
                                    currentP, currentGraphIndex + 1, graphsPerP, progress);
                            publish(new GraphProgressUpdate(pProgress,
                                    (currentPIndex * graphsPerP) + currentGraphIndex, totalGraphs));
                        });

                        addGraph(graph);

                        // Update overall progress
                        int overallProgress = (int) (((double) ((currentPIndex * graphsPerP) + currentGraphIndex + 1) / totalGraphs) * 100);
                        publish(new GraphProgressUpdate(null,
                                (currentPIndex * graphsPerP) + currentGraphIndex + 1, totalGraphs, overallProgress));
                    }
                }
                return null;
            }

            @Override
            protected void process(java.util.List<GraphProgressUpdate> chunks) {
                if (!chunks.isEmpty()) {
                    GraphProgressUpdate latestUpdate = chunks.getLast();

                    if (latestUpdate.message != null) {
                        statusPanel.setStatus(latestUpdate.message);
                    } else {
                        int totalGraphs = latestUpdate.totalGraphs;
                        int completed = latestUpdate.completedGraphs;
                        statusPanel.setStatus(String.format("Generated %d of %d graphs", completed, totalGraphs));
                    }

                    if (latestUpdate.overallProgress >= 0) {
                        statusPanel.setProgress(latestUpdate.overallProgress);
                    }
                }
            }

            @Override
            protected void done() {
                try {
                    get();

                    if (getGeneratedGraphs().isEmpty()) {
                        statusPanel.setStatus("No graphs were generated");
                    } else {
                        setCurrentGraph(getGeneratedGraphs().getLast());

                        int totalGraphs = getGeneratedGraphs().size();
                        if (totalGraphs == 1) {
                            statusPanel.setStatus("Successfully generated 1 graph");
                        } else {
                            statusPanel.setStatus("Successfully generated " + totalGraphs + " graphs");
                        }
                        updateCharts();
                    }

                } catch (Exception e) {
                    statusPanel.setStatus("Error generating graphs: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    inputPanel.setGenerateButtonEnabled(true);
                    statusPanel.setProgressVisible(false);
                }
            }
        };

        worker.execute();
    }

    public void updateCharts() {
        if (infoPanel != null) {
            infoPanel.updateCharts();
        }
    }
}