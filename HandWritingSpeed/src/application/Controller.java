package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {

	public static void loadOfflineFile(Stage primaryStage, MainContent content, Tab tab) {
		Szoveg szoveg = new Szoveg();
		Group onlineData = new Group();
		Group onlineDataTollLent = new Group();
		FileChooser filechooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"JPG, PNG, GIF files (*.jpg), (*.png), (*.gif)", "*.jpg", "*.png", "*.gif");
		filechooser.getExtensionFilters().add(extFilter);
		File selectedFile = filechooser.showOpenDialog(primaryStage);
		if (selectedFile != null) {
			try {
				tab.setText(selectedFile.getName().substring(0, selectedFile.getName().length()-4));
				Path from = Paths.get(selectedFile.toURI());
				Path to = Paths.get("offlineKep.png");
				Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
				Image image = new Image("file:offlineKep.png");
				content.offlineKep.getImageView().preserveRatioProperty().set(true);
				content.offlineKep.getImageView().setImage(image);
				// saveAsPng(offlineImage, "off.png");
				content.offlineKep.getZoomProperty().set(image.getWidth());
				content.offlineKep.getScrollPane().setContent(content.offlineKep.getImageView());

				String fileName = selectedFile.getAbsolutePath().substring(0,
						selectedFile.getAbsolutePath().length() - 4);
//				System.out.println(fileName);
				String onlineFileName = fileName + ".csv";
				File file = new File(onlineFileName);
				Controller.beolvas(file, onlineData, onlineDataTollLent, szoveg, content.infoTabla, content.balAlso);
				saveAsPng(onlineData, "onlineSnapshot.png");
				saveAsPng(onlineDataTollLent, "onlineDataTollLent.png");
				Image img = new Image("file:onlineSnapshot.png");
				content.onlineKep.getImageView().setImage(img);
				content.onlineKep.getImageView().preserveRatioProperty().set(true);
				content.onlineKep.getZoomProperty().set(img.getWidth());
				content.onlineKep.getScrollPane().setContent(content.onlineKep.getImageView());

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

	public static void loadOnlineFile(Stage primaryStage, MainContent content, Tab tab) {
		Szoveg szoveg = new Szoveg();
		Group onlineData = new Group();
		Group onlineDataTollLent = new Group();
		FileChooser filechooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV, HWR files (*.csv), (*.hwr)",
				"*.csv", "*.hwr");
		filechooser.getExtensionFilters().add(extFilter);
		File selectedFile = filechooser.showOpenDialog(primaryStage);

		if (selectedFile != null) {
			try {
				tab.setText(selectedFile.getName().substring(0, selectedFile.getName().length()-4));
				Controller.beolvas(selectedFile, onlineData, onlineDataTollLent, szoveg, content.infoTabla,
						content.balAlso);
				saveAsPng(onlineData, "onlineSnapshot.png");
				saveAsPng(onlineDataTollLent, "onlineDataTollLent.png");
				Image image = new Image("file:onlineSnapshot.png");
				content.onlineKep.getImageView().setImage(image);
				content.onlineKep.getImageView().preserveRatioProperty().set(true);
				content.onlineKep.getZoomProperty().set(image.getWidth());
				content.onlineKep.getScrollPane().setContent(content.onlineKep.getImageView());

				String fileName = selectedFile.getAbsolutePath().substring(0,
						selectedFile.getAbsolutePath().length() - 4);
				System.out.println(fileName);
				String onlineFileName = fileName + ".png";
				File file = new File(onlineFileName);
				Path from = Paths.get(file.toURI());
				Path to = Paths.get("offlineKep.png");
				Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
				Image img = new Image("file:offlineKep.png");
				content.offlineKep.getImageView().preserveRatioProperty().set(true);
				content.offlineKep.getImageView().setImage(img);
				// saveAsPng(offlineImage, "off.png");
				content.offlineKep.getZoomProperty().set(img.getWidth());
				content.offlineKep.getScrollPane().setContent(content.offlineKep.getImageView());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void beolvas(File selectedFile, Group onlineData, Group onlineDataTollLent, Szoveg szoveg,
			InfoTabla infoTabla, ScrollPane scrollPaneBalAlso) throws Exception {

		onlineData.getChildren().clear();
		scrollPaneBalAlso.setContent(null);
		List<List<String>> records = setRecords(selectedFile);
		Pont pont = new Pont();

		System.out.println(selectedFile);

		pont.setXY(Integer.parseInt(records.get(1).get(3)), Integer.parseInt(records.get(1).get(2)));
		pont.setT(Integer.parseInt(records.get(1).get(1)));
		pont.setP(Integer.parseInt(records.get(1).get(4)));

		Vonal vonal = new Vonal(pont);
		Vonal.setOsszIdo(0);

		for (int i = 2; i < records.size(); i++) {
			pont.setXY(Integer.parseInt(records.get(i).get(3)), Integer.parseInt(records.get(i).get(2)));
			pont.setT(Integer.parseInt(records.get(i).get(1)));
			pont.setP(Integer.parseInt(records.get(i).get(4)));

			if (vonal.getPontok().size() < 2) {
				vonal.addPont(pont);
			} else if (((vonal.getSzakaszPont1().getP() != vonal.getSzakaszPont2().getP())
					&& (vonal.getSzakaszPont1().getP() == 0 || vonal.getSzakaszPont2().getP() == 0))
					|| (vonal.getSzakaszIdo() > 15)) {

				szoveg.addVonal(vonal);
				vonal = new Vonal(pont);
			} else {

				Line line = VonalRajzolo.vonalRajz(vonal);
				onlineData.getChildren().add(line);
				Line line2 = VonalRajzolo.vonalRajzKek(vonal);
				onlineDataTollLent.getChildren().add(line2);

				vonal.addPont(pont);
			}

		}

		Gorbe sebessegGorbeG = new Gorbe("Globális sebesség", "Idõ", "Sebesség");
		Gorbe sebessegGorbeH = new Gorbe("Horizontális sebesség", "Idõ", "Sebesség");
		Gorbe sebessegGorbeV = new Gorbe("Vertikális sebesség", "Idõ", "Sebesség");
		Gorbe gyorsulasGorbeG = new Gorbe("Globális gyorsulás", "Idõ", "Gyorsulás");
		Gorbe gyorsulasGorbeH = new Gorbe("Horizontális gyorsulás", "Idõ", "Gyorsulás");
		Gorbe gyorsulasGorbeV = new Gorbe("Vertikális gyorsulás", "Idõ", "Gyorsulás");
		new Thread(() -> {
			sebessegGorbeG.GorbeRajz(szoveg, "sebessegG");
			sebessegGorbeH.GorbeRajz(szoveg, "sebessegH");
			sebessegGorbeV.GorbeRajz(szoveg, "sebessegV");
			gyorsulasGorbeG.GorbeRajz(szoveg, "gyorsulasG");
			gyorsulasGorbeH.GorbeRajz(szoveg, "gyorsulasH");
			gyorsulasGorbeV.GorbeRajz(szoveg, "gyorsulasV");

		}).start();
		infoTabla.setInfoTablaAdatok(szoveg, scrollPaneBalAlso, sebessegGorbeG, sebessegGorbeH, sebessegGorbeV,
				gyorsulasGorbeG, gyorsulasGorbeH, gyorsulasGorbeV);

		onlineData.setScaleX(0.06);
		onlineData.setScaleY(0.06);
		onlineDataTollLent.setScaleX(0.06);
		onlineDataTollLent.setScaleY(0.06);
	}

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
