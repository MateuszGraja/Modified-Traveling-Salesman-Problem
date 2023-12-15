package org.example;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Distances {
    public static List<List<Double>> getDistanceTable(String filePath) {
        List<List<Double>> distanceTable = new ArrayList<>();

        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            DataModel dataModel = gson.fromJson(reader, DataModel.class);

            int n = dataModel.getNumberOfVertices();

            for (int i = 0; i < n; ++i) {
                List<Double> row = new ArrayList<>();
                for (int j = 0; j < n; ++j) {
                    row.add(0.0);
                }
                distanceTable.add(row);
            }

            List<Vertex> nodesTable = dataModel.getVertices();

            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (i == j) {
                        distanceTable.get(i).set(j, 0.0);
                    } else {
                        double distance = Math.sqrt(
                                Math.pow(nodesTable.get(i).getX() - nodesTable.get(j).getX(), 2.0) +
                                        Math.pow(nodesTable.get(i).getY() - nodesTable.get(j).getY(), 2.0)
                        );
                        distanceTable.get(i).set(j, distance);
                        distanceTable.get(j).set(i, distance);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return distanceTable;
    }
    public static List<IndexInfo> getIndexInfos(String filePath) {
        List<IndexInfo> indexInfos = new ArrayList<>();

        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            DataModel dataModel = gson.fromJson(reader, DataModel.class);

            indexInfos.addAll(dataModel.getIndexInfos());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexInfos;
    }
}
