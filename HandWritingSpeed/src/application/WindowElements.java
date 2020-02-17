package application;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class WindowElements {

	public static GridPane createGridPane(){
		GridPane gridpane = new GridPane();
		RowConstraints row = new RowConstraints();
		row.setPercentHeight(50);
		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(50);
		gridpane.getRowConstraints().addAll(row, row);
		gridpane.getColumnConstraints().addAll(column, column);
		return gridpane;		
	}
	
	public static ScrollPane createScrollPane(){
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.pannableProperty().set(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		return scrollPane;
		
	}
	
	public static LineChart<Number, Number> speedFunction(){
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		final LineChart<Number, Number> linechart = new LineChart<Number, Number>(xAxis, yAxis);
		linechart.setCreateSymbols(false);
		xAxis.setLabel("Idõ");
		yAxis.setLabel("Sebesség");		
		linechart.setTitle("Sebességfüggvény");
		return linechart;		
	}

}
