package methods;

import model.Dot;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NewtonInterpolation implements Interpolation {
    @Override
    public String name() {
        return "Интерполяция полиномом Ньютона";
    }

    @Override
    public List<Double> interpolationPolynomial(List<Dot> dots, double x) {
        List<Double> values = new ArrayList<>();
        List<Double> coefficients = new ArrayList<>();

        for (Dot dot : dots) {
            values.add(dot.y());
        }

        int i = 0;
        for (int j = dots.size() - 1; j > 0; j--) {
            for (int k = 1; k <= j; k++) {
                values.set(k - 1, countF(values.get(k), values.get(k - 1), dots.get(k + i).x(), dots.get(k - 1).x()));
                if (k == 1) {
                    coefficients.add(values.get(0));
                }
            }
            i++;
        }

        List<Double> result = countCoefficients(dots, coefficients);
        while (result.size() != dots.size()) {
            result.add(0d);
        }

        return result;
    }

    private double countF(double f1, double f2, double x1, double x2) {
        return (f1 - f2) / (x1 - x2);
    }

    private List<Double> countCoefficients(List<Dot> dots, List<Double> coefficients) {
        double[] firstCoefficient = new double[dots.size()];
        firstCoefficient[0] = dots.get(0).y();
        for (int i = 1; i < firstCoefficient.length; i++) {
            firstCoefficient[i] = 0;
        }
        PolynomialFunction resultPolynomial = new PolynomialFunction(firstCoefficient);
        PolynomialFunction brackets = new PolynomialFunction(new double[]{1});

        for (int i = 0; i < dots.size() - 1; i++) {
            double[] polynomialCoefficients = {-dots.get(i).x(), 1};
            PolynomialFunction polynomialB = new PolynomialFunction(polynomialCoefficients);
            brackets = brackets.multiply(polynomialB);

            double[] coeff = {coefficients.get(i)};
            polynomialB = brackets.multiply(new PolynomialFunction(coeff));

            resultPolynomial = resultPolynomial.add(polynomialB);
        }

        return Arrays.stream(resultPolynomial.getCoefficients())
                .boxed()
                .collect(Collectors.toList());
    }
}
