package application;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		VBox root = new VBox();
		Scene scene = new Scene(root);
		MenuBar menubar = new MenuBar();
		Menu menu = new Menu("Menü");
		NumberAxis xAxisIdoSebesseg = new NumberAxis();
		NumberAxis xAxisIdoGyorsulas = new NumberAxis();
		NumberAxis yAxisSebesseg = new NumberAxis();
		NumberAxis yAxisGyorsulas = new NumberAxis();
		xAxisIdoSebesseg.setLabel("Idõ");
		xAxisIdoGyorsulas.setLabel("Idõ");
		yAxisSebesseg.setLabel("Sebesség");
		yAxisGyorsulas.setLabel("Gyorsulás");
		LineChart<Number, Number> linechartSebesseg = new LineChart<Number, Number>(xAxisIdoSebesseg, yAxisSebesseg);
		linechartSebesseg.setTitle("Sebességgörbe");
		LineChart<Number, Number> linechartGyorsulas = new LineChart<Number, Number>(xAxisIdoGyorsulas, yAxisGyorsulas);
		linechartGyorsulas.setTitle("Gyorsulásgörbe");
		StackPane stackPaneSebesseg = new StackPane();
		StackPane stackPaneGyorsulas = new StackPane();
		StackPane stackPaneKepek = new StackPane();
		ImageView bottom = new ImageView();
		ImageView topp = new ImageView();
		HBox jobbAlso = new HBox();
		jobbAlso.setSpacing(25);
		VBox gombok = WindowElements.createButtons();
		
		Slider sliderFeatures = WindowElements.createFeaturesSlider();
		Slider sliderTopMatches = WindowElements.createTopMatchesSlider();

		GridPane gridpane = WindowElements.createGridPane();
		GridPane infoGridPane = WindowElements.createInfoGridPane();
		ScrollPane scrollPaneOnline = WindowElements.createScrollPane();
		ScrollPane scrollPaneOffline = WindowElements.createScrollPane();
		ScrollPane scrollPaneOssz = WindowElements.createScrollPane();
		ImageView onlineImage = new ImageView();
		ImageView offlineImage = new ImageView();
		DoubleProperty zoomPropertyOffline = new SimpleDoubleProperty();
		DoubleProperty zoomPropertyOnline = new SimpleDoubleProperty();
		primaryStage.setTitle("Handwriting speed");
		MenuItem beolvasOnline = new MenuItem("Megnyitás online");
		MenuItem beolvasOffline = new MenuItem("Megnyitás offline");
		MenuItem visszaAllitas = new MenuItem("Visszaállítás");
		menu.getItems().addAll(beolvasOnline, beolvasOffline, visszaAllitas);
		menubar.getMenus().add(menu);
		root.getChildren().add(menubar);

		Button sebessegGorbeButton = new Button("Sebességgörbe");
		Button gyorsulasGorbeButton = new Button("Gyorsulásgörbe");
		Button illesztesButton = new Button("Egymásra illesztés");
		Button mutatButton = new Button("Sebesség elrejtése");
		sebessegGorbeButton.setDisable(true);
		gyorsulasGorbeButton.setDisable(true);
		illesztesButton.setDisable(true);
		mutatButton.setDisable(true);
		
		gombok.getChildren().addAll(sebessegGorbeButton, gyorsulasGorbeButton, illesztesButton, mutatButton, sliderFeatures, sliderTopMatches);

		
		try {

			sebessegGorbeButton.setOnAction(e -> {
				sebessegGorbeButton.setDisable(true);
				gyorsulasGorbeButton.setDisable(false);
				if(offlineImage.getImage() != null && onlineImage.getImage() != null)illesztesButton.setDisable(false);
				mutatButton.setDisable(true);
				sliderFeatures.setDisable(true);
				sliderTopMatches.setDisable(true);
				scrollPaneOssz.setContent(stackPaneSebesseg);

			});

			gyorsulasGorbeButton.setOnAction(e -> {
				sebessegGorbeButton.setDisable(false);
				gyorsulasGorbeButton.setDisable(true);
				if(offlineImage.getImage() != null && onlineImage.getImage() != null)illesztesButton.setDisable(false);
				mutatButton.setDisable(true);
				sliderFeatures.setDisable(true);
				sliderTopMatches.setDisable(true);
				scrollPaneOssz.setContent(stackPaneGyorsulas);

			});

			illesztesButton.setOnAction(e -> {
				sebessegGorbeButton.setDisable(false);
				gyorsulasGorbeButton.setDisable(false);
				illesztesButton.setDisable(true);
				mutatButton.setDisable(false);
				
				scrollPaneOssz.setContent(stackPaneKepek);
				if (scrollPaneOssz.getContent() != null) {
					sliderFeatures.setDisable(false);
					sliderTopMatches.setDisable(false);
				}
				
			});

			mutatButton.setOnAction(e -> {
				if (topp.isVisible() == true) {
					topp.setVisible(false);
					mutatButton.setText("Sebesség mutatása");
				} else if (topp.isVisible() == false) {
					topp.setVisible(true);
					mutatButton.setText("Sebesség elrejtése");
				}
			});

			sliderFeatures.valueProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
					KepRegisztralo.regisztracio(stackPaneKepek, bottom, topp, new_val.doubleValue(),
							sliderTopMatches.getValue());
				}
			});

			sliderTopMatches.valueProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
					KepRegisztralo.regisztracio(stackPaneKepek, bottom, topp, sliderFeatures.getValue(),
							new_val.doubleValue());
				}
			});

			// írás adat megnyitása fájlból online
			beolvasOnline.setOnAction(e -> {
				Szoveg szoveg = new Szoveg();
				scrollPaneOssz.setContent(null);
				Controller.loadOnlineFile(primaryStage, scrollPaneOnline, zoomPropertyOnline, onlineImage,
						linechartSebesseg, linechartGyorsulas, szoveg);
				if (onlineImage.getImage() != null) {
					WindowElements.infoTabla(infoGridPane, szoveg);
					SebessegGyorsulasGorbe.speedFunction(stackPaneSebesseg, linechartSebesseg);
					SebessegGyorsulasGorbe.accFunction(stackPaneGyorsulas, linechartGyorsulas);
					
					sebessegGorbeButton.setDisable(false);
					gyorsulasGorbeButton.setDisable(false);
				}
				if (offlineImage.getImage() != null && onlineImage.getImage() != null) {
					KepRegisztralo.regisztracio(stackPaneKepek, bottom, topp, sliderFeatures.getValue(),
							sliderTopMatches.getValue());
					scrollPaneOssz.setContent(null);
					illesztesButton.setDisable(false);
				}

			});

			// kép megnyitása fájlból offline
			beolvasOffline.setOnAction(e -> {

				Controller.loadOfflineFile(primaryStage, scrollPaneOffline, zoomPropertyOffline, offlineImage);

				if (offlineImage.getImage() != null && onlineImage.getImage() != null) {
					KepRegisztralo.regisztracio(stackPaneKepek, bottom, topp, sliderFeatures.getValue(),
							sliderTopMatches.getValue());
					scrollPaneOssz.setContent(null);
					illesztesButton.setDisable(false);
				}
			});

			visszaAllitas.setOnAction(e -> {
				onlineImage.setImage(null);
				offlineImage.setImage(null);
				scrollPaneOssz.setContent(null);
			});

			// nagyítás és kicsinyítés egér görgõvel online
			scrollPaneOnline.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
				@Override
				public void handle(ScrollEvent event) {
					if (onlineImage.getImage() != null && event.getDeltaY() > 0
					/* && zoomPropertyOffline.get() <= offlineImage.getImage().getWidth() * 4 */) {
						zoomPropertyOnline.set(zoomPropertyOnline.get() * 1.1);
					} else if (onlineImage.getImage() != null && event.getDeltaY() < 0
					/* && zoomPropertyOffline.get() >= offlineImage.getImage().getWidth() / 3 */ ) {
						zoomPropertyOnline.set(zoomPropertyOnline.get() / 1.1);
					}
					event.consume();
				}
			});

			// nagyítás és kicsinyítés egér görgõvel offline
			scrollPaneOffline.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
				@Override
				public void handle(ScrollEvent event) {
					if (offlineImage.getImage() != null && event.getDeltaY() > 0
					/* && zoomPropertyOffline.get() <= offlineImage.getImage().getWidth() * 4 */) {
						zoomPropertyOffline.set(zoomPropertyOffline.get() * 1.1);
					} else if (offlineImage.getImage() != null && event.getDeltaY() < 0
					/* && zoomPropertyOffline.get() >= offlineImage.getImage().getWidth() / 3 */ ) {
						zoomPropertyOffline.set(zoomPropertyOffline.get() / 1.1);
					}
					event.consume();
				}
			});

			zoomPropertyOnline.addListener(new InvalidationListener() {
				@Override
				public void invalidated(Observable o) {
					onlineImage.setFitWidth(zoomPropertyOnline.get());
				}
			});

			zoomPropertyOffline.addListener(new InvalidationListener() {
				@Override
				public void invalidated(Observable o) {
					offlineImage.setFitWidth(zoomPropertyOffline.get());
				}
			});

			gridpane.add(scrollPaneOssz, 0, 1);
			jobbAlso.getChildren().add(gombok);
			jobbAlso.getChildren().add(infoGridPane);
			gridpane.add(jobbAlso, 1, 1);
//			infoGridPane.add(sebessegGorbeButton, 0, 0);
//			infoGridPane.add(gyorsulasGorbeButton, 0, 1);
//			infoGridPane.add(illesztesButton, 0, 2);
//			infoGridPane.add(mutatButton, 0, 3);
//			infoGridPane.add(sliderFeatures, 0, 4);
//			infoGridPane.add(sliderTopMatches, 0, 5);

			WindowElements.CreateStage(gridpane, scrollPaneOnline, scrollPaneOffline, infoGridPane, root, scene,
					primaryStage);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
