package application;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jfxutils.chart.AxisConstraint;
import jfxutils.chart.AxisConstraintStrategies;
import jfxutils.chart.ChartPanManager;
import jfxutils.chart.ChartZoomManager;

public final class SebessegGyorsulasGorbe {
	
	public static void speedFunction(StackPane stackpane, LineChart<Number, Number> linechart) {
		stackpane.getChildren().clear();
		Rectangle rectangleSebesseg = new Rectangle();
		rectangleSebesseg.setVisible(false);
		rectangleSebesseg.setMouseTransparent(false);
		stackpane.getChildren().add(linechart);
		stackpane.getChildren().add(rectangleSebesseg);
		linechart.setCreateSymbols(false);
		linechart.setLegendVisible(false);
		ChartPanManager pannerSebesseg = new ChartPanManager(linechart);
		pannerSebesseg.setMouseFilter(mouseEvent -> {
			if (mouseEvent.getButton() == MouseButton.PRIMARY) {
				// let it through
			} else {
				mouseEvent.consume();
			}
		});
		pannerSebesseg.start();

		ChartZoomManager zoomerSebesseg = new ChartZoomManager(stackpane, rectangleSebesseg, linechart);
		zoomerSebesseg.setMouseFilter(mouseEvent -> {
			mouseEvent.consume();
		});
		zoomerSebesseg.setMouseWheelZoomAllowed(true);
		zoomerSebesseg.setMouseWheelAxisConstraintStrategy(AxisConstraintStrategies.getFixed(AxisConstraint.Horizontal));
		zoomerSebesseg.start();

	}

	public static void accFunction(StackPane stackpane, LineChart<Number, Number> linechart) {
		stackpane.getChildren().clear();
		Rectangle rectangleGyorsulas = new Rectangle();
		rectangleGyorsulas.setVisible(false);
		rectangleGyorsulas.setMouseTransparent(false);
		stackpane.getChildren().add(linechart);
		stackpane.getChildren().add(rectangleGyorsulas);
		linechart.setCreateSymbols(false);
		linechart.setLegendVisible(false);
		ChartPanManager pannerGyorsulas = new ChartPanManager(linechart);
		pannerGyorsulas.setMouseFilter(mouseEvent -> {
			if (mouseEvent.getButton() == MouseButton.PRIMARY) {
				// let it through
			} else {
				mouseEvent.consume();
			}
		});
		pannerGyorsulas.start();

		ChartZoomManager zoomerGyorsulas = new ChartZoomManager(stackpane, rectangleGyorsulas, linechart);
		zoomerGyorsulas.setMouseFilter(mouseEvent -> {
			mouseEvent.consume();
		});
		zoomerGyorsulas.setMouseWheelZoomAllowed(true);
		zoomerGyorsulas.setMouseWheelAxisConstraintStrategy(AxisConstraintStrategies.getFixed(AxisConstraint.Horizontal));
		zoomerGyorsulas.start();	

	}

	public static void sebessegGorbe(LineChart<Number, Number> linechart, Szoveg szoveg) {
		for (int i = 0; i < szoveg.getVonalak().size(); i++) {
			Series<Number, Number> sorozat = new Series<Number, Number>();
			sorozat.setName("Sebesség");
			sorozat = szoveg.getVonalak().get(i).getSebessegSeries();
			linechart.getData().add(sorozat);
			Node vonalNode = sorozat.getNode().lookup(".chart-series-line");

			if (szoveg.getVonalak().get(i).isTollFent() == true) {
				vonalNode.setStyle("-fx-stroke: #AAAAAA;");

			} else {
				Color[] colors = SzinSkala.intervalColors(0, 120, 60); // green to red
				int tempo = (int) Math.floor(szoveg.getVonalak().get(i).getVonalSebesseg() * 5);
				if (tempo >= colors.length) {
					tempo = colors.length - 1;
				}
				String szinKod = colors[tempo].toString().substring(colors[tempo].toString().length() - 8,
						colors[tempo].toString().length() - 2);
				vonalNode.setStyle("-fx-stroke: #" + szinKod + ";");
			}

		}

	}

	public static void gyorsulasGorbe(LineChart<Number, Number> linechart, Szoveg szoveg) {
		for (int i = 0; i < szoveg.getVonalak().size(); i++) {
			Series<Number, Number> sorozat = new Series<Number, Number>();
			sorozat = szoveg.getVonalak().get(i).getGyorsulasSeries();
			linechart.getData().add(sorozat);
			Node vonalNode = sorozat.getNode().lookup(".chart-series-line");

			if (szoveg.getVonalak().get(i).isTollFent() == true) {
				vonalNode.setStyle("-fx-stroke: #AAAAAA;");
			} else {				
				Color[] colors = SzinSkala.intervalColors(0, 120, 60); // green to red
				int tempo = (int) Math.floor(szoveg.getVonalak().get(i).getVonalSebesseg() * 5);
				if (tempo >= colors.length) {
					tempo = colors.length - 1;
				}
				String szinKod = colors[tempo].toString().substring(colors[tempo].toString().length()-8, colors[tempo].toString().length()-2);
				vonalNode.setStyle("-fx-stroke: #" + szinKod + ";");
			}

		}

	}

}
