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
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.ORB;

public class Controller {

	public static void loadOnline(Stage primaryStage, ScrollPane scrollPaneOnline, DoubleProperty zoomPropertyOnline,
			ImageView onlineImage, LineChart<Number, Number> linechart, Text text) {
		Group onlineData = new Group();
		Group onlineDataTollLent = new Group();
		FileChooser filechooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV, HWR files (*.csv), (*.hwr)",
				"*.csv", "*.hwr");
		filechooser.getExtensionFilters().add(extFilter);
		File selectedFile = filechooser.showOpenDialog(primaryStage);

		if (selectedFile != null) {
			try {
				Controller.beolvas(selectedFile, onlineData, onlineDataTollLent, linechart, text);
				saveAsPng(onlineData, "onlineSnapshot.png");
				saveAsPng(onlineDataTollLent, "onlineDataTollLent.png");
				
				Image image = new Image("file:onlineDataTollLent.png");
				
				onlineImage.setImage(image);				
				onlineImage.preserveRatioProperty().set(true);
				zoomPropertyOnline.set(image.getWidth());
				scrollPaneOnline.setContent(onlineImage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void loadOffline(Stage primaryStage, ScrollPane scrollPaneOffline, DoubleProperty zoomPropertyOffline,
			ImageView offlineImage) {
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
				//Image image = new Image("file:offlineKep.png", 0, 400, true, true);
				Image image = new Image("file:offlineKep.png");
				offlineImage.preserveRatioProperty().set(true);
				offlineImage.setImage(image);				
				//saveAsPng(offlineImage, "off.png");				
				zoomPropertyOffline.set(image.getWidth());
				scrollPaneOffline.setContent(offlineImage);
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

	public static void imageRegistration(ImageView online, ImageView offline) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat onlineMindenPont = Imgcodecs.imread("onlineSnapshot.png");
		Mat onlinePic = Imgcodecs.imread("onlineDataTollLent.png");
		Mat offlinePic = Imgcodecs.imread("offlineKep.png");
		Mat onlineGray = new Mat();
		Mat offlineGray = new Mat();
		Imgproc.cvtColor(onlinePic, onlineGray, Imgproc.COLOR_RGB2GRAY);
		Imgproc.cvtColor(offlinePic, offlineGray, Imgproc.COLOR_RGB2GRAY);
		System.out.println("Converted to Grayscale");
		Mat onlineBinary = new Mat();
		Mat offlineBinary = new Mat();
		Imgproc.threshold(onlineGray, onlineBinary, 0, 255, Imgproc.THRESH_BINARY);
		Imgproc.threshold(offlineGray, offlineBinary, 240, 255, Imgproc.THRESH_BINARY_INV);
		System.out.println("Converted to Binary");
		Imgcodecs.imwrite("onlineBinary.png", onlineBinary);
		Imgcodecs.imwrite("offlineBinary.png", offlineBinary);
		Mat offlineBinaryOpen = new Mat();
		Size size = new Size(3.0, 3.0);
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, size);
		Imgproc.morphologyEx(offlineBinary, offlineBinaryOpen, Imgproc.MORPH_OPEN, kernel);
		Imgcodecs.imwrite("offlineBinaryOpen.png", offlineBinaryOpen);
		
		
		// Variables to store keypoints and descriptors
		MatOfKeyPoint keyPointsOffline = new MatOfKeyPoint();
		MatOfKeyPoint keyPointsOnline = new MatOfKeyPoint();
		Mat descriptorsOnline = new Mat();
		Mat descriptorsOffline = new Mat();

		// Detect ORB features and compute descriptors.
		ORB orbDetector = ORB.create(1500);	
		orbDetector.detectAndCompute(onlineBinary, new Mat(), keyPointsOnline, descriptorsOnline);
		orbDetector.detectAndCompute(offlineBinaryOpen, new Mat(), keyPointsOffline, descriptorsOffline);
		
		// Match features.
		MatOfDMatch matches = new MatOfDMatch();
		DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
		matcher.match(descriptorsOffline, descriptorsOnline,  matches, new Mat());
		System.out.println("Matches: " + matches.rows());
		
		// Sort matches by score
		DMatch[] matchesArray = matches.toArray();		
		boolean sorted = false;
		DMatch temp;
		while (!sorted) {
			sorted = true;
			for (int i = 0; i < matchesArray.length - 1; i++) {
				if (matchesArray[i].distance > matchesArray[i + 1].distance) {
					temp = matchesArray[i];
					matchesArray[i] = matchesArray[i + 1];
					matchesArray[i + 1] = temp;
					sorted = false;
				}
			}
		}
		// Remove not so good matces

		int top = (int) (matchesArray.length * 0.1);
		DMatch[] topMatchesArray = new DMatch[top];		
		for (int i=0; i< top; i++) {
			topMatchesArray[i] = matchesArray[i];
			System.out.println("Index: " + i + ", " + topMatchesArray[i]);
		}
		
		MatOfDMatch topMatches = new MatOfDMatch();
	    topMatches.fromArray(topMatchesArray);
		// Draw top matches
		Mat imMatches = new Mat();
		Features2d.drawMatches(offlineBinaryOpen, keyPointsOffline, onlineBinary, keyPointsOnline, topMatches, imMatches);
		Imgcodecs.imwrite("matches.png", imMatches);

		// Extract location of good matches
		MatOfPoint2f points1 = new MatOfPoint2f();
		MatOfPoint2f points2 = new MatOfPoint2f();
		
		List<Point> obj = new ArrayList<>();
	    List<Point> scene = new ArrayList<>();
		
		 KeyPoint[] keyPointsOfflineArray = keyPointsOffline.toArray();
		 KeyPoint[] keyPointsOnlineArray = keyPointsOnline.toArray();
		 
		for (int i = 0; i < topMatches.height(); i++) {			
			obj.add(keyPointsOfflineArray[topMatchesArray[i].queryIdx].pt);
			scene.add(keyPointsOnlineArray[topMatchesArray[i].trainIdx].pt);
		}

		points1.fromList(obj);
		points2.fromList(scene);
		Mat imReg = new Mat();
		// Find homography
		 Mat h = Calib3d.findHomography(points1, points2, Calib3d.RANSAC);

		 System.out.println("Estimated homography : \n" + h);
		// Use homography to warp image
		 Imgproc.warpPerspective(offlinePic, imReg, h, onlinePic.size());
		 Imgcodecs.imwrite("illesztett.png", imReg);
		 
		 
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

	public static void beolvas(File selectedFile, Group onlineData, Group onlineDataTollLent, LineChart<Number, Number> linechart, Text text)
			throws Exception {
		Szoveg szoveg = new Szoveg();
		Line line = new Line();
		Pont pont = new Pont();
		
		onlineData.getChildren().clear();
		linechart.getData().clear();

		System.out.println(selectedFile);
		text.setText(selectedFile.getName());
		List<List<String>> records = setRecords(selectedFile);

		pont.setXY(Integer.parseInt(records.get(1).get(3)), Integer.parseInt(records.get(1).get(2)));
		pont.setT(Integer.parseInt(records.get(1).get(1)));
		pont.setP(Integer.parseInt(records.get(1).get(4)));

		Vonal vonal = new Vonal(pont);
		Vonal.setOsszIdo(0);

		for (int i = 2; i < records.size(); i++) {
			pont.setXY(Integer.parseInt(records.get(i).get(3)), Integer.parseInt(records.get(i).get(2)));
			pont.setT(Integer.parseInt(records.get(i).get(1)));
			pont.setP(Integer.parseInt(records.get(i).get(4)));

			if (vonal.getPontokSzama() < 2) {
				vonal.addPont(pont);

			} else if ((vonal.getSzakaszPont1().getP() != vonal.getSzakaszPont2().getP())
					&& (vonal.getSzakaszPont1().getP() == 0 || vonal.getSzakaszPont2().getP() == 0)
					|| (vonal.getSzakaszIdo() > 10)) {
				szoveg.addVonal(vonal);
				System.out.println(vonal.getVonalGyorsulas());
				vonal = new Vonal(pont);
			} else {

				line = vonalRajz(vonal);				
				onlineData.getChildren().add(line);				
				Line line2 = vonalRajzKek(vonal);
				onlineDataTollLent.getChildren().add(line2);
				
				vonal.addPont(pont);
			}
//			System.out.println(vonal.getSzakaszIdo());
//			System.out.println(vonal.getSzakaszSebesseg());

		}
		
		//sebessegGorbe(linechart, szoveg);
		gyorsulasGorbe(linechart, szoveg);
		System.out.println("Golbális átlagsebesség: " + szoveg.getGlobalisSebesseg());
		onlineData.setScaleX(0.1);
		onlineData.setScaleY(0.1);
		onlineDataTollLent.setScaleX(0.06);
		onlineDataTollLent.setScaleY(0.06);
		return;
	}

	public static void sebessegGorbe(LineChart<Number, Number> linechart, Szoveg szoveg) {
		for (int i = 0; i < szoveg.getVonalak().size(); i++) {
			Series<Number, Number> sorozat = new Series<Number, Number>();
			sorozat.setName("Sebesség");
			sorozat = szoveg.getVonalak().get(i).getSebessegSeries();
			linechart.getData().add(sorozat);
			Node vonalNode = sorozat.getNode().lookup(".chart-series-line");

			if (szoveg.getVonalak().get(i).isTollFent() == true) {
				vonalNode.setStyle("-fx-stroke: #AAAAAA;");

			} else {
				Color[] colors = intervalColors(0, 120, 60); // green to red
				int tempo = (int) Math.floor(szoveg.getVonalak().get(i).getVonalSebesseg() * 5);
				if (tempo >= colors.length) {
					tempo = colors.length - 1;
				}
				String szinKod = colors[tempo].toString().substring(colors[tempo].toString().length() - 8,
						colors[tempo].toString().length() - 2);
				vonalNode.setStyle("-fx-stroke: #" + szinKod + ";");
			}

		}

	}

	public static void gyorsulasGorbe(LineChart<Number, Number> linechart, Szoveg szoveg) {
		for (int i = 0; i < szoveg.getVonalak().size(); i++) {
			Series<Number, Number> sorozat = new Series<Number, Number>();
			sorozat = szoveg.getVonalak().get(i).getGyorsulasSeries();
			linechart.getData().add(sorozat);
			Node vonalNode = sorozat.getNode().lookup(".chart-series-line");

			if (szoveg.getVonalak().get(i).isTollFent() == true) {
				vonalNode.setStyle("-fx-stroke: #AAAAAA;");
			} else {
				vonalNode.setStyle("-fx-stroke: #0000FF;");
//				Color[] colors = intervalColors(0, 120, 60); // green to red
//				System.out.println(szoveg.getVonalak().get(i).getVonalSebesseg());
//				int tempo = (int) Math.floor((szoveg.getVonalak().get(i).getVonalGyorsulas()+1.5) * 20);
//				if (tempo >= colors.length) {
//					tempo = colors.length - 1;
//				}
//				String szinKod = colors[tempo].toString().substring(colors[tempo].toString().length()-8, colors[tempo].toString().length()-2);
//				vonalNode.setStyle("-fx-stroke: #" + szinKod + ";");
			}

		}

	}

	public static Line vonalRajz(Vonal vonal) {
		Line line = new Line();

		if (vonal.isTollFent() == true) {
			line.setStroke(Color.LIGHTGRAY);
			line.setStrokeWidth(15);
		} else {
			Color[] colors = intervalColors(0, 120, 60); // green to red

			int tempo = (int) Math.floor(vonal.getSzakaszSebesseg() * 5);
			if (tempo >= colors.length) {
				tempo = colors.length - 1;
			}
			line.setStroke(colors[tempo]);
			line.setStrokeWidth(45);
//			line.setStroke(Color.BLUE);
		}

		line.setStrokeLineCap(StrokeLineCap.ROUND);
		line.setStartX(vonal.getSzakaszPont1().getX());
		line.setStartY(vonal.getSzakaszPont1().getY());
		line.setEndX(vonal.getSzakaszPont2().getX());
		line.setEndY(vonal.getSzakaszPont2().getY());
		return line;

	}
	
	public static Line vonalRajzKek(Vonal vonal) {
		Line line = new Line();

		if (vonal.isTollFent() == true) {
			line.setStroke(Color.TRANSPARENT);
			line.setStrokeWidth(5);
		} else {			
			Color[] colors = intervalColors(0, 120, 60); // green to red

			int tempo = (int) Math.floor(vonal.getSzakaszSebesseg() * 5);
			if (tempo >= colors.length) {
				tempo = colors.length - 1;
			}
			line.setStroke(colors[tempo]);
			line.setStrokeWidth(55);
		}

		line.setStrokeLineCap(StrokeLineCap.ROUND);
		line.setStartX(vonal.getSzakaszPont1().getX());
		line.setStartY(vonal.getSzakaszPont1().getY());
		line.setEndX(vonal.getSzakaszPont2().getX());
		line.setEndY(vonal.getSzakaszPont2().getY());
		return line;

	}

	public static Color[] intervalColors(float angleFrom, float angleTo, int n) {
		float angleRange = angleTo - angleFrom;
		float stepAngle = angleRange / n;
		Color[] colors = new Color[n];

		for (int i = 0; i < n; i++) {
			float angle = angleFrom + i * stepAngle;
			colors[i] = Color.hsb(angle, 1.0, 0.95);
		}

		for (int i = 0; i < colors.length / 2; i++) {
			Color temp = colors[i];
			colors[i] = colors[colors.length - i - 1];
			colors[colors.length - i - 1] = temp;
		}

		return colors;
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
