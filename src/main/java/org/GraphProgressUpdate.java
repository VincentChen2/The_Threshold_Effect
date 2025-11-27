package org;

public class GraphProgressUpdate {
    String message;
    int completedGraphs;
    int totalGraphs;
    int overallProgress;

    GraphProgressUpdate(String message, int completedGraphs, int totalGraphs) {
        this(message, completedGraphs, totalGraphs, -1);
    }

    GraphProgressUpdate(String message, int completedGraphs, int totalGraphs, int overallProgress) {
        this.message = message;
        this.completedGraphs = completedGraphs;
        this.totalGraphs = totalGraphs;
        this.overallProgress = overallProgress;
    }
}
