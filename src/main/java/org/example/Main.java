package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<List<Integer>> antBest = new ArrayList<>();
        List<Long> antTimeList = new ArrayList<>();
        List<List<Integer>> bruteForceBest = new ArrayList<>();
        List<Long> bruteForceTimeList = new ArrayList<>();

        for (int n = 11; n < 12; n++) {
            Generator.generateGraph(n);

            if (n < 12) {
                long startTime = System.currentTimeMillis();
                List<Integer> optimalBruteForceRoute = BruteForce.FindBruteForceRoute();
                long endTime = System.currentTimeMillis();
                long bruteForceTime = endTime - startTime;

                bruteForceBest.add(optimalBruteForceRoute);
                bruteForceTimeList.add(bruteForceTime);
            }

            long startTime = System.currentTimeMillis();
            List<Integer> optimalAntRoute = AntColony.FindAntRoute();
            long endTime = System.currentTimeMillis();
            long antTime = endTime - startTime;

            antBest.add(optimalAntRoute);
            antTimeList.add(antTime);
        }

        // Print the results outside the loop
        System.out.println("Optimal ant routes: " + antBest);
        System.out.println("Execution ant times[ms]: " + antTimeList);
        System.out.println("Optimal bruteForce routes: " + bruteForceBest);
        System.out.println("Execution bruteForce times[ms]: " + bruteForceTimeList);

        // Write antTimeList to file
        writeTimeListToFile("antTimeList.txt", antTimeList, false);

        // Write bruteForceTimeList to file
        writeTimeListToFile("bruteForceTimeList.txt", bruteForceTimeList, false);
    }

    private static void writeTimeListToFile(String fileName, List<Long> timeList, boolean append) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, append))) {
            for (Long time : timeList) {
                writer.println(time);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
