package application;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WindowElements {

	public static GridPane createGridPane() {
		GridPane gridpane = new GridPane();
		RowConstraints row = new RowConstraints();
		row.setPercentHeight(50);
		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(50);
		gridpane.getRowConstraints().addAll(row, row);
		gridpane.getColumnConstraints().addAll(column, column);
		return gridpane;
	}

	public static ScrollPane createScrollPane() {
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.pannableProperty().set(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		return scrollPane;
	}

	public static LineChart<Number, Number> speedFunction() {
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		final LineChart<Number, Number> linechart = new LineChart<Number, Number>(xAxis, yAxis);
		linechart.setCreateSymbols(false);
		linechart.setLegendVisible(false);
		xAxis.setLabel("Idõ");
		yAxis.setLabel("Gyorsulás");
		linechart.setTitle("Gyorsulásfüggvény");
		return linechart;
	}

	public static void CreateStage(GridPane gridpane, ScrollPane scrollPaneOnline, ScrollPane scrollPaneOffline,
			StackPane stackPaneChart, Text text, VBox root, Scene scene, Stage primaryStage) {
		gridpane.add(scrollPaneOnline, 0, 0);
		gridpane.add(scrollPaneOffline, 1, 0);
		gridpane.add(stackPaneChart, 0, 1);
		gridpane.add(text, 1, 1);
		gridpane.prefHeightProperty().bind(root.heightProperty());
		root.getChildren().addAll(gridpane);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}

}
