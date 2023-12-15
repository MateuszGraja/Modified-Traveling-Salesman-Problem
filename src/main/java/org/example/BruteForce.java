package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class BruteForce {

    public static List<Integer> FindBruteForceRoute() {
        // Load distance and time tables from example.json
        List<List<Double>> timeTable = Times.getTimeTable();

        List<IndexInfo> indexInfos = Distances.getIndexInfos("example.json");

        // Get the number of vertices
        int n = timeTable.size();

        // Generate a list of indices representing cities (excluding the starting city)
        List<Integer> cityIndices = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            cityIndices.add(i);
        }

        // Generate all possible permutations of city indices
        List<List<Integer>> permutations = generatePermutations(cityIndices);

        // Calculate total distance for each permutation and find the optimal route
        List<Integer> optimalRoute = null;
        double minTotalDistance = Double.MAX_VALUE;

        for (List<Integer> permutation : permutations) {
            // Add the starting and ending city (0) to the permutation
            permutation.add(0, 0);
            permutation.add(0);

            // Check constraints
            if (checkConstraints(permutation, indexInfos)) {
                double totalDistance = calculateTotalTime(permutation, timeTable);

                if (totalDistance < minTotalDistance) {
                    minTotalDistance = totalDistance;
                    optimalRoute = new ArrayList<>(permutation);
                }
            }
        }
//        System.out.println("\nOptimal route: " + optimalRoute);
//        System.out.println("Optimal time: " + minTotalDistance);
        return optimalRoute;
    }

    private static List<List<Integer>> generatePermutations(List<Integer> elements) {
        List<List<Integer>> permutations = new ArrayList<>();
        generatePermutationsHelper(elements, 0, permutations);
        return permutations;
    }

    private static void generatePermutationsHelper(List<Integer> elements, int index, List<List<Integer>> permutations) {
        if (index == elements.size() - 1) {
            permutations.add(new ArrayList<>(elements));
            return;
        }

        for (int i = index; i < elements.size(); i++) {
            Collections.swap(elements, index, i);
            generatePermutationsHelper(elements, index + 1, permutations);
            Collections.swap(elements, index, i);
        }
    }

    private static double calculateTotalTime(List<Integer> route, List<List<Double>> timeTable) {
        double totalTime = 0;

        for (int i = 0; i < route.size() - 1; i++) {
            int currentVertex = route.get(i);
            int nextVertex = route.get(i + 1);
            totalTime += timeTable.get(currentVertex).get(nextVertex);
        }

        return totalTime;
    }


    private static boolean checkConstraints(List<Integer> solution, List<IndexInfo> indexInfos) {
        for (IndexInfo indexInfo : indexInfos) {
            int vertexIndex = indexInfo.getIndex();
            int requiredIterations = indexInfo.getIterationToUnlock();
            int indexInSolution = solution.indexOf(vertexIndex);

            if (indexInSolution == -1 || indexInSolution < requiredIterations) {
                return false;
            }
        }
        return true;
    }
}
