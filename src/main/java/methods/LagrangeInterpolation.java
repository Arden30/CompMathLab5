package methods;

import model.Dot;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
public class LagrangeInterpolation implements Interpolation {
    @Override
    public String name() {
        return "Интерполяция полиномом Лагранжа";
    }

    @Override
    public List<Double> interpolationPolynomial(List<Dot> dots, double x) {
        List<Double> interpolationPolynomial = new ArrayList<>();
        for (int i = 0; i < dots.size(); i++) {
            interpolationPolynomial.add(0d);
        }

        for (int i = 0; i < dots.size(); i++) {
            int tmp;
            int cnt = i;
            List<Double> dotsForIteration = new ArrayList<>();
            for (int j = 1; j < dots.size(); j++) {
                if (cnt-- > 0) {
                    tmp = 1;
                } else tmp = 0;

                dotsForIteration.add(dots.get(j - tmp).x());
            }
            List<Double> coefficients = countCoefficients(dotsForIteration);

            double denominator = countDenominator(dots.get(i).x(), dots, i);
            for (int j = 0; j < dots.size(); j++) {
                interpolationPolynomial.set(j, dots.get(i).y() / denominator * coefficients.get(j) + interpolationPolynomial.get(j));
            }
        }

        return interpolationPolynomial;
    }

    private double countDenominator(double x, List<Dot> dots, int cnt) {
        int tmp;
        double denominator = 1;
        for (int j = 1; j < dots.size(); j++) {
            if (cnt-- > 0) {
                tmp = 1;
            } else tmp = 0;

            denominator *= x - dots.get(j - tmp).x();
        }

        return denominator;
    }
    private List<Double> countCoefficients(List<Double> dots) {
        double[] firstCoefficients = {-dots.get(0), 1};
        PolynomialFunction polynomialA = new PolynomialFunction(firstCoefficients);
        for (int i = 1; i < dots.size(); i++) {
            double[] coefficients = {-dots.get(i), 1};
            PolynomialFunction polynomialB = new PolynomialFunction(coefficients);
            polynomialA = polynomialA.multiply(polynomialB);
        }

        return Arrays.stream(polynomialA.getCoefficients())
                .boxed()
                .collect(Collectors.toList());
    }
}
