package utils;

import model.Dot;

import java.util.ArrayList;
import java.util.List;

public class TableOfFiniteDifferences {
    public static List<List<Double>> getTable(List<Dot> dots) {
        List<List<Double>> columns = new ArrayList<>();
        int numOfRows = dots.size() - 1;

        int size = numOfRows;
        while (size > 0) {
            columns.add(new ArrayList<>(--size));
        }

        for (int i = 1; i < dots.size(); i++) {
            columns.get(0).add(dots.get(i).y() - dots.get(i - 1).y());
        }

        for (int i = 1; i < numOfRows; i++) {
            List<Double> prevColumn = columns.get(i - 1);
            List<Double> currColumn = columns.get(i);
            for (int j = 1; j < prevColumn.size(); j++) {
                double diff = prevColumn.get(j) - prevColumn.get(j - 1);
                currColumn.add(diff);
            }
        }

        return columns;
    }
}
