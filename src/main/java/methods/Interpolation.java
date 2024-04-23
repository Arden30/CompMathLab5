package methods;

import model.Dot;

import java.util.List;

import static java.lang.Math.pow;

public interface Interpolation {
    String name();
    List<Double> interpolationPolynomial(List<Dot> dots, double x);

    default double value(List<Double> interpolationPolynomial, double x) {
        double val = 0;
        for (int i = 0; i < interpolationPolynomial.size(); i++) {
            val += interpolationPolynomial.get(i) * pow(x, i);
        }

        return val;
    }

    default String polynomial(List<Double> interpolationPolynomial) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%.5f",interpolationPolynomial.get(0)));

        for (int i = 1; i < interpolationPolynomial.size(); i++) {
            stringBuilder.append(getSign(interpolationPolynomial.get(i))).append(String.format("%.5f", interpolationPolynomial.get(i))).append("x^").append(i);
        }

        return stringBuilder.toString();
    }

    default String getSign(double b) {
        return b >= 0 ? "+": "";
    }
}
