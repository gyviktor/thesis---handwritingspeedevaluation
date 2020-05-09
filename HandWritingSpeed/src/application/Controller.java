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
import javafx.beans.property.DoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {

	public static void loadOfflineFile(Stage primaryStage, ScrollPane scrollPaneOffline,
			DoubleProperty zoomPropertyOffline, ImageView offlineImage) {
		FileChooser filechooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"JPG, PNG, GIF files (*.jpg), (*.png), (*.gif)", "*.jpg", "*.png", "*.gif");
		filechooser.getExtensionFilters().add(extFilter);
		File selectedFile = filechooser.showOpenDialog(primaryStage);
		if (selectedFile != null) {
			try {
				Path from = Paths.get(selectedFile.toURI());
				Path to = Paths.get("offlineKep.png");
				Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
				Image image = new Image("file:offlineKep.png");
				offlineImage.preserveRatioProperty().set(true);
				offlineImage.setImage(image);
				// saveAsPng(offlineImage, "off.png");
				zoomPropertyOffline.set(image.getWidth());
				scrollPaneOffline.setContent(offlineImage);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

	public static void loadOnlineFile(Stage primaryStage, ScrollPane scrollPaneOnline,
			DoubleProperty zoomPropertyOnline, ImageView onlineImage, LineChart<Number, Number> linechartSebesseg,
			LineChart<Number, Number> linechartGyorsulas, Szoveg szoveg) {
		Group onlineData = new Group();
		Group onlineDataTollLent = new Group();
		FileChooser filechooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV, HWR files (*.csv), (*.hwr)",
				"*.csv", "*.hwr");
		filechooser.getExtensionFilters().add(extFilter);
		File selectedFile = filechooser.showOpenDialog(primaryStage);

		if (selectedFile != null) {
			try {
				Controller.beolvas(selectedFile, onlineData, onlineDataTollLent, linechartSebesseg, linechartGyorsulas,
						szoveg);
				saveAsPng(onlineData, "onlineSnapshot.png");
				saveAsPng(onlineDataTollLent, "onlineDataTollLent.png");
				Image image = new Image("file:onlineSnapshot.png");
				onlineImage.setImage(image);
				onlineImage.preserveRatioProperty().set(true);
				zoomPropertyOnline.set(image.getWidth());
				scrollPaneOnline.setContent(onlineImage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void beolvas(File selectedFile, Group onlineData, Group onlineDataTollLent,
			LineChart<Number, Number> linechartSebesseg, LineChart<Number, Number> linechartGyorsulas, Szoveg szoveg)
			throws Exception {

		onlineData.getChildren().clear();
		linechartSebesseg.getData().clear();
		linechartGyorsulas.getData().clear();

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
			System.out.println(vonal.getSzakaszNyomas());

			if (vonal.getPontokSzama() < 2) {
				vonal.addPont(pont);
			} else if ((vonal.getSzakaszPont1().getP() != vonal.getSzakaszPont2().getP())
					&& (vonal.getSzakaszPont1().getP() == 0 || vonal.getSzakaszPont2().getP() == 0)
					|| (vonal.getSzakaszIdo() > 10)) {
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

		SebessegGyorsulasGorbe.sebessegGorbe(linechartSebesseg, szoveg);
		SebessegGyorsulasGorbe.gyorsulasGorbe(linechartGyorsulas, szoveg);
		System.out.println("Golbális átlagsebesség: " + szoveg.getGlobalisSebesseg());
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
			// handle exception here
		}
	}

}
