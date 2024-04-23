package utils;

import methods.Interpolation;
import model.Dot;

import java.util.List;

public class PrettyPrinter {
    public static void printString(String s) {
        System.out.println(s);
    }

    public static void printTableOfDifferences(List<Dot> dots, List<List<Double>> columns) {
        StringBuilder header = new StringBuilder();
        header.append("Таблица конечных разностей").append("\n");
        header.append("№").append(String.format("%" + 13 + "s", "")).append("xi").append(String.format("%" + 15 + "s", "")).append("yi").append(String.format("%" + 14 + "s", "")).append("d_yi").append(String.format("%" + 11 + "s", ""));
        for (int i = 2; i < dots.size(); i++) {
            header.append("d_yi^").append(i).append(String.format("%" + (10 + i) + "s", ""));
        }
        header.append("\n");

        StringBuilder rows = new StringBuilder();
        for (int i = 0; i < dots.size() - 1; i++) {
            Dot dot = dots.get(i);
            rows.append(i).append(String.format("%" + 13 + "s", "")).append(String.format("%.2f", dot.x())).append(String.format("%" + 13 + "s", "")).append(String.format("%.2f", dot.y())).append(String.format("%" + 13 + "s", ""));
            for (int j = 0; j < columns.get(i).size(); j++) {
                rows.append(String.format("%.2f", columns.get(j).get(i))).append(String.format("%" + 13 + "s", ""));
            }
            rows.append("\n");
        }

        printString(header.append(rows).toString());
    }

    public static void printResult(double dot, List<Dot> dots, List<Interpolation> interpolations) {
        int cnt = 1;
        for (Interpolation interpolation: interpolations) {
            List<Double> interpolationPolynomial = interpolation.interpolationPolynomial(dots, dot);
            if (interpolationPolynomial == null) {
                printString("Узлы находятся не на равном расстоянии или их четное количество, интерполяция методом Гаусса невозможна");
            } else {
                printString(cnt++ + ". " + interpolation.name());
                printString("Интерполяционный многочлен: " + interpolation.polynomial(interpolationPolynomial));
                printString("Значение в точке " + dot + " равно " + String.format("%.5f", interpolation.value(interpolationPolynomial, dot)) + "\n");
            }
        }
    }
}
