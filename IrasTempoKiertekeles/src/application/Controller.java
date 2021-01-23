package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {

	// fajlok tallozasa
	public static void loadFromFile(Stage primaryStage, VBox root, TabPane lapok) {
		FileChooser filechooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG, PNG files (*.jpg), (*.png)",
				"*.jpg", "*.png");
		filechooser.getExtensionFilters().add(extFilter);
		List<File> selectedFiles = filechooser.showOpenMultipleDialog(primaryStage);
		if (selectedFiles != null) {
			for (File file : selectedFiles) {
				try {
					loadFile(root, file, lapok);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// kivalasztott fajlok betoltese
	public static void loadFile(VBox root, File file, TabPane lapok) throws Exception {
		Szoveg szoveg = new Szoveg();
		VonalRajz onlineRajz = new VonalRajz();
		VonalRajz onlineRajzNagy = new VonalRajz();
		VonalRajz onlineRajzTollLent = new VonalRajz();
		MainContent content = new MainContent(root);
		Tab tab = new Tab();

		String fileNev = file.getName().substring(0, file.getName().length() - 4);
		tab.setText(fileNev);

		// a kivalasztas kiterjesztese on-line es off-linera
		String filePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 4);
		String onlineFilePath = filePath + ".csv";
		String offlineFilePath = filePath + ".png";
		File fileOffline = new File(offlineFilePath);
		File fileOnline = new File(onlineFilePath);

		// off-line kep atmasolasa
		Path from = Paths.get(fileOffline.toURI());
		Path to = Paths.get("images/" + fileNev + "_offlineKep.png");
		Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
		Image image = new Image("file:images/" + fileNev + "_offlineKep.png");
		content.offlineKep.getImageView().preserveRatioProperty().set(true);
		content.offlineKep.getImageView().setImage(image);
		content.offlineKep.getZoomProperty().set(image.getWidth());
		content.offlineKep.getScrollPane().setContent(content.offlineKep.getImageView());

		//adatpontok beolvasása
		Controller.beolvas(fileOnline, onlineRajz, onlineRajzNagy, onlineRajzTollLent, szoveg, content.infoTabla,
				content.balAlso);
		
		//szoveg elhelyezése a szinskalan
		content.szinSkala.skala.get(content.infoTabla.getSebesseg()).getChildren().clear();
		content.szinSkala.skala.get(content.infoTabla.getSebesseg()).getChildren().add((new Text(" o ")));

		//kepek az kepilleszteshez
		saveAsPng(onlineRajz.group, "images/" + fileNev + "_onlineSnapshot.png");
		saveAsPng(onlineRajzTollLent.group, "images/" + fileNev + "_onlineDataTollLent.png");
		
		//megjelenített on-line kep
		saveAsPng(onlineRajzNagy.group, "images/" + fileNev + "_onlineNagy.png");
		Image img = new Image("file:images/" + fileNev + "_onlineNagy.png");
		content.onlineKep.getImageView().setImage(img);
		content.onlineKep.getImageView().preserveRatioProperty().set(true);
		content.onlineKep.getZoomProperty().set(img.getWidth());
		content.onlineKep.getScrollPane().setContent(content.onlineKep.getImageView());
		
		//kepregisztralas folyamat
		new Thread(() -> {
			content.kepRegisztralo.regisztracio(fileNev);
			content.illesztesButton.setDisable(false);
		}).start();
		
		tab.setContent(content.gridpaneAblak);
		lapok.getTabs().add(tab);
//		Controller.faljbaIr(szoveg, fileNev);

	}

	//adatpontok beolvasása 
	public static void beolvas(File selectedFile, VonalRajz onlineRajz, VonalRajz onlineRajzNagy,
			VonalRajz onlineRajzTollLent, Szoveg szoveg, InfoTabla infoTabla, ScrollPane scrollPaneBalAlso)
			throws Exception {

		scrollPaneBalAlso.setContent(null);
		List<List<String>> records = setRecords(selectedFile);
		
		// pontok felvetele es hozzaadasa vonalakhoz
		Pont pont = new Pont();
		pont.setXY(Integer.parseInt(records.get(1).get(3)), Integer.parseInt(records.get(1).get(2)));
		pont.setT(Integer.parseInt(records.get(1).get(1)));
		pont.setP(Integer.parseInt(records.get(1).get(4)));
		Vonal.setOsszIdo(0.0);
		Vonal vonal = new Vonal(pont);

		//vegigmegyunk a fajl osszes soran
		for (int i = 2; i < records.size(); i++) {
			pont.setXY(Integer.parseInt(records.get(i).get(3)), Integer.parseInt(records.get(i).get(2)));
			pont.setT(Integer.parseInt(records.get(i).get(1)));
			pont.setP(Integer.parseInt(records.get(i).get(4)));
			
			if (((vonal.getSzakaszPont1().getP() != vonal.getSzakaszPont2().getP())
					&& (vonal.getSzakaszPont1().getP() == 0 || vonal.getSzakaszPont2().getP() == 0))
					|| (vonal.getSzakaszIdo() > 15) & vonal.getPontok().size() > 1) {
				//a vonalat hozzaadjuk a szoveghez, uj vonalat kezdunk es ahhoz adjuk a pontot
				vonal.javit();
				szoveg.addVonal(vonal);
				vonal = new Vonal(pont);
			} else {
				//a jelenlegi vonalhoz adjuk a pontot
				vonal.addPont(pont);
			}

		}

		vonal.javit();
		szoveg.addVonal(vonal);
		szoveg.setAtlagok();
		szoveg.setSzoras();
		//on-line kepek rajzolasa
		onlineRajz.rajzol(szoveg, 0.06, true);
		onlineRajzTollLent.rajzol(szoveg, 0.06, false);
		onlineRajzNagy.rajzol(szoveg, 0.15, true);

		//grafikonok letrehozasa
		Gorbe sebessegGorbeG = new Gorbe("Globális sebesség", "Idõ (s)", "Sebesség (cm/s)");
		Gorbe sebessegGorbeH = new Gorbe("Horizontális sebesség", "Idõ (s)", "Sebesség (cm/s)");
		Gorbe sebessegGorbeV = new Gorbe("Vertikális sebesség", "Idõ (s)", "Sebesség (cm/s)");
		Gorbe gyorsulasGorbeG = new Gorbe("Globális gyorsulás", "Idõ (s)", "Gyorsulás (cm/s^2)");
		Gorbe gyorsulasGorbeH = new Gorbe("Horizontális gyorsulás", "Idõ (s)", "Gyorsulás (cm/s^2)");
		Gorbe gyorsulasGorbeV = new Gorbe("Vertikális gyorsulás", "Idõ (s)", "Gyorsulás (cm/s^2)");
		Gorbe xGorbe = new Gorbe("X (cm)", "Idõ (s)", "X");
		Gorbe yGorbe = new Gorbe("Y (cm)", "Idõ (s)", "Y");
		Gorbe nyomasGorbe = new Gorbe("Nyomás", "Idõ", "Nyomás");

		//gorbek megrajzolasa
		new Thread(() -> {
			sebessegGorbeG.GorbeRajz(szoveg, "sebessegG");
			sebessegGorbeH.GorbeRajz(szoveg, "sebessegH");
			sebessegGorbeV.GorbeRajz(szoveg, "sebessegV");
			gyorsulasGorbeG.GorbeRajz(szoveg, "gyorsulasG");
			gyorsulasGorbeH.GorbeRajz(szoveg, "gyorsulasH");
			gyorsulasGorbeV.GorbeRajz(szoveg, "gyorsulasV");
			xGorbe.GorbeRajz(szoveg, "xGorbe");
			yGorbe.GorbeRajz(szoveg, "yGorbe");
			nyomasGorbe.GorbeRajz(szoveg, "nyomasG");
		}).start();
		// inormacios tablazathoz ertekek adasa
		infoTabla.setInfoTablaAdatok(szoveg, scrollPaneBalAlso, sebessegGorbeG, sebessegGorbeH, sebessegGorbeV,
				gyorsulasGorbeG, gyorsulasGorbeH, gyorsulasGorbeV, xGorbe, yGorbe, nyomasGorbe);

	}

	// informaciok kiirasa fajlba 
	public static void faljbaIr(Szoveg szoveg, String fileNev) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter("info.csv", true));

		writer.append(fileNev + ";");
		writer.append(String.valueOf(szoveg.getSebessegAtlagG()) + ";");
		writer.append(String.valueOf(szoveg.getSebessegSzorasG()) + ";");
		writer.append(String.valueOf(szoveg.getSebessegAtlagH()) + ";");
		writer.append(String.valueOf(szoveg.getSebessegSzorasH()) + ";");
		writer.append(String.valueOf(szoveg.getSebessegAtlagV()) + ";");
		writer.append(String.valueOf(szoveg.getSebessegSzorasV()) + ";");
		writer.append(String.valueOf(szoveg.getGyorsulasAtlagG()) + ";");
		writer.append(String.valueOf(szoveg.getGyorsulasSzorasG()) + ";");
		writer.append(String.valueOf(szoveg.getGyorsulasAtlagH()) + ";");
		writer.append(String.valueOf(szoveg.getGyorsulasSzorasH()) + ";");
		writer.append(String.valueOf(szoveg.getGyorsulasAtlagV()) + ";");
		writer.append(String.valueOf(szoveg.getGyorsulasSzorasV()) + ";");
		writer.append(String.valueOf(szoveg.getSzovegHossz()) + ";");
		writer.append(String.valueOf(szoveg.getSzovegMeretH()) + ";");
		writer.append(String.valueOf(szoveg.getSzovegMeretV()) + ";");
		writer.append(String.valueOf(szoveg.getGlobalisNyomas()) + ";");
		writer.append(String.valueOf(szoveg.getSzovegOsszIdo()) + ";");
		writer.append(String.valueOf(szoveg.getSzovegTollIdo()) + ";");
		writer.append(String.valueOf(szoveg.getSzovegEmeltIdo()) + ";\n");

		writer.close();

	}

	// CSV fajl sorokra bontasa es tarolasa
	public static List<List<String>> setRecords(File selectedFile) throws Exception {
		List<List<String>> records = new ArrayList<>();
		BufferedReader br = new BufferedReader(new FileReader(selectedFile));
		String l;
		while ((l = br.readLine()) != null) {
			String[] values = l.split(";");
			records.add(Arrays.asList(values));
		}
		br.close();
		return records;
	}

	//Node kimentese kepkent, atlatszo hatterrel
	public static final void saveAsPng(Node NODE, String fileNev) {
		SnapshotParameters sp = new SnapshotParameters();
		sp.setFill(Color.TRANSPARENT);
		WritableImage SNAPSHOT = NODE.snapshot(sp, null);
		File FILE = new File(fileNev);

		try {
			ImageIO.write(SwingFXUtils.fromFXImage(SNAPSHOT, null), "png", FILE);
		} catch (IOException exception) {
			System.out.println("Nem sikerült a kép mentés!");
		}
	}

}