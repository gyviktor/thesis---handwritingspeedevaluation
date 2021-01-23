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
		menu = new Menu("Men�");
		beolvas = new MenuItem("Megnyit�s");
		bezaras = new MenuItem("Bez�r�s");

		menu.getItems().addAll(beolvas, bezaras);
		menubar.getMenus().add(menu);

		// k�p megnyit�sa f�jlb�l
		beolvas.setOnAction(e -> {	
			Controller.loadFromFile(primaryStage, root, lapok);			
		});
		// program bez�r�sa
		bezaras.setOnAction(e -> {
	         System.exit(0);
	      });

	}

	public MenuBar getMenubar() {
		return menubar;
	}

}
