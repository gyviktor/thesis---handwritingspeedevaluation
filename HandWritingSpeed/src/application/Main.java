package application;

import java.io.File;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {		
		VBox root = new VBox();
		Scene scene;		
		LineChart<Number, Number> linechart = WindowElements.speedFunction();		
		GridPane gridpane = WindowElements.createGridPane();
		ScrollPane scrollPaneOnline = WindowElements.createScrollPane();
		ScrollPane scrollPaneOffline = WindowElements.createScrollPane();
		AnchorPane onlineData = new AnchorPane();
		MenuBar menubar = new MenuBar();
		Menu menu = new Menu("Menü");
		
		
		Text text = new Text("Text");
		ImageView offlineImage = new ImageView();
		DoubleProperty zoomPropertyOffline = new SimpleDoubleProperty();

		try {
			primaryStage.setTitle("Handwriting speed");
			MenuItem beolvasOnline = new MenuItem("Megnyitás online");
			MenuItem beolvasOffline = new MenuItem("Megnyitás offline");
			menu.getItems().addAll(beolvasOnline, beolvasOffline);
			menubar.getMenus().add(menu);
			root.getChildren().add(menubar);

			// zoom in and out with mouse scroll online
			scrollPaneOnline.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
				@Override
				public void handle(ScrollEvent event) {
					if (event.getDeltaY() > 0 /*&& onlineData.getScaleX() <= 2*/) {
						onlineData.setScaleX(onlineData.getScaleX() * 1.5);
						onlineData.setScaleY(onlineData.getScaleY() * 1.5);
						System.out.println();
					} else if (event.getDeltaY() < 0 /*&& onlineData.getScaleX() >= 0.1*/) {
						onlineData.setScaleX(onlineData.getScaleX() * 0.75);
						onlineData.setScaleY(onlineData.getScaleY() * 0.75);
						//onlineData.setTranslateX(0 - scrollPaneOnline.getWidth());     
						//onlineData.setTranslateY(0 - scrollPaneOnline.getHeight());
						//System.out.println("Width: " + onlineData.getMinWidth() + "  " + onlineData.getMaxWidth() + "  " + onlineData.getPrefWidth());
						//System.out.println("Height: " + scrollPaneOnline.getHeight());
						
					}
					event.consume();
				}
			});
			// scrollPaneOnline.resize(onlineData.getScaleX(), onlineData.getScaleY());

			beolvasOnline.setOnAction(e -> {
				FileChooser filechooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV, HWR files (*.csv), (*.hwr)", "*.csv", "*.hwr");
				filechooser.getExtensionFilters().add(extFilter);
				File selectedFile = filechooser.showOpenDialog(primaryStage);
				try {	
					/*
					onlineData = Controller.beolvas(selectedFile);
					linechart = Controller.speedFunctionData(linechart, selectedFile);
					//onlineData.prefWidthProperty().bind(scrollPaneOnline.widthProperty());
					
					onlineData.resize(scrollPaneOnline.getWidth(), scrollPaneOnline.getHeight());
					onlineData.setMaxHeight(scrollPaneOnline.getMaxHeight());
					onlineData.setMaxWidth(scrollPaneOnline.getMaxWidth());
					onlineData.setMinHeight(scrollPaneOnline.getMinHeight());
					onlineData.setMinWidth(scrollPaneOnline.getMinWidth());
					onlineData.setScaleX(0.1);
					onlineData.setScaleY(0.1);
					*/
					Controller.beolvass(selectedFile, onlineData, linechart, text);
										
					scrollPaneOnline.setContent(onlineData);					
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}	

			});

			// zoom in and out with mouse scroll offline
			scrollPaneOffline.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
				@Override
				public void handle(ScrollEvent event) {
					if (offlineImage.getImage() != null && event.getDeltaY() > 0
					/* && zoomProperty.get() <= offlineImage.getImage().getWidth() * 4 */) {
						zoomPropertyOffline.set(zoomPropertyOffline.get() * 1.1);
					} else if (offlineImage.getImage() != null && event.getDeltaY() < 0
					/* && zoomProperty.get() >= offlineImage.getImage().getWidth() / 3 */) {
						zoomPropertyOffline.set(zoomPropertyOffline.get() / 1.1);
					}
					event.consume();
				}
			});
			zoomPropertyOffline.addListener(new InvalidationListener() {
				@Override
				public void invalidated(Observable o) {
					offlineImage.setFitWidth(zoomPropertyOffline.get());
				}
			});
			// open image from file offline
			beolvasOffline.setOnAction(e -> {
				FileChooser filechooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG, PNG, GIF files (*.jpg), (*.png), (*.gif)", "*.jpg", "*.png", "*.gif");
				filechooser.getExtensionFilters().add(extFilter);
				File selectedFile = filechooser.showOpenDialog(primaryStage);
				try {
					Image image = new Image(selectedFile.toURI().toString());
					offlineImage.setImage(image);
					offlineImage.preserveRatioProperty().set(true);
					offlineImage.setFitWidth(scrollPaneOffline.getWidth());
					zoomPropertyOffline.set(scrollPaneOffline.getWidth());
					scrollPaneOffline.setContent(offlineImage);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					// e1.printStackTrace();
				}
			});
			

			
			gridpane.add(scrollPaneOnline, 0, 0);
			gridpane.add(scrollPaneOffline, 0, 1);
			gridpane.add(linechart, 1, 0);
			gridpane.add(text, 1, 1);
			gridpane.prefHeightProperty().bind(root.heightProperty());
			root.getChildren().addAll(gridpane);
			// create a scene
			scene = new Scene(root, 800, 600);
			scene.setRoot(root);
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
