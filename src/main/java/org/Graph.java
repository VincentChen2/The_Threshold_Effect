package org;

import java.util.*;
import java.util.function.Consumer;

public class Graph {
    private final int n;
    private final double p;
    private final Random random;
    private final Map<Integer, Set<Integer>> adjacencyList;
    private final Consumer<String> progressCallback;
    private boolean hasAnyEdge;

    public Graph(int n, double p, Consumer<String> progressCallback) {
        this.n = n;
        this.p = p;
        this.random = new Random();
        this.adjacencyList = new HashMap<>(n);
        this.progressCallback = progressCallback;
        this.hasAnyEdge = false;

        generateGraph();
    }

    private void generateGraph() {
        //Initialize all vertices
        for (int i = 0; i < n; i++) {
            adjacencyList.put(i, new HashSet<>());
        }

        //Generate edges
        for (int i = 0; i < n; i++) {
            if (progressCallback != null) {
                progressCallback.accept("Generating Graph... (" + (i + 1) + "/" + n + ") vertices completed");
            }

            for (int j = i + 1; j < n; j++) {
                if (random.nextDouble() < p) {
                    adjacencyList.get(i).add(j);
                    adjacencyList.get(j).add(i);
                    hasAnyEdge = true;
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

    public boolean hasAnyEdge() {
        return hasAnyEdge;
    }

    public boolean hasTriangle() {
        for (int u = 0; u < n; u++) {
            if (getDegree(u) < 2) continue;

            Set<Integer> neighborsU = getNeighbors(u);

            for (int v : neighborsU) {
                if (v <= u) continue;
                if (getDegree(v) < 2) continue;
                if (!Collections.disjoint(neighborsU, getNeighbors(v))) return true;
            }
        }
        return false;
    }

    public boolean hasK4() {
        for (int u = 0; u < n; u++) {
            if (getDegree(u) < 3) continue;

            Set<Integer> neighborsU = getNeighbors(u);

            for (int v : neighborsU) {
                if (v <= u) continue;
                if (getDegree(v) < 3) continue;

                Set<Integer> UnionUV = new HashSet<>(neighborsU);
                UnionUV.retainAll(getNeighbors(v));

                for (int w : UnionUV) {
                    if (w <= v) continue;
                    if (getDegree(w) < 3) continue;
                    if (!Collections.disjoint(UnionUV, getNeighbors(w))) return true;
                }
            }
        }
        return false;
    }

    public boolean isConnected() {
        if (n <= 1) return true;

        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();

        queue.add(0);
        visited[0] = true;
        int visitedCount = 1;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int neighbor : getNeighbors(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    visitedCount++;
                    queue.add(neighbor);
                }
            }
        }

        return visitedCount == n;
    }

    public boolean hasHamiltonianCycle() {
        if (n <= 2) return false;
        if (!isConnected()) return false;

        boolean hasMinimumDegree = true;
        for (int i = 0; i < n; i++) {
            if (getDegree(i) < n / 2) {
                hasMinimumDegree = false;
                break;
            }
        }
        if (hasMinimumDegree) return true;

        int[] path = new int[n];
        boolean[] visited = new boolean[n];

        path[0] = 0;
        visited[0] = true;

        return hasHamiltonianCycleHelper(path, visited, 1);
    }

    //TODO: Optimize this function (possibly with memoization?)
    private boolean hasHamiltonianCycleHelper(int[] path, boolean[] visited, int pos) {
        //Base Case: All Positions in Path are Filled
        if (pos == n) { return hasEdge(path[pos - 1], path[0]);}

        //Recursively find Hamiltonian Cycle if Possible
        for (int v : getNeighbors(path[pos - 1])) {
            if (!visited[v]) {
                visited[v] = true;
                path[pos] = v;

                if (hasHamiltonianCycleHelper(path, visited, pos + 1)) return true;

                // Backtrack
                visited[v] = false;
            }
        }

        return false;
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