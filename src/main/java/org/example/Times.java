package org.example;

import java.util.ArrayList;
import java.util.List;

public class Times {
    public static  List<List<Double>> getTimeTable () {
        // create distances table with data from generator
        String filePath = "example.json";
        List<List<Double>> distanceTable = Distances.getDistanceTable(filePath);

        double accDec = 6.2; // acceleration / deceleration

        List<List<Double>> timeTable = new ArrayList<>();

        for (int i = 0; i < distanceTable.size(); i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < distanceTable.get(i).size(); j++) {
                double S = distanceTable.get(i).get(j);
                double endTime;

                if (S == 0.0) {
                    endTime = 0.0;
                } else {
                    double maxSpeed = calculateMaxSpeed(S);
                    double accelerationTime = maxSpeed / accDec;

                    endTime = calculateSpeedProfile(S, maxSpeed, accelerationTime);
                }
                row.add(endTime);
            }
            timeTable.add(row);
        }
        return timeTable;
    }

    private static double calculateMaxSpeed(double distance) {
        return Math.sqrt(distance);
    }

    private static double calculateSpeedProfile(double distance, double maxSpeed, double accelerationTime) {
        // return final time
        return accelerationTime + (distance - 2 * accelerationTime * maxSpeed) / maxSpeed + accelerationTime;
    }
}
