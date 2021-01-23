package application;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jfxutils.chart.AxisConstraint;
import jfxutils.chart.AxisConstraintStrategies;
import jfxutils.chart.ChartPanManager;
import jfxutils.chart.ChartZoomManager;

public class Gorbe {
	private StackPane stackPane;
	private LineChart<Number, Number> linechart;
	private NumberAxis xAxis;
	private NumberAxis yAxis;
	private Rectangle rectangle;
	private ChartPanManager panner;
	private ChartZoomManager zoomer;

	// gorbe letrehozasa
	public Gorbe(String nev, String xLabel, String yLabel) {
		stackPane = new StackPane();
		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		xAxis.setLabel(xLabel);
		yAxis.setLabel(yLabel);
		linechart = new LineChart<Number, Number>(xAxis, yAxis);
		linechart.setTitle(nev);
		linechart.setCreateSymbols(false);
		linechart.setLegendVisible(false);
		rectangle = new Rectangle();
		rectangle.setVisible(false);
		rectangle.setMouseTransparent(false);

		//pasztazo hozzaadasa grafikonhoz
		panner = new ChartPanManager(linechart);
		panner.setMouseFilter(mouseEvent -> {
			if (mouseEvent.getButton() == MouseButton.PRIMARY) {
				// let it through
			} else {
				mouseEvent.consume();
			}
		});
		panner.start();

		//zoom hozzaadasa grafikonhoz
		zoomer = new ChartZoomManager(stackPane, rectangle, linechart);
		zoomer.setMouseFilter(mouseEvent -> {
			mouseEvent.consume();
		});
		zoomer.setMouseWheelZoomAllowed(true);
		zoomer.setMouseWheelAxisConstraintStrategy(AxisConstraintStrategies.getFixed(AxisConstraint.Horizontal));
		zoomer.start();
		stackPane.getChildren().add(rectangle);
	}

	public void GorbeRajz(Szoveg szoveg, String gorbe) {
		//gorbe adatsorozat felvetele
		for (int i = 0; i < szoveg.getVonalak().size(); i++) {
			Series<Number, Number> sorozat = new Series<Number, Number>();
			switch (gorbe) {
			case "sebessegG": {
				sorozat = szoveg.getVonalak().get(i).getSebessegSeries();
				break;
			}
			case "sebessegH": {
				sorozat = szoveg.getVonalak().get(i).getSebessegSeriesH();
				break;
			}
			case "sebessegV": {
				sorozat = szoveg.getVonalak().get(i).getSebessegSeriesV();
				break;
			}
			case "gyorsulasG": {
				sorozat = szoveg.getVonalak().get(i).getGyorsulasSeries();
				break;
			}
			case "gyorsulasH": {
				sorozat = szoveg.getVonalak().get(i).getGyorsulasSeriesH();
				break;
			}
			case "gyorsulasV": {
				sorozat = szoveg.getVonalak().get(i).getGyorsulasSeriesV();
				break;
			}
			case "xGorbe": {
				sorozat = szoveg.getVonalak().get(i).getxSeries();
				break;
			}
			case "yGorbe": {
				sorozat = szoveg.getVonalak().get(i).getySeries();
				break;
			}
			case "nyomasG": {
				sorozat = szoveg.getVonalak().get(i).getNyomasSeriesG();
				break;
			}
			}

			linechart.getData().add(sorozat);
			Node vonalNode = sorozat.getNode().lookup(".chart-series-line");

			//gorbe szinezese
			if (szoveg.getVonalak().get(i).isTollFent() == true) {
				vonalNode.setStyle("-fx-stroke: #AAAAAA;");
			} else {
				Color[] colors = SzinSkala.intervalColors(0, 120, 60); // green to red
				int tempo = 0;
				switch (gorbe) {
				case "sebessegG": {
					tempo = (int) Math.floor(szoveg.getVonalak().get(i).getVonalSebessegAtlagG() * 20 - 10);
					break;
				}
				case "sebessegH": {
					tempo = (int) Math.floor(szoveg.getVonalak().get(i).getVonalSebessegH() * 30);
					break;
				}
				case "sebessegV": {
					tempo = (int) Math.floor(szoveg.getVonalak().get(i).getVonalSebessegV() * 20);
					break;
				}
				case "gyorsulasG": {
					tempo = (int) Math.floor(szoveg.getVonalak().get(i).getVonalSebessegAtlagG()* 20 - 10);
					break;
				}
				case "gyorsulasH": {
					tempo = (int) Math.floor(szoveg.getVonalak().get(i).getVonalSebessegH() * 30);
					break;
				}
				case "gyorsulasV": {
					tempo = (int) Math.floor(szoveg.getVonalak().get(i).getVonalSebessegV() * 20);
					break;
				}
				case "xGorbe": {
					tempo = (int) Math.floor(szoveg.getVonalak().get(i).getVonalSebessegAtlagG()* 20 - 10);
					break;
				}
				case "yGorbe": {
					tempo = (int) Math.floor(szoveg.getVonalak().get(i).getVonalSebessegAtlagG()* 20 - 10);
					break;
				}
				case "nyomasG": {
					tempo = (int) Math.floor((szoveg.getVonalak().get(i).getVonalNyomas() - 5000) / 220);
					break;
				}
				}
				if (tempo >= colors.length) {
					tempo = colors.length - 1;
				}else if (tempo < 0) {
					tempo = 0;
				}
				String szinKod = colors[tempo].toString().substring(colors[tempo].toString().length() - 8,
						colors[tempo].toString().length() - 2);
				vonalNode.setStyle("-fx-stroke: #" + szinKod + ";");
			}
		}
		//gorbe hozzaadasa grafikonhoz
		stackPane.getChildren().add(linechart);
	}

	public StackPane getStackPane() {
		return stackPane;
	}
	
}
