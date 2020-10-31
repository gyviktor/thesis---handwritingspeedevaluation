package application;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuLista {
	MenuBar menubar;
	Menu menu;
	MenuItem beolvasOnline;
	MenuItem beolvasOffline;

	public MenuLista(Stage primaryStage, VBox root, TabPane lapok/* , ArrayList<MainContent> contentLista */) {
		menubar = new MenuBar();
		menu = new Menu("Men�");
		beolvasOnline = new MenuItem("Megnyit�s online");
		beolvasOffline = new MenuItem("Megnyit�s offline");

		menu.getItems().addAll(beolvasOnline, beolvasOffline);
		menubar.getMenus().add(menu);

		// online �r�s adat megnyit�sa f�jlb�l
		beolvasOnline.setOnAction(e -> {
			MainContent content = new MainContent(root);
			Tab tab = new Tab();
			Controller.loadOnlineFile(primaryStage, content, tab);
			new Thread(() -> {				
				content.kepRegisztralo.regisztracio();
				content.illesztesButton.setDisable(false);
			}).start();
			tab.setContent(content.gridpaneAblak);
			lapok.getTabs().add(tab);

		});

		// k�p megnyit�sa f�jlb�l offline
		beolvasOffline.setOnAction(e -> {
			MainContent content = new MainContent(root);
			Tab tab = new Tab();			
			Controller.loadOfflineFile(primaryStage, content, tab);
			new Thread(() -> {				
				content.kepRegisztralo.regisztracio();
				content.illesztesButton.setDisable(false);
			}).start();
			tab.setContent(content.gridpaneAblak);
			lapok.getTabs().add(tab);

		});

	}

	public MenuBar getMenubar() {
		return menubar;
	}

	public void setMenubar(MenuBar menubar) {
		this.menubar = menubar;
	}

	public MenuItem getBeolvasOnline() {
		return beolvasOnline;
	}

	public void setBeolvasOnline(MenuItem beolvasOnline) {
		this.beolvasOnline = beolvasOnline;

	}

	public MenuItem getBeolvasOffline() {
		return beolvasOffline;
	}

	public void setBeolvasOffline(MenuItem beolvasOffline) {
		this.beolvasOffline = beolvasOffline;
	}

}
