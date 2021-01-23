package application;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;

public class WindowElements {

	//Gombok tárolója
	public static HBox createButtons() {
		HBox hbox = new HBox();
		hbox.setSpacing(50);
		hbox.setPrefWidth(500);
		hbox.setPadding(new Insets(10));
		return hbox;
	}

	//2x2-es racs felosztas
	public static GridPane createGridPane() {
		GridPane gridpane = new GridPane();
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(40);
		RowConstraints row2 = new RowConstraints();
		row2.setPercentHeight(60);
		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(50);
		gridpane.getRowConstraints().addAll(row1, row2);
		gridpane.getColumnConstraints().addAll(column, column);
		return gridpane;
	}

	//Gorgetheto, pasztazhato ScrollPane
	public static ScrollPane createScrollPane() {
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.pannableProperty().set(true);
		scrollPane.setFitToHeight(true);
		scrollPane.setFitToWidth(true);
		return scrollPane;
	}

}
