package methods;

import model.Dot;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import utils.TableOfFiniteDifferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class GaussInterpolation implements Interpolation {
    @Override
    public String name() {
        return "Интерполяция полиномом Гаусса";
    }

    @Override
    public List<Double> interpolationPolynomial(List<Dot> dots, double x) {
        if (!checkConstH(dots) || dots.size() % 2 == 0) {
            return null;
        }
        double middle = dots.get(dots.size() / 2).x();
        double h = dots.get(1).x() - dots.get(0).x();
        var infiniteDifferences = TableOfFiniteDifferences.getTable(dots);

        if (x > middle) {
            return firstFormula(dots, infiniteDifferences, middle, h);
        } else return secondFormula(dots, infiniteDifferences, middle, h);
    }

    private List<Double> firstFormula(List<Dot> dots, List<List<Double>> infiniteDifferences, double middle, double h) {
        List<Double> coefficients = new ArrayList<>();
        int num = dots.size() / 2;
        coefficients.add(dots.get(num).y());
        int tmp = 1;
        for (List<Double> column: infiniteDifferences) {
            if (tmp-- > 0) {
                coefficients.add(column.get(num));
            } else {
                num--;
                coefficients.add(column.get(num));
                tmp = 1;
            }
        }

        double[] firstCoefficient = new double[dots.size()];
        firstCoefficient[0] = coefficients.get(0);
        for (int i = 1; i < firstCoefficient.length; i++) {
            firstCoefficient[i] = 0;
        }
        PolynomialFunction resultPolynomial = new PolynomialFunction(firstCoefficient);
        PolynomialFunction brackets = new PolynomialFunction(new double[]{1});

        int parity = 0;
        for (int i = 1; i < coefficients.size(); i++) {
            double[] polynomialCoefficients = {parity - middle / h, 1 / h};

            if (i % 2 != 0) {
                parity = -1 * (parity + 1);
            } else parity *= -1;

            PolynomialFunction polynomialB = new PolynomialFunction(polynomialCoefficients);
            brackets = brackets.multiply(polynomialB);

            double[] coeff = {coefficients.get(i) / factorial(i)};
            polynomialB = brackets.multiply(new PolynomialFunction(coeff));

            resultPolynomial = resultPolynomial.add(polynomialB);
        }

        return Arrays.stream(resultPolynomial.getCoefficients())
                .boxed()
                .collect(Collectors.toList());
    }

    private List<Double> secondFormula(List<Dot> dots, List<List<Double>> infiniteDifferences, double middle, double h) {
        List<Double> coefficients = new ArrayList<>();
        int num = dots.size() / 2;
        coefficients.add(dots.get(num).y());
        int tmp = 2;
        for (List<Double> column: infiniteDifferences) {
            if (tmp-- > 0) {
                coefficients.add(column.get(num - 1));
            } else {
                num--;
                coefficients.add(column.get(num - 1));
                tmp = 2;
            }
        }

        double[] firstCoefficient = new double[dots.size()];
        firstCoefficient[0] = coefficients.get(0);
        for (int i = 1; i < firstCoefficient.length; i++) {
            firstCoefficient[i] = 0;
        }
        PolynomialFunction resultPolynomial = new PolynomialFunction(firstCoefficient);
        PolynomialFunction brackets = new PolynomialFunction(new double[]{1});

        int parity = 0;
        for (int i = 1; i < coefficients.size(); i++) {
            double[] polynomialCoefficients = {parity - middle / h, 1 / h};

            if (i % 2 != 0) {
                parity = abs(parity) + 1;
            } else parity *= -1;

            PolynomialFunction polynomialB = new PolynomialFunction(polynomialCoefficients);
            brackets = brackets.multiply(polynomialB);

            double[] coeff = {coefficients.get(i) / factorial(i)};
            polynomialB = brackets.multiply(new PolynomialFunction(coeff));

            resultPolynomial = resultPolynomial.add(polynomialB);
        }

        return Arrays.stream(resultPolynomial.getCoefficients())
                .boxed()
                .collect(Collectors.toList());
    }
    private boolean checkConstH(List<Dot> dots) {
        double h = dots.get(1).x() - dots.get(0).x();
        for (int i = 2; i < dots.size(); i++) {
            double diff = dots.get(i).x() - dots.get(i - 1).x() - h;
            if (diff > 0.000001) {
                return false;
            }
        }

        return true;
    }

    private double factorial(int n) {
        if (n == 1) {
            return 1;
        } else return n * factorial(n - 1);
    }
}
