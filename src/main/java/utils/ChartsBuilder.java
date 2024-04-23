package utils;

import methods.Interpolation;
import model.Dot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class ChartsBuilder extends JFrame {

    public ChartsBuilder(double dot, List<Dot> dots, List<Interpolation> interpolations, int numOfFunc) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        int sizeOfSeries = 0;
        XYSeriesCollection dataset = new XYSeriesCollection();

        List<String> functions = List.of("sin(x)", "x^3");
        if (numOfFunc != 0) {
            XYSeries functionSeries = new XYSeries(functions.get(numOfFunc - 1));
            for (double x = -15.0; x <= 15.0; x += 0.1) {
                List<DoubleUnaryOperator> functionsList = List.of(Math::sin, v -> Math.pow(v, 3));
                double y = functionsList.get(numOfFunc - 1).applyAsDouble(x);
                functionSeries.add(x, y);
            }
            sizeOfSeries++;
            dataset.addSeries(functionSeries);
        }

        for (Interpolation interpolation: interpolations) {
            if (interpolation.interpolationPolynomial(dots, dot) != null) {
                XYSeries series = new XYSeries(interpolation.polynomial(interpolation.interpolationPolynomial(dots, dot)));
                for (double x = -15.0; x <= 15.0; x += 0.1) {
                    double y = interpolation.value(interpolation.interpolationPolynomial(dots, dot), x);
                    series.add(x, y);
                }
                try {
                    sizeOfSeries++;
                    dataset.addSeries(series);
                } catch (Exception e) {
                    sizeOfSeries--;
                }
            }
        }

        // Создание набора данных для точек
        XYSeries pointsSeries = new XYSeries("Точки");
        for (Dot d : dots) {
            pointsSeries.add(d.x(), d.y());
        }
        sizeOfSeries++;
        dataset.addSeries(pointsSeries);

        // Создание графика
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Графики аппроксимирующих функций", "X", "Y",
                dataset
        );

        chart.getXYPlot().getDomainAxis().setRange(-15, 15);
        chart.getXYPlot().getRangeAxis().setRange(-5, 25);

        NumberAxis xAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
        NumberAxis yAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();

        xAxis.setTickUnit(new NumberTickUnit(1));
        yAxis.setTickUnit(new NumberTickUnit(1));

        // Настройка стилей линий осей координат
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainZeroBaselineVisible(true);
        plot.setRangeZeroBaselineVisible(true);
        plot.setDomainZeroBaselinePaint(Color.BLACK); // черный цвет для вертикальной оси 0
        plot.setRangeZeroBaselinePaint(Color.BLACK); // черный цвет для горизонтальной оси 0

        // Устанавливаем толщину линии для функций
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setStroke(new BasicStroke(2)); // Установите желаемую толщину линии здесь

        // Настройка отображения точек
        renderer.setSeriesLinesVisible(sizeOfSeries - 1, false); // Отключаем соединение линиями для точек
        renderer.setSeriesShapesVisible(sizeOfSeries - 1, true); // Включаем отображение формы для точек
        renderer.setSeriesShape(sizeOfSeries - 1, new Ellipse2D.Double(-2, -2, 8, 8)); // Задаем форму точки (в данном случае - круг)
        renderer.setSeriesPaint(sizeOfSeries - 1, Color.RED); // Задаем цвет точек

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(1100, 630));
        add(chartPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
