package application;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainContent {
	GridPane gridpaneAblak;
	InfoTabla infoTabla;
	Kepek offlineKep;
	Kepek onlineKep;
	VBox jobbAlso;
	ScrollPane balAlso;
	HBox gombok;
	SzinSkala szinSkala;
	Button illesztesButton;
	Button mutatButton;
	KepRegisztralo kepRegisztralo;

	// a fo ablak osszeallitasa
	public MainContent(VBox root) {
		gridpaneAblak = WindowElements.createGridPane();
		balAlso = WindowElements.createScrollPane();
		jobbAlso = new VBox();		
		infoTabla = new InfoTabla();
		offlineKep = new Kepek();
		onlineKep = new Kepek();
		gombok = WindowElements.createButtons();
		szinSkala = new SzinSkala();
		
		illesztesButton = new Button("Egymásra illesztés");
		mutatButton = new Button("Sebesség elrejtése");
		kepRegisztralo = new KepRegisztralo();

		gridpaneAblak.add(balAlso, 0, 1);
		gridpaneAblak.add(jobbAlso, 1, 1);
		gridpaneAblak.add(onlineKep.getScrollPane(), 0, 0);
		gridpaneAblak.add(offlineKep.getScrollPane(), 1, 0);
		gridpaneAblak.prefHeightProperty().bind(root.heightProperty());

		illesztesButton.setDisable(true);
		mutatButton.setDisable(true);
		gombok.getChildren().addAll(illesztesButton, mutatButton);

		jobbAlso.getChildren().add(infoTabla.getInfoGridPane());
		jobbAlso.getChildren().add(gombok);
		jobbAlso.getChildren().add(szinSkala.HBox);

		// gomb a kepregisztracio megjelenitesere
		illesztesButton.setOnAction(e -> {
			mutatButton.setDisable(false);
			balAlso.setContent(kepRegisztralo.getStackpane());
		});

		// gomb a regisztralt kepen a sebesseg elrejtesere/mutatasara
		mutatButton.setOnAction(e -> {
			if (kepRegisztralo.getOnlineIllesztettKep().isVisible() == true) {
				kepRegisztralo.getOnlineIllesztettKep().setVisible(false);
				mutatButton.setText("Sebesség mutatása");
			} else if (kepRegisztralo.getOnlineIllesztettKep().isVisible() == false) {
				kepRegisztralo.getOnlineIllesztettKep().setVisible(true);
				mutatButton.setText("Sebesség elrejtése");
			}
		});

	}

}
