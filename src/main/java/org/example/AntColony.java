package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntColony {

    private final int numberOfAnts;
    private final double evaporationRate;
    private final double alpha;
    private final double beta;
    private final List<List<Double>> pheromones;
    private final List<List<Double>> times;
    private final List<IndexInfo> indexInfos;

    public AntColony(int numberOfAnts, double evaporationRate, double alpha, double beta, List<List<Double>> times, List<IndexInfo> indexInfos) {
        this.numberOfAnts = numberOfAnts;
        this.evaporationRate = evaporationRate;
        this.alpha = alpha;
        this.beta = beta;
        this.times = times;
        this.indexInfos = indexInfos;

        int n = times.size();

        // Initialize pheromone levels on each edge
        this.pheromones = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(1.0); // Initial pheromone level (can be adjusted)
            }
            this.pheromones.add(row);
        }
    }

    public List<Integer> findOptimalRoute() {
        List<Integer> optimalRoute = null;
        double minTotalTime = Double.MAX_VALUE;

        for (int iteration = 0; iteration < numberOfAnts; iteration++) {
            List<Integer> antRoute = generateAntRoute();
            int attempts = 0;

            // Try to generate new routes until you find a valid one or exceed the attempt limit
            while (antRoute == null || !checkConstraints(antRoute)) {
                antRoute = generateAntRoute();
                attempts++;

                // Set a limit on attempts to avoid an infinite loop
                if (attempts > 100000) {
//                    System.out.println("Exceeded the maximum number of attempts. Unable to find a valid route.");
                    break;
                }
            }

            // Check if the route satisfies constraints
            if (checkConstraints(antRoute)) {
                double totalTime = calculateTotalTime(antRoute);

                // Update pheromone levels
                updatePheromones(antRoute, totalTime);

                if (totalTime < minTotalTime) {
                    minTotalTime = totalTime;
                    optimalRoute = new ArrayList<>(antRoute);
                }
            } else {
//                System.out.println("Generated route does not satisfy constraints after " + attempts + " attempts.");
            }
        }

        return optimalRoute;
    }

    private List<Integer> generateAntRoute() {
        int n = times.size();
        List<Integer> route = new ArrayList<>();
        boolean[] visited = new boolean[n];
        int visitedCount = 0;

        // Choose a random starting point
        int currentVertex = new Random().nextInt(n);
        route.add(currentVertex);
        visited[currentVertex] = true;
        visitedCount++;

        // Generate the rest of the route
        while (visitedCount < n) {
            int nextVertex = chooseNextVertex(currentVertex, visited);
            if (nextVertex == -1) {
                return null; // No valid next vertex found
            }
            route.add(nextVertex);
            visited[nextVertex] = true;
            visitedCount++;
            currentVertex = nextVertex;
        }

        // Add the return to the starting node to form a cycle
        route.add(route.get(0));

        return route;
    }

    private int chooseNextVertex(int currentVertex, boolean[] visited) {
        int n = times.size();
        double[] probabilities = new double[n];
        double totalProbability = 0.0;

        // Calculate probabilities based on pheromone levels and times
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                double pheromone = Math.pow(pheromones.get(currentVertex).get(i), alpha);
                double time = Math.pow(1.0 / times.get(currentVertex).get(i), beta);
                probabilities[i] = pheromone * time;
                totalProbability += probabilities[i];
            }
        }

        // Choose the next vertex based on probabilities
        if (totalProbability > 0) {
            double randomValue = new Random().nextDouble() * totalProbability;
            double cumulativeProbability = 0.0;

            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    cumulativeProbability += probabilities[i];
                    if (cumulativeProbability >= randomValue) {
                        return i;
                    }
                }
            }
        }

        // Fallback (should not reach this point)
        return -1;
    }

    private double calculateTotalTime(List<Integer> route) {
        double totalTime = 0;

        for (int i = 0; i < route.size() - 1; i++) {
            int currentVertex = route.get(i);
            int nextVertex = route.get(i + 1);
            totalTime += times.get(currentVertex).get(nextVertex);
        }

        return totalTime;
    }

    private boolean checkConstraints(List<Integer> route) {
        int visitedCount = 0;
        boolean[] visited = new boolean[times.size()];

        for (int i = 0; i < route.size(); i++) {
            int vertex = route.get(i);

            // Check if the vertex satisfies the constraints
            for (IndexInfo indexInfo : indexInfos) {
                if (indexInfo.getIndex() == vertex) {
                    int requiredIterations = indexInfo.getIterationToUnlock();
                    if (visitedCount < requiredIterations) {
                        return false;
                    }
                }
            }

            visited[vertex] = true;
            visitedCount++;
        }

        return true;
    }

    private void updatePheromones(List<Integer> route, double totalTime) {
        // Evaporate existing pheromones
        for (int i = 0; i < pheromones.size(); i++) {
            for (int j = 0; j < pheromones.get(i).size(); j++) {
                pheromones.get(i).set(j, pheromones.get(i).get(j) * (1 - evaporationRate));
            }
        }

        // Update pheromones along the route
        for (int i = 0; i < route.size() - 1; i++) {
            int currentVertex = route.get(i);
            int nextVertex = route.get(i + 1);
            double pheromoneDeposit = 1.0 / totalTime;
            pheromones.get(currentVertex).set(nextVertex, pheromones.get(currentVertex).get(nextVertex) + pheromoneDeposit);
            pheromones.get(nextVertex).set(currentVertex, pheromones.get(nextVertex).get(currentVertex) + pheromoneDeposit);
        }
    }

    public static List<Integer> FindAntRoute() {
        // Load distance and time tables from example.json
        List<List<Double>> timeTable = Times.getTimeTable();
        List<IndexInfo> indexInfos = Distances.getIndexInfos("example.json"); // Using Distances for indexInfos (can be adjusted)

        // Set parameters for the Ant Colony Optimization algorithm
        int numberOfAnts = 100;
        double evaporationRate = 0.5;
        double alpha = 1.0;
        double beta = 2.0;

        // Create an instance of AntColony
        AntColony antColony = new AntColony(numberOfAnts, evaporationRate, alpha, beta, timeTable, indexInfos);

        // Find the optimal route
        List<Integer> optimalRoute = antColony.findOptimalRoute();

        // Print the results
//        System.out.println("Optimal route: " + optimalRoute);
//        System.out.println("Optimal time: " + antColony.calculateTotalTime(optimalRoute));

        return optimalRoute;
    }
}
