package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Írástempó értékelõ");
		VBox root = new VBox();
		Scene scene = new Scene(root);
		TabPane lapok = new TabPane();
		MenuLista menu = new MenuLista(primaryStage, root, lapok);

		root.getChildren().add(menu.getMenubar());
		root.getChildren().add(lapok);

		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.setMinHeight(540);
		primaryStage.setMinWidth(960);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
