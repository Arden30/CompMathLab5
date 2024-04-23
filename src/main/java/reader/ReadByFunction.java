package reader;

import model.Dot;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.DoubleUnaryOperator;

import static utils.PrettyPrinter.printString;

public class ReadByFunction implements Readable {
    private final Scanner scanner = new Scanner(System.in);
    private final int numOfFunc;

    private final List<DoubleUnaryOperator> functionsList = List.of(Math::sin, v -> Math.pow(v, 3));
    public ReadByFunction(int numOfFunc) {
        this.numOfFunc = numOfFunc;
    }
    @Override
    public List<Dot> read() {
        double a = readLeftBoundary();
        double b = readRightBoundary(a);
        int n = readNumOfDots(a, b);

        return getDots(numOfFunc, a, b, n);
    }

    private List<Dot> getDots(int funcNum, double a, double b, int n) {
        List<Dot> dots = new ArrayList<>();
        double h = (b - a) / n;
        for (double x = a; x < b; x += h) {
            Dot dot = new Dot(x, functionsList.get(funcNum - 1).applyAsDouble(x));
            dots.add(dot);
        }

        return dots;
    }
    private double readLeftBoundary() {
        while (true) {
            try {
                printString("Введите левую границу интервала:");

                return scanner.nextDouble();
            } catch (Exception e) {
                printString("Ошибка ввода левой границы, попробуйте ещё раз");
                scanner.next();
            }
        }
    }

    private double readRightBoundary(double a) {
        while (true) {
            try {
                printString("Введите правую границу интервала:");
                double b = scanner.nextDouble();
                if (a >= b) {
                    printString("Правая граница должна быть больше левой границы, введите ещё раз");
                } else {
                    return b;
                }
            } catch (Exception e) {
                printString("Ошибка ввода правой границы, попробуйте ещё раз");
                scanner.next();
            }
        }
    }

    private int readNumOfDots(double a, double b) {
        while (true) {
            try {
                printString("Введите количество точек для интерполяции на интервале [" + a + ", " + b + "]: ");
                int num = scanner.nextInt();
                if (num <= 1) {
                    printString("Должно быть не меньше 2 точек");
                } else {
                    return num;
                }
            } catch (Exception e) {
                printString("Ошибка ввода количества точек, попробуйте ещё раз");
                scanner.next();
            }
        }
    }
}
