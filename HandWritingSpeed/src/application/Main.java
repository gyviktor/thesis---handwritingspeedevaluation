package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Handwriting speed");
		VBox root = new VBox();
		Scene scene = new Scene(root, 1280, 720);
		TabPane lapok = new TabPane();
		MenuLista menu = new MenuLista(primaryStage, root, lapok);

		try {

			root.getChildren().add(menu.getMenubar());
			root.getChildren().add(lapok);

			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			primaryStage.setMinHeight(540);
			primaryStage.setMinWidth(960);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
