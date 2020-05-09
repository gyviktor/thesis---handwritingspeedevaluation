package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class WindowElements {

	public static GridPane createInfoGridPane() {

		GridPane infoGridPane = new GridPane();
		RowConstraints row = new RowConstraints();
		row.setPercentHeight(15);
		ColumnConstraints column = new ColumnConstraints();
		column.setPercentWidth(15);
		infoGridPane.getRowConstraints().addAll(row, row, row, row, row);
		infoGridPane.getColumnConstraints().addAll(column, column, column, column, column, column);
		infoGridPane.setStyle("-fx-font-size: 14");
		
		infoGridPane.add(new Text("Sebesség"), 1, 0);
		infoGridPane.add(new Text("Átlag"), 1, 1);
		infoGridPane.add(new Text("Maximum"), 2, 1);
		infoGridPane.add(new Text("Globális"), 0, 2);
		infoGridPane.add(new Text("Horizontális"), 0, 3);
		infoGridPane.add(new Text("Vertikális"), 0, 4);
		
		infoGridPane.add(new Text("Gyorsulás"), 3, 0);
		infoGridPane.add(new Text("Átlag"), 3, 1);
		infoGridPane.add(new Text("Maximum"), 4, 1);
		
		infoGridPane.add(new Text("Nyomás"), 5, 0);
		infoGridPane.add(new Text("Átlag"), 5, 1);

		return infoGridPane;
	}
	
	public static void infoTabla(GridPane infoGridPane, Szoveg szoveg) {
		
		int tempo = (int) Math.floor(Double.parseDouble((szoveg.getGlobalisSebesseg()))*5);
		Color[] colors = SzinSkala.intervalColors(0, 120, 60); // green to red
		String szinKod = colors[tempo].toString().substring(colors[tempo].toString().length() - 8,
				colors[tempo].toString().length() - 2);
		TextFlow txt = new TextFlow();
		txt.setStyle("-fx-background-color: #" + szinKod + ";");
		txt.getChildren().add(new Text(szoveg.getGlobalisSebesseg()));

		tempo = (int) Math.floor(Double.parseDouble((szoveg.getGlobalisGyorsulas()))*5);
		if(tempo < 0)tempo = 0;
		colors = SzinSkala.intervalColors(0, 120, 60); // green to red
		szinKod = colors[tempo].toString().substring(colors[tempo].toString().length() - 8,
				colors[tempo].toString().length() - 2);
		TextFlow txt2 = new TextFlow();
		txt2.setStyle("-fx-background-color: #00FF00");
		txt2.setTextAlignment(TextAlignment.CENTER);
		txt2.getChildren().add(new Text(szoveg.getGlobalisGyorsulas()));		
		

		//Pane sebessegAtlagGlobal = new Pane(txt);
		Pane sebessegMaxGlobal = new Pane();
		Pane sebessegAtlagH = new Pane();
		Pane sebessegMaxH = new Pane();
		Pane sebessegAtlagV = new Pane();
		Pane sebessegMaxV = new Pane();
		//Pane gyorsulasAtlagGlobal = new Pane(txt2);
		Pane gyorsulasMaxGlobal = new Pane();
		Pane gyorsulasAtlagH = new Pane();
		Pane gyorsulasMaxH = new Pane();
		Pane gyorsulasAtlagV = new Pane();
		Pane gyorsulasMaxV = new Pane();
		Pane nyomasAtlag = new Pane();

		infoGridPane.add(txt, 1, 2);
		infoGridPane.add(sebessegAtlagH, 1, 3);
		infoGridPane.add(sebessegAtlagV, 1, 4);
		infoGridPane.add(sebessegMaxGlobal, 2, 2);
		infoGridPane.add(sebessegMaxH, 2, 3);
		infoGridPane.add(sebessegMaxV, 2, 4);
		
		infoGridPane.add(txt2, 3, 2);
		infoGridPane.add(gyorsulasAtlagH, 3, 3);
		infoGridPane.add(gyorsulasAtlagV, 3, 4);
		infoGridPane.add(gyorsulasMaxGlobal, 4, 2);
		infoGridPane.add(gyorsulasMaxH, 4, 3);
		infoGridPane.add(gyorsulasMaxV, 4, 4);		
		
		infoGridPane.add(nyomasAtlag, 5, 2);
	}
	
	public static VBox createButtons() {
		VBox vbox = new VBox();
		vbox.setSpacing(50);
		vbox.setPrefWidth(200);
		vbox.setMaxWidth(200);
		vbox.setPadding(new Insets(10));
		
		return vbox;
	}

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

	public static Slider createFeaturesSlider() {
		Slider sliderFeatures = new Slider();
		sliderFeatures.setMin(0);
		sliderFeatures.setMax(2000);
		sliderFeatures.setValue(1000);
		sliderFeatures.setShowTickLabels(true);
		sliderFeatures.setShowTickMarks(true);
		sliderFeatures.setMajorTickUnit(100);
		sliderFeatures.setMinorTickCount(5);
		sliderFeatures.setBlockIncrement(100);
		sliderFeatures.setSnapToTicks(true);
		sliderFeatures.setDisable(true);
		return sliderFeatures;

	}

	public static Slider createTopMatchesSlider() {
		Slider sliderTopMatches = new Slider();
		sliderTopMatches.setMin(1);
		sliderTopMatches.setMax(25);
		sliderTopMatches.setValue(15);
		sliderTopMatches.setShowTickLabels(true);
		sliderTopMatches.setShowTickMarks(true);
		sliderTopMatches.setMajorTickUnit(5);
		sliderTopMatches.setMinorTickCount(1);
		sliderTopMatches.setBlockIncrement(5);
		sliderTopMatches.setDisable(true);
		return sliderTopMatches;
	}

	public static void CreateStage(GridPane gridpane, ScrollPane scrollPaneOnline, ScrollPane scrollPaneOffline,
			GridPane infoGridPane, VBox root, Scene scene, Stage primaryStage) {
		gridpane.add(scrollPaneOnline, 0, 0);
		gridpane.add(scrollPaneOffline, 1, 0);
		//gridpane.add(infoGridPane, 1, 1);
		gridpane.prefHeightProperty().bind(root.heightProperty());
		root.getChildren().addAll(gridpane);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}

}
