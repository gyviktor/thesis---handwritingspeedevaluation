package application;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuLista {
	MenuBar menubar;
	Menu menu;
	MenuItem beolvas;
	MenuItem bezaras;

	public MenuLista(Stage primaryStage, VBox root, TabPane lapok) {
		menubar = new MenuBar();
		menu = new Menu("Menü");
		beolvas = new MenuItem("Megnyitás");
		bezaras = new MenuItem("Bezárás");

		menu.getItems().addAll(beolvas, bezaras);
		menubar.getMenus().add(menu);

		// kép megnyitása fájlból
		beolvas.setOnAction(e -> {	
			Controller.loadFromFile(primaryStage, root, lapok);			
		});
		// program bezárása
		bezaras.setOnAction(e -> {
	         System.exit(0);
	      });

	}

	public MenuBar getMenubar() {
		return menubar;
	}

}
