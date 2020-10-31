package application;

import java.util.ArrayList;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class InfoTabla {
	ArrayList<TextFlow> infok;
	GridPane infoGridPane;

	public GridPane getInfoGridPane() {
		return infoGridPane;
	}

	public void setInfoGridPane(GridPane infoGridPane) {
		this.infoGridPane = infoGridPane;
	}

	public InfoTabla() {

		infoGridPane = new GridPane();
		RowConstraints sor = new RowConstraints();
		RowConstraints cimsor = new RowConstraints();
		cimsor.setPrefHeight(30);
		sor.setPrefHeight(50);
		ColumnConstraints oszlop = new ColumnConstraints();
		oszlop.setPrefWidth(100);
		infoGridPane.getRowConstraints().addAll(cimsor, cimsor, sor, sor, sor, sor, sor);
		infoGridPane.getColumnConstraints().addAll(oszlop, oszlop, oszlop, oszlop, oszlop, oszlop);
		infoGridPane.setStyle("-fx-font-size: 14");

		infoGridPane.add(new Text("Sebess�g"), 1, 0);
		infoGridPane.add(new Text("�tlag"), 1, 1);
		infoGridPane.add(new Text("Sz�r�s"), 2, 1);
		infoGridPane.add(new Text("Glob�lis"), 0, 2);
		infoGridPane.add(new Text("Horizont�lis"), 0, 3);
		infoGridPane.add(new Text("Vertik�lis"), 0, 4);
		infoGridPane.add(new Text("Id�"), 0, 5);

		infoGridPane.add(new Text("Gyorsul�s"), 3, 0);
		infoGridPane.add(new Text("�tlag"), 3, 1);
		infoGridPane.add(new Text("Sz�r�s"), 4, 1);

		infoGridPane.add(new Text("Nyom�s"), 0, 6);
		infoGridPane.add(new Text("M�ret"), 5, 1);
	}

	public void setInfoTablaAdatok(Szoveg szoveg, ScrollPane scrollPaneBalAlso, Gorbe sebessegGorbeG,
			Gorbe sebessegGorbeH, Gorbe sebessegGorbeV, Gorbe gyorsulasGorbeG, Gorbe gyorsulasGorbeH,
			Gorbe gyorsulasGorbeV) {
		infok = new ArrayList<TextFlow>();
		String szinKod;
		int[] tempo = new int[19];
		Color[] colors = SzinSkala.intervalColors(0, 120, 60);

		// A sz�veg glob�lis �tlagsebess�ge
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSebessegAtlagG()).substring(0, 6))));
		tempo[0] = szoveg.getSebessegAtlagSzinG();

		// A sz�veg horizont�lis �tlagsebess�ge
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSebessegAtlagH()).substring(0, 6))));
		tempo[1] = szoveg.getSebessegAtlagSzinH();

		// A sz�veg vertik�lis �tlagsebess�ge
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSebessegAtlagV()).substring(0, 6))));
		tempo[2] = szoveg.getSebessegAtlagSzinV();

		// A sz�veg glob�lis sebess�g�nek sz�r�sa
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSebessegSzorasG()).substring(0, 6))));
		tempo[3] = szoveg.getSebessegSzorasSzinG();

		// A sz�veg horizont�lis sebess�g�nek sz�r�sa
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSebessegSzorasH()).substring(0, 6))));
		tempo[4] = szoveg.getSebessegSzorasSzinH();

		// A sz�veg vertik�lis sebess�g�nek sz�r�sa
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSebessegSzorasV()).substring(0, 6))));
		tempo[5] = szoveg.getSebessegSzorasSzinV();

		// A sz�veg glob�lis �tlaggyorsul�sa
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getGyorsulasAtlagG()).substring(0, 6))));
		tempo[6] = szoveg.getGyorsulasAtlagSzinG();
//		System.out.println(szoveg.getGyorsulasAtlagSzinG());

		// A sz�veg horizont�lis �tlaggyorsul�sa
		infok.add(new TextFlow(new Text("\n " + String.valueOf(szoveg.getGyorsulasAtlagH()).substring(0, 6))));
//		System.out.println(szoveg.getGyorsulasAtlagH());
		tempo[7] = szoveg.getGyorsulasAtlagSzinH();
//		System.out.println(szoveg.getGyorsulasAtlagSzinH());

		// A sz�veg vertik�lis �tlaggyorsul�sa
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getGyorsulasAtlagV()).substring(0, 6))));
		tempo[8] = szoveg.getGyorsulasAtlagSzinV();
		System.out.println(szoveg.getGyorsulasAtlagSzinV());

		// A sz�veg glob�lis gyorsul�s�nak sz�r�sa
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getGyorsulasSzorasG()).substring(0, 6))));
		tempo[9] = szoveg.getGyorsulasSzorasSzinG();

		// A sz�veg horizont�lis gyorsul�s�nak sz�r�sa
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getGyorsulasSzorasH()).substring(0, 6))));
		tempo[10] = szoveg.getGyorsulasSzorasSzinH();

		// A sz�veg vertik�lis gyorsul�s�nak sz�r�sa
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getGyorsulasSzorasV()).substring(0, 6))));
		tempo[11] = szoveg.getGyorsulasSzorasSzinV();

		// A sz�veg hossza
		infok.add(new TextFlow(new Text("\n" + szoveg.getSzovegHosszString())));
		tempo[12] = (int) ((szoveg.getSzovegHossz() - 6000) / 700);

		// A sz�veg horizont�lis m�rete
		infok.add(new TextFlow(new Text("\n" + (szoveg.getMaxH() - szoveg.getMinH()))));
		tempo[13] = (int) (szoveg.getMaxH() - szoveg.getMinH()) / 120;

		// A sz�veg vertik�lis m�rete
		infok.add(new TextFlow(new Text("\n" + (szoveg.getMaxV() - szoveg.getMinV()))));
		tempo[14] = (int) (szoveg.getMaxV() - szoveg.getMinV()) / 40;

		infok.add(new TextFlow(new Text("\n" + String.valueOf((szoveg.getSzovegOsszIdo() / 1000)) + " mp")));
		tempo[15] = (int) Math.floor((szoveg.getSzovegHossz() / szoveg.getSzovegOsszIdo() - 1) * 15);
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSzovegTollIdo() / 1000) + " mp")));
		tempo[16] = (int) Math.floor((szoveg.getSzovegTollIdo() / 1000) / (szoveg.getSzovegOsszIdo() / 1000 / 60));
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSzovegEmeltIdo() / 1000) + " mp")));
		tempo[17] = (int) Math.floor((szoveg.getSzovegEmeltIdo() / 1000) / (szoveg.getSzovegOsszIdo() / 1000 / 60));
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getGlobalisNyomas()).substring(0, 7))));
		tempo[18] = (int) Math.floor(((szoveg.getGlobalisNyomas()) - 5000) / 200);

		for (int i = 0; i < infok.size(); i++) {
			if (tempo[i] < 0) {
				tempo[i] = 0;
			}

			else if (tempo[i] >= 60) {
				tempo[i] = 59;
			}

			szinKod = colors[tempo[i]].toString().substring(colors[tempo[i]].toString().length() - 8,
					colors[tempo[i]].toString().length() - 2);
			infok.get(i).setStyle("-fx-background-color: #" + szinKod + ";");
			infok.get(i).setTextAlignment(TextAlignment.CENTER);
		}

		int index = 0;
		for (int i = 1; i < 6; i++) {
			for (int j = 2; j < 5; j++) {
				infoGridPane.add(infok.get(index), i, j);
				index++;
			}
		}

		infoGridPane.add(infok.get(15), 1, 5);
		infoGridPane.add(infok.get(16), 2, 5);
		infoGridPane.add(infok.get(17), 3, 5);
		infoGridPane.add(infok.get(18), 1, 6);
		TablazatGorbeClick(infok, scrollPaneBalAlso, sebessegGorbeG, sebessegGorbeH, sebessegGorbeV, gyorsulasGorbeG,
				gyorsulasGorbeH, gyorsulasGorbeV);

	}

	static int aktiv;
	static int i = 0;

	public static void TablazatGorbeClick(ArrayList<TextFlow> infok, ScrollPane scrollPaneBalAlso, Gorbe sebessegGorbeG,
			Gorbe sebessegGorbeH, Gorbe sebessegGorbeV, Gorbe gyorsulasGorbeG, Gorbe gyorsulasGorbeH,
			Gorbe gyorsulasGorbeV) {
		Border border = new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
		aktiv = 17;

		infok.get(0).setOnMouseClicked(e -> {
			if (aktiv == 0) {
				infok.get(0).setBorder(null);
				scrollPaneBalAlso.setContent(null);
				aktiv = 17;
			} else {
				infok.get(aktiv).setBorder(null);
				scrollPaneBalAlso.setContent(sebessegGorbeG.getStackPane());
				infok.get(0).setBorder(border);
				aktiv = 0;
			}
		});

		infok.get(1).setOnMouseClicked(e -> {
			if (aktiv == 1) {
				infok.get(1).setBorder(null);
				scrollPaneBalAlso.setContent(null);
				aktiv = 17;
			} else {
				infok.get(aktiv).setBorder(null);
				scrollPaneBalAlso.setContent(sebessegGorbeH.getStackPane());
				infok.get(1).setBorder(border);
				aktiv = 1;
			}
		});

		infok.get(2).setOnMouseClicked(e -> {
			if (aktiv == 2) {
				infok.get(2).setBorder(null);
				scrollPaneBalAlso.setContent(null);
				aktiv = 17;
			} else {
				infok.get(aktiv).setBorder(null);
				scrollPaneBalAlso.setContent(sebessegGorbeV.getStackPane());
				infok.get(2).setBorder(border);
				aktiv = 2;
			}
		});

		infok.get(6).setOnMouseClicked(e -> {
			if (aktiv == 6) {
				infok.get(6).setBorder(null);
				scrollPaneBalAlso.setContent(null);
				aktiv = 17;
			} else {
				infok.get(aktiv).setBorder(null);
				scrollPaneBalAlso.setContent(gyorsulasGorbeG.getStackPane());
				infok.get(6).setBorder(border);
				aktiv = 6;
			}

		});

		infok.get(7).setOnMouseClicked(e -> {
			if (aktiv == 7) {
				infok.get(7).setBorder(null);
				scrollPaneBalAlso.setContent(null);
				aktiv = 17;
			} else {
				infok.get(aktiv).setBorder(null);
				scrollPaneBalAlso.setContent(gyorsulasGorbeH.getStackPane());
				infok.get(7).setBorder(border);
				aktiv = 7;
			}
		});

		infok.get(8).setOnMouseClicked(e -> {
			if (aktiv == 8) {
				infok.get(8).setBorder(null);
				scrollPaneBalAlso.setContent(null);
				aktiv = 17;
			} else {
				infok.get(aktiv).setBorder(null);
				scrollPaneBalAlso.setContent(gyorsulasGorbeV.getStackPane());
				infok.get(8).setBorder(border);
				aktiv = 8;
			}
		});

	}
}
