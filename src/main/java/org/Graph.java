package org;

import java.util.*;
import java.util.function.Consumer;

public class Graph {
    private final int n;
    private final double p;
    private final Random random;
    private final Map<Integer, Set<Integer>> adjacencyList;
    private Consumer<String> progressCallback;

    public Graph(int n, double p, Consumer<String> progressCallback) {
        this.n = n;
        this.p = p;
        this.random = new Random();
        this.adjacencyList = new HashMap<>(n);
        this.progressCallback = progressCallback;

        generateGraph();
    }

    private void generateGraph() {
        // Initialize all vertices
        for (int i = 0; i < n; i++) {
            adjacencyList.put(i, new HashSet<>());
        }

        // Generate edges
        for (int i = 0; i < n; i++) {
            if (progressCallback != null) {
                progressCallback.accept("Generating Graph... (" + (i + 1) + "/" + n + ") vertices completed");
            }

            for (int j = i + 1; j < n; j++) {
                if (random.nextDouble() < p) {
                    adjacencyList.get(i).add(j);
                    adjacencyList.get(j).add(i);
                }
            }
        }

        if (progressCallback != null) {
            progressCallback.accept("Generated Graph");
        }
    }

    public boolean hasEdge(int u, int v) {
        return adjacencyList.get(u).contains(v);
    }

    public Set<Integer> getNeighbors(int vertex) {
        if (vertex < 0 || vertex >= n) {
            throw new IllegalArgumentException("Vertex index out of bounds: " + vertex);
        }

        return new HashSet<>(adjacencyList.get(vertex));
    }

    public int getVertexCount() {
        return n;
    }

    public double getProbability() {
        return p;
    }

    public int getEdgeCount() {
        int edges = 0;

        for (Set<Integer> neighbors : adjacencyList.values()) {
            edges += neighbors.size();
        }

        return edges / 2;
    }

    public int getDegree(int vertex) {
        return adjacencyList.get(vertex).size();
    }

    @Override
    public String toString() {
        return String.format("Graph[n=%d, p=%.3f, edges=%d]",
                n, p, getEdgeCount());
    }
}