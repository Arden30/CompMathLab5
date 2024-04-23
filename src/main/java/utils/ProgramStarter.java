package utils;

import methods.Interpolation;
import methods.InterpolationStorage;
import model.Dot;
import reader.ReadByFunction;
import reader.ReadDotsFromConsole;
import reader.ReadDotsFromFile;
import reader.Readable;

import javax.swing.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import static utils.PrettyPrinter.*;

public class ProgramStarter {
    private final Scanner scanner = new Scanner(System.in);
    private final List<Interpolation> interpolations = InterpolationStorage.getMethods();
    private final List<String> functions = List.of("sin(x)", "x^3");
    public void start() {
        while (true) {
            try {
                int formatInput = readFormatInput();

                Readable readable;
                int numOfFunc = 0;
                switch (formatInput) {
                    case 1 -> readable = new ReadDotsFromConsole();
                    case 2 -> readable = new ReadDotsFromFile(readPath());
                    case 3 -> {
                        numOfFunc = functionNumber();
                        readable = new ReadByFunction(numOfFunc);
                    }
                    default -> throw new Exception("Такого номера нет, попробуйте ещё раз");
                }

                List<Dot> dots = readable.read();
                double dot = readDot();
                List<List<Double>> table = TableOfFiniteDifferences.getTable(dots);
                printTableOfDifferences(dots, table);

                printResult(dot, dots, interpolations);

                int finalNumOfFunc = numOfFunc;
                SwingUtilities.invokeLater(() -> new ChartsBuilder(dot, dots, interpolations.subList(1, interpolations.size()), finalNumOfFunc));
            } catch (Exception e) {
                printString(e.getMessage());
                scanner.next();
            }
        }
    }

    private int readFormatInput() {
        while (true) {
            try {
                printString("Выберите формат ввода: консоль (введите 1) или файл (введите 2) или функцией (введите 3)");
                int format = scanner.nextInt();

                if (format != 1 && format != 2 && format != 3) {
                    throw new IllegalArgumentException("Такого номера нет, попробуйте ещё раз");
                }

                return format;
            } catch (IllegalArgumentException e) {
                printString(e.getMessage());
            } catch (Exception e) {
                printString("Ошибка ввода формата, попробуйте ещё раз");
                scanner.next();
            }
        }
    }

    private Path readPath() {
        while (true) {
            try {
                printString("Укажите путь к файлу: ");

                return Path.of(scanner.next());
            } catch (Exception e) {
                printString("Файла по заданному пути нет, попробуйте ещё раз");
            }
        }
    }

    private double readDot() {
        while (true) {
            try {
                printString("Введите точку, в которой хотите посчитать значение многочлена: ");

                return scanner.nextDouble();
            } catch (IllegalArgumentException e) {
                printString(e.getMessage());
            } catch (Exception e) {
                printString("Ошибка ввода формата, попробуйте ещё раз");
                scanner.next();
            }
        }
    }

    private int functionNumber() {
        while (true) {
            try {
                printString("Выберите одну из функций для интерполяции:");
                for (int i = 1; i <= functions.size(); i++) {
                    printString(i + ". " + functions.get(i - 1));
                }

                int num = scanner.nextInt();
                if (num < 1 || num > functions.size()) {
                    printString("Такой функции нет, попробуйте ещё раз");
                } else {
                    return num;
                }
            } catch (Exception e) {
                printString("Ошибка ввода номера функции, попробуйте ещё раз");
                scanner.next();
            }
        }
    }
}
