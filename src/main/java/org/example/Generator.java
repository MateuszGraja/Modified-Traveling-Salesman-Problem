package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Generator {
    public static void generateGraph(int n) {
        Random random = new Random();

        // Default settings
        int minX = 0, maxX = 5000;
        int minY = 0, maxY = 5000;

        // Set of used indexes to avoid repetitions
        Set<Integer> usedIndexes = new HashSet<>();

        try (FileWriter writer = new FileWriter("example.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            DataModel dataModel = new DataModel();
            dataModel.setNumberOfVertices(n);

            // Creating information about indexes for less than half of the vertices
            int numberOfIndexInfos = Math.max(1, n / 2);

            for (int i = 0; i < numberOfIndexInfos; i++) {
                int index;
                do {
                    index = random.nextInt(n); // Generating a random index
                } while (index == 0); // Repeat until index is different from 0

                if (!usedIndexes.contains(index)) {
                    IndexInfo indexInfo = new IndexInfo();
//                    indexInfo.setIndex(index);
//                    indexInfo.setIterationToUnlock(random.nextInt(n));
                    dataModel.getIndexInfos().add(indexInfo);

                    // Add the index to the set of used indexes
                    usedIndexes.add(index);
                }
            }

            // Creating information about vertices
            for (int i = 0; i < n; i++) {
                Vertex vertex = new Vertex();
                vertex.setIndex(i);
                vertex.setX(random.nextInt(maxX - minX + 1) + minX);
                vertex.setY(random.nextInt(maxY - minY + 1) + minY);

                dataModel.getVertices().add(vertex);
            }

            gson.toJson(dataModel, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
