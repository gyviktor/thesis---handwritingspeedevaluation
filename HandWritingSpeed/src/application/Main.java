package application;

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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jfxutils.chart.AxisConstraint;
import jfxutils.chart.AxisConstraintStrategies;
import jfxutils.chart.AxisConstraintStrategy;
import jfxutils.chart.ChartPanManager;
import jfxutils.chart.ChartZoomManager;
import jfxutils.chart.JFXChartUtil;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		VBox root = new VBox();
		Scene scene = new Scene(root);
		LineChart<Number, Number> linechart = WindowElements.speedFunction();
		GridPane gridpane = WindowElements.createGridPane();
		StackPane stackPaneChart = new StackPane();
		Rectangle rectangle = new Rectangle();
		rectangle.setVisible(false);
		rectangle.setMouseTransparent(false);
		stackPaneChart.getChildren().add(linechart);
		stackPaneChart.getChildren().add(rectangle);
		ScrollPane scrollPaneOnline = WindowElements.createScrollPane();
		ScrollPane scrollPaneOffline = WindowElements.createScrollPane();
		ImageView onlineImage = new ImageView();
		ImageView offlineImage = new ImageView();
		MenuBar menubar = new MenuBar();
		Menu menu = new Menu("Menü");
		Text text = new Text("Text");		
		DoubleProperty zoomPropertyOffline = new SimpleDoubleProperty();
		DoubleProperty zoomPropertyOnline = new SimpleDoubleProperty();
		primaryStage.setTitle("Handwriting speed");
		MenuItem beolvasOnline = new MenuItem("Megnyitás online");
		MenuItem beolvasOffline = new MenuItem("Megnyitás offline");
		menu.getItems().addAll(beolvasOnline, beolvasOffline);
		menubar.getMenus().add(menu);
		root.getChildren().add(menubar);	    

		try {

			beolvasOnline.setOnAction(e -> {
				Controller.loadOnline(primaryStage, scrollPaneOnline, zoomPropertyOnline, onlineImage, linechart, text);
				if(offlineImage.getImage() != null) {
					Controller.imageRegistration();
				}
			});
			
			// open image from file offline
			beolvasOffline.setOnAction(e -> {
				Controller.loadOffline(primaryStage, scrollPaneOffline, zoomPropertyOffline, offlineImage);
				if(onlineImage.getImage() != null) {
					Controller.imageRegistration();
				}
			});

			// zoom in and out with mouse scroll online
			scrollPaneOnline.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
				@Override
				public void handle(ScrollEvent event) {
					if (onlineImage.getImage() != null && event.getDeltaY() > 0
					 /*&& zoomPropertyOffline.get() <= offlineImage.getImage().getWidth() * 4*/) {
						zoomPropertyOnline.set(zoomPropertyOnline.get() * 1.1);
					} else if (onlineImage.getImage() != null && event.getDeltaY() < 0
					 /*&& zoomPropertyOffline.get() >= offlineImage.getImage().getWidth() / 3*/ ) {
						zoomPropertyOnline.set(zoomPropertyOnline.get() / 1.1);
					}
					event.consume();
				}
			});
			
			// zoom in and out with mouse scroll offline
			scrollPaneOffline.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
				@Override
				public void handle(ScrollEvent event) {
					if (offlineImage.getImage() != null && event.getDeltaY() > 0
					 /*&& zoomPropertyOffline.get() <= offlineImage.getImage().getWidth() * 4*/) {
						zoomPropertyOffline.set(zoomPropertyOffline.get() * 1.1);
					} else if (offlineImage.getImage() != null && event.getDeltaY() < 0
					 /*&& zoomPropertyOffline.get() >= offlineImage.getImage().getWidth() / 3*/ ) {
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
			
		    ChartPanManager panner = new ChartPanManager(linechart);
			panner.setMouseFilter(mouseEvent -> {
		        if (mouseEvent.getButton() == MouseButton.PRIMARY) {//set your custom combination to trigger navigation
		            // let it through
		        } else {
		            mouseEvent.consume();
		        }
		    });
		    panner.start();

		    //holding the right mouse button will draw a rectangle to zoom to desired location
		    ChartZoomManager zoomer = new ChartZoomManager(stackPaneChart, rectangle, linechart);
		    zoomer.setMouseFilter(mouseEvent -> {mouseEvent.consume();});
		    zoomer.setMouseWheelZoomAllowed(true);		    
		    zoomer.setMouseWheelAxisConstraintStrategy(AxisConstraintStrategies.getFixed(AxisConstraint.Horizontal));
		    zoomer.start();

			WindowElements.CreateStage(gridpane, scrollPaneOnline, scrollPaneOffline, stackPaneChart, text, root, scene,
					primaryStage);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
