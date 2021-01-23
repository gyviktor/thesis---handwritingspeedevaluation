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
	private int sebesseg;

	public GridPane getInfoGridPane() {
		return infoGridPane;
	}

	public void setInfoGridPane(GridPane infoGridPane) {
		this.infoGridPane = infoGridPane;
	}

	// Informacios tablazat letrehozasa
	public InfoTabla() {
		infoGridPane = new GridPane();
		RowConstraints sor = new RowConstraints();
		RowConstraints cimsor = new RowConstraints();
		cimsor.setPrefHeight(30);
		sor.setPrefHeight(50);
		ColumnConstraints oszlop = new ColumnConstraints();
		oszlop.setPercentWidth(18.0);
		ColumnConstraints oszlop2 = new ColumnConstraints();
		oszlop.setPercentWidth(10.0);
		infoGridPane.getRowConstraints().addAll(cimsor, cimsor, sor, sor, sor, sor, sor, sor);
		infoGridPane.getColumnConstraints().addAll(oszlop2, oszlop, oszlop, oszlop, oszlop, oszlop);
		infoGridPane.setStyle("-fx-font-size: 14");

		infoGridPane.add(new Text("Sebesség"), 1, 0);
		infoGridPane.add(new Text("Átlag"), 1, 1);
		infoGridPane.add(new Text("Szórás"), 2, 1);
		infoGridPane.add(new Text("Globális"), 0, 2);
		infoGridPane.add(new Text("Horizontális"), 0, 3);
		infoGridPane.add(new Text("Vertikális"), 0, 4);
		infoGridPane.add(new Text("Idõ"), 0, 7);

		infoGridPane.add(new Text("Gyorsulás"), 3, 0);
		infoGridPane.add(new Text("Átlag"), 3, 1);
		infoGridPane.add(new Text("Szórás"), 4, 1);

		infoGridPane.add(new Text("Nyomás"), 0, 6);
		infoGridPane.add(new Text("Méret"), 5, 1);
	}

	// inormacios tablazathoz ertekek adasa
	public void setInfoTablaAdatok(Szoveg szoveg, ScrollPane scrollPaneBalAlso, Gorbe sebessegGorbeG,
			Gorbe sebessegGorbeH, Gorbe sebessegGorbeV, Gorbe gyorsulasGorbeG, Gorbe gyorsulasGorbeH,
			Gorbe gyorsulasGorbeV, Gorbe xGorbe, Gorbe yGorbe, Gorbe nyomasGorbe) {
		infok = new ArrayList<TextFlow>();
		String szinKod;
		int[] tempo = new int[19];
		Color[] colors = SzinSkala.intervalColors(0, 120, 60);

		// A szöveg globális átlagsebessége
		if (String.valueOf(szoveg.getSebessegAtlagG()).length() < 6) {
			infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSebessegAtlagG()).substring(0,
					String.valueOf(szoveg.getSebessegAtlagG()).length()) + " cm/s")));
		} else {
			infok.add(new TextFlow(
					new Text("\n" + String.valueOf(szoveg.getSebessegAtlagG()).substring(0, 5) + " cm/s")));
		}
		tempo[0] = szoveg.getSebessegAtlagSzinG();

		// A szöveg horizontális átlagsebessége
		if (String.valueOf(szoveg.getSebessegAtlagH()).length() < 6) {
			infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSebessegAtlagH()).substring(0,
					String.valueOf(szoveg.getSebessegAtlagH()).length()) + " cm/s")));
		} else {
			infok.add(new TextFlow(
					new Text("\n" + String.valueOf(szoveg.getSebessegAtlagH()).substring(0, 5) + " cm/s")));
		}
		tempo[1] = szoveg.getSebessegAtlagSzinH();

		// A szöveg vertikális átlagsebessége
		if (String.valueOf(szoveg.getSebessegAtlagV()).length() < 6) {
			infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSebessegAtlagV()).substring(0,
					String.valueOf(szoveg.getSebessegAtlagV()).length()) + " cm/s")));
		} else {
			infok.add(new TextFlow(
					new Text("\n" + String.valueOf(szoveg.getSebessegAtlagV()).substring(0, 5) + " cm/s")));
		}
		tempo[2] = szoveg.getSebessegAtlagSzinV();

		// A szöveg globális sebességének szórása
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSebessegSzorasG()).substring(0, 6))));
		tempo[3] = szoveg.getSebessegSzorasSzinG();

		// A szöveg horizontális sebességének szórása
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSebessegSzorasH()).substring(0, 6))));
		tempo[4] = szoveg.getSebessegSzorasSzinH();

		// A szöveg vertikális sebességének szórása
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSebessegSzorasV()).substring(0, 6))));
		tempo[5] = szoveg.getSebessegSzorasSzinV();

		// A szöveg globális átlaggyorsulása
		Text kitevo = new Text("2");
		kitevo.setTranslateY(kitevo.getFont().getSize() * -0.5);
		if (String.valueOf(szoveg.getGyorsulasAtlagG()).length() < 6) {
			infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getGyorsulasAtlagG()).substring(0,
					String.valueOf(szoveg.getGyorsulasAtlagG()).length()) + " cm/s"), kitevo));
		} else {
			infok.add(new TextFlow(
					new Text("\n" + String.valueOf(szoveg.getGyorsulasAtlagG()).substring(0, 6) + " cm/s"), kitevo));
		}
		tempo[6] = szoveg.getGyorsulasAtlagSzinG();

		// A szöveg horizontális átlaggyorsulása
		Text kitevo2 = new Text("2");
		kitevo2.setTranslateY(kitevo2.getFont().getSize() * -0.5);
		if (String.valueOf(szoveg.getGyorsulasAtlagH()).length() < 6) {
			infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getGyorsulasAtlagH()).substring(0,
					String.valueOf(szoveg.getGyorsulasAtlagH()).length()) + " cm/s"), kitevo2));
		} else {
			infok.add(new TextFlow(
					new Text("\n" + String.valueOf(szoveg.getGyorsulasAtlagH()).substring(0, 6) + " cm/s"), kitevo2));
		}

		tempo[7] = szoveg.getGyorsulasAtlagSzinH();

		// A szöveg vertikális átlaggyorsulása
		Text kitevo3 = new Text("2");
		kitevo3.setTranslateY(kitevo3.getFont().getSize() * -0.5);

		if (String.valueOf(szoveg.getGyorsulasAtlagV()).length() < 6) {
			infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getGyorsulasAtlagV()).substring(0,
					String.valueOf(szoveg.getGyorsulasAtlagV()).length()) + " cm/s"), kitevo3));
		} else {
			infok.add(new TextFlow(
					new Text("\n" + String.valueOf(szoveg.getGyorsulasAtlagV()).substring(0, 6) + " cm/s"), kitevo3));
		}

		tempo[8] = szoveg.getGyorsulasAtlagSzinV();

		// A szöveg globális gyorsulásának szórása
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getGyorsulasSzorasG()).substring(0, 6))));
		tempo[9] = szoveg.getGyorsulasSzorasSzinG();

		// A szöveg horizontális gyorsulásának szórása
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getGyorsulasSzorasH()).substring(0, 6))));
		tempo[10] = szoveg.getGyorsulasSzorasSzinH();

		// A szöveg vertikális gyorsulásának szórása
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getGyorsulasSzorasV()).substring(0, 6))));
		tempo[11] = szoveg.getGyorsulasSzorasSzinV();

		// A szöveg hossza
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSzovegHossz()).substring(0, 6) + " cm")));
		tempo[12] = (int) (szoveg.getSzovegHossz() *5);

		// A szöveg horizontális mérete
		if(String.valueOf(szoveg.getSzovegMeretH()).length() > 6) {
			infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSzovegMeretH()).substring(0, 6) + " cm")));
		}else {
			infok.add(new TextFlow(new Text("\n" + (szoveg.getSzovegMeretH()) + " cm")));
		}		
		tempo[13] = (int) ((szoveg.getSzovegMeretH() / szoveg.getSzovegHossz()-0.25) * 200);

		// A szöveg vertikális mérete
		if(String.valueOf(szoveg.getSzovegMeretV()).length() > 6) {
			infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSzovegMeretV()).substring(0, 6) + " cm")));
		}else {
			infok.add(new TextFlow(new Text("\n" + (szoveg.getSzovegMeretV()) + " cm")));
		}
		tempo[14] = (int) (szoveg.getSzovegMeretV() * 60);

		// A szöveg összideje
		if(String.valueOf((szoveg.getSzovegOsszIdo())).length() < 5){
			infok.add(new TextFlow(new Text("\n" + String.valueOf((szoveg.getSzovegOsszIdo())).substring(0, String.valueOf((szoveg.getSzovegOsszIdo())).length()) + " mp")));
		}else {
			infok.add(new TextFlow(new Text("\n" + String.valueOf((szoveg.getSzovegOsszIdo())).substring(0, 5) + " mp")));
		}
				
		
		tempo[15] = (int) Math.floor(szoveg.getSzovegHossz() / szoveg.getSzovegOsszIdo() * 20);
		
		// A szöveg toll ideje
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSzovegTollIdo()).substring(0, 5) + " mp")));
		tempo[16] = (int) Math.floor(szoveg.getSzovegTollIdo() / szoveg.getSzovegOsszIdo() *60);
		
		// A szöveg emelt ideje
		infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getSzovegEmeltIdo()).substring(0, 5) + " mp")));
		tempo[17] = (int) Math.floor(szoveg.getSzovegEmeltIdo() / szoveg.getSzovegOsszIdo() * 60);
		
		// A szöveg atlag nyomasa
		if (szoveg.getGlobalisNyomas() >= 10000) {
			infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getGlobalisNyomas()).substring(0, 7))));
		} else {
			infok.add(new TextFlow(new Text("\n" + String.valueOf(szoveg.getGlobalisNyomas()).substring(0, 6))));
		}

		tempo[18] = (int) Math.floor(((szoveg.getGlobalisNyomas()) - 5000) / 220);
		
		//Szinek hozzarendelese
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
		//tempo becslese sebessegbol es idobol
		this.setSebesseg((tempo[15] + tempo[0]) / 2);

		// infok beillesztese a tablazatba
		int index = 0;
		for (int i = 1; i < 6; i++) {
			for (int j = 2; j < 5; j++) {
				infoGridPane.add(infok.get(index), i, j);
				index++;
			}
		}
		infoGridPane.add(infok.get(15), 1, 7);
		infoGridPane.add(infok.get(16), 2, 7);
		infoGridPane.add(infok.get(17), 3, 7);
		infoGridPane.add(infok.get(18), 1, 6);
		//Kattintásra grafikonok megjelenítése
		TablazatGorbeClick(infok, scrollPaneBalAlso, sebessegGorbeG, sebessegGorbeH, sebessegGorbeV, gyorsulasGorbeG,
				gyorsulasGorbeH, gyorsulasGorbeV, xGorbe, yGorbe, nyomasGorbe);

	}

	static int aktiv;

	//Kattintásra grafikonok megjelenítése
	public static void TablazatGorbeClick(ArrayList<TextFlow> infok, ScrollPane scrollPaneBalAlso, Gorbe sebessegGorbeG,
			Gorbe sebessegGorbeH, Gorbe sebessegGorbeV, Gorbe gyorsulasGorbeG, Gorbe gyorsulasGorbeH,
			Gorbe gyorsulasGorbeV, Gorbe xGorbe, Gorbe yGorbe, Gorbe nyomasGorbe) {
		
		//a kivalasztott cellahoz fekete korvonalat adunk es megjelenitjuk a hozzatartozo gorbet
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

		infok.get(13).setOnMouseClicked(e -> {
			if (aktiv == 13) {
				infok.get(13).setBorder(null);
				scrollPaneBalAlso.setContent(null);
				aktiv = 17;
			} else {
				infok.get(aktiv).setBorder(null);
				scrollPaneBalAlso.setContent(xGorbe.getStackPane());
				infok.get(13).setBorder(border);
				aktiv = 13;
			}
		});

		infok.get(14).setOnMouseClicked(e -> {
			if (aktiv == 14) {
				infok.get(14).setBorder(null);
				scrollPaneBalAlso.setContent(null);
				aktiv = 17;
			} else {
				infok.get(aktiv).setBorder(null);
				scrollPaneBalAlso.setContent(yGorbe.getStackPane());
				infok.get(14).setBorder(border);
				aktiv = 14;
			}
		});

		infok.get(18).setOnMouseClicked(e -> {
			if (aktiv == 18) {
				infok.get(18).setBorder(null);
				scrollPaneBalAlso.setContent(null);
				aktiv = 17;
			} else {
				infok.get(aktiv).setBorder(null);
				scrollPaneBalAlso.setContent(nyomasGorbe.getStackPane());
				infok.get(18).setBorder(border);
				aktiv = 18;
			}
		});

	}

	public int getSebesseg() {
		return sebesseg;
	}

	public void setSebesseg(int sebesseg) {
		this.sebesseg = sebesseg;
	}
}
