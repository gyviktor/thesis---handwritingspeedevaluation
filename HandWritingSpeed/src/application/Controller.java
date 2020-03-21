package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.ORB;

public class Controller {

	static String path = new String();

	public static String getPath() {
		return path;
	}

	public static void setPath(String path) {
		Controller.path = path;
	}

	public static void loadOnline(Stage primaryStage, ScrollPane scrollPaneOnline, DoubleProperty zoomPropertyOnline,
			ImageView onlineImage, LineChart<Number, Number> linechart, Text text) {
		Group onlineData = new Group();
		FileChooser filechooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV, HWR files (*.csv), (*.hwr)",
				"*.csv", "*.hwr");
		filechooser.getExtensionFilters().add(extFilter);

		try {
			File selectedFile = filechooser.showOpenDialog(primaryStage);
			Controller.beolvass(selectedFile, onlineData, linechart, text);
			saveAsPng(onlineData, "onlineSnapshot.png");			
			Image image = new Image("file:onlineSnapshot.png");
			onlineImage.setImage(image);
			onlineImage.preserveRatioProperty().set(true);
			zoomPropertyOnline.set(image.getWidth());
			scrollPaneOnline.setContent(onlineImage);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public static void loadOffline(Stage primaryStage, ScrollPane scrollPaneOffline, DoubleProperty zoomPropertyOffline,
			ImageView offlineImage) {
		FileChooser filechooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"JPG, PNG, GIF files (*.jpg), (*.png), (*.gif)", "*.jpg", "*.png", "*.gif");
		filechooser.getExtensionFilters().add(extFilter);

		try {
			File selectedFile = filechooser.showOpenDialog(primaryStage);
			setPath(selectedFile.getAbsolutePath());
			Image image = new Image(selectedFile.toURI().toString());
			offlineImage.setImage(image);
			offlineImage.preserveRatioProperty().set(true);
			zoomPropertyOffline.set(image.getWidth());
			scrollPaneOffline.setContent(offlineImage);
		} catch (Exception e1) {
			// e1.printStackTrace();
		}
	}

	public static void imageRegistration() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat onlinePic = Imgcodecs.imread("onlineSnapshot.png");
		Mat offlinePic = Imgcodecs.imread(getPath());
		Mat onlineGray = new Mat();
		Mat offlineGray = new Mat();
		Imgproc.cvtColor(onlinePic, onlineGray, Imgproc.COLOR_RGB2GRAY);
		Imgproc.cvtColor(offlinePic, offlineGray, Imgproc.COLOR_RGB2GRAY);
		System.out.println("Converted to Grayscale");
		Mat onlineBinary = new Mat();
		Mat offlineBinary = new Mat();
		Imgproc.threshold(onlineGray, onlineBinary, 0, 255, Imgproc.THRESH_BINARY_INV);
		Imgproc.threshold(offlineGray, offlineBinary, 240, 255, Imgproc.THRESH_BINARY);
		System.out.println("Converted to Binary");
		//Imgcodecs.imwrite("onlineBinary.png", onlineBinary);
		//Imgcodecs.imwrite("offlineBinary.png", offlineBinary);

		// Variables to store keypoints and descriptors
		  MatOfKeyPoint keyPointsOffline = new MatOfKeyPoint();
		  MatOfKeyPoint keyPointsOnline = new MatOfKeyPoint();
		  Mat descriptorsOnline = new Mat(); 
		  Mat descriptorsOffline = new Mat();
		  
		  // Detect ORB features and compute descriptors.
		  ORB orbDetector = ORB.create(100);		  
		  orbDetector.detectAndCompute(onlineBinary, new Mat(), keyPointsOnline, descriptorsOnline); 
		  orbDetector.detectAndCompute(offlineBinary, new Mat(), keyPointsOffline, descriptorsOffline);
		  
		// Match features.
		  MatOfDMatch matches = new MatOfDMatch();		  
		  DescriptorMatcher matcher =  DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
		  matcher.match(descriptorsOnline, descriptorsOffline, matches);

		// Sort matches by score
		  System.out.println(matches.cols() + "  rows: " + matches.rows());
		 DMatch[] matchesArray = matches.toArray();
		 MatOfDMatch topMatches = new MatOfDMatch();
		 
		 boolean sorted = false;
		    DMatch temp;
		    while(!sorted) {
		        sorted = true;
		        for (int i = 0; i < matchesArray.length - 1; i++) {
		            if (matchesArray[i].distance > matchesArray[i+1].distance) {
		                temp = matchesArray[i];
		                matchesArray[i] = matchesArray[i+1];
		                matchesArray[i+1] = temp;
		                sorted = false;
		            }
		        }
		    }
		    
		    
		    //topMatches.fromArray(matchesArray);
		 //System.out.println(topMatches.cols() + "  rows: " + topMatches.rows());
		  // Remove not so good matces 
		 //int numGoodMatches = (int) (matchesArray.length * 0.15); 

		 		 
		// Draw top matches
		  Mat imMatches = new Mat(); 
		  Features2d.drawMatches(onlinePic, keyPointsOnline, offlinePic, keyPointsOffline, matches, imMatches);
		  Imgcodecs.imwrite("matches.png", imMatches);
		  
		  //System.out.println(matches.width() + " height:  " + matches.height()); 
		  
		  // Extract location of good matches
		  MatOfPoint2f points1 = new MatOfPoint2f(); 
		  MatOfPoint2f points2 = new MatOfPoint2f();
		  
		  for (int i = 0; i<topMatches.height(); i++) {
			  points1.push_back(keyPointsOnline.row(matchesArray[i].queryIdx));
			  points2.push_back(keyPointsOffline.row(matchesArray[i].trainIdx));
		  }

		 Mat imReg = new Mat();
		// Find homography
		 //Mat h = Calib3d.findHomography(points1, points2, Calib3d.RANSAC);
		// Use homography to warp image
		 //Imgproc.warpPerspective(onlinePic, imReg, h, onlinePic.size());
		 //Imgcodecs.imwrite("illesztett.png", imReg);
		 //System.out.println("Estimated homography : \n" + h);
		 
		 
		 /*
			final int warpMode = Video.MOTION_HOMOGRAPHY;
			Mat warpMatrix = Mat.eye(3, 3, CvType.CV_32F);
			int numbeOfIterations = 500;
			double terminationEps = 1e-5;
			TermCriteria criteria = new TermCriteria(TermCriteria.COUNT + TermCriteria.EPS, numbeOfIterations,
					terminationEps);
			Video.findTransformECC(onlineBinary, offlineBinary, warpMatrix, warpMode, criteria, new Mat(), 5);
			Mat allignedImage = new Mat();
			Imgproc.warpPerspective(offlinePic, allignedImage, warpMatrix, offlinePic.size(),
					Imgproc.INTER_LINEAR + Imgproc.CV_WARP_INVERSE_MAP);
			Imgcodecs.imwrite("alligned.png", allignedImage);
	*/
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

	public static void beolvass(File selectedFile, Group onlineData, LineChart<Number, Number> linechart, Text text)
			throws Exception {
		Szoveg szoveg = new Szoveg();
		Line line = new Line();
		Pont pont = new Pont();
		int vonalDb = 0;
		Group onlineDataTollLent = new Group();
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
				vonalDb++;
			} else {
				
				line = vonalRajz(vonal);
				if(vonal.isTollFent() == false) {
					onlineDataTollLent.getChildren().add(line);
						
				}
				onlineData.getChildren().add(line);
				vonal.addPont(pont);
			}
//			System.out.println(vonal.getSzakaszIdo());
//			System.out.println(vonal.getSzakaszSebesseg());
			
			

		}
		saveAsPng(onlineDataTollLent, "onlineDataTollLent.png");
		sebessegGorbe(linechart, szoveg);
		gyorsulasGorbe(linechart, szoveg);
//		System.out.println("Vonalak száma: " + vonalDb);
		System.out.println("Golbális átlagsebesség: " + szoveg.getGlobalisSebesseg());
		onlineData.setScaleX(0.05);
		onlineData.setScaleY(0.05);

		return;
	}
	

	public static void sebessegGorbe(LineChart<Number, Number> linechart, Szoveg szoveg) {
		for (int i = 0; i < szoveg.getVonalak().size(); i++) {
			Series <Number, Number> sorozat = new Series<Number, Number>();
			sorozat.setName("Sebesség");
			sorozat = szoveg.getVonalak().get(i).getSebessegSeries();
			linechart.getData().add(sorozat);
			Node vonalNode = sorozat.getNode().lookup(".chart-series-line");
			
			if(szoveg.getVonalak().get(i).isTollFent() == true) {
				vonalNode.setStyle("-fx-stroke: #AAAAAA;");
				
			}else {
				Color[] colors = intervalColors(0, 120, 60); // green to red
				int tempo = (int) Math.floor(szoveg.getVonalak().get(i).getVonalSebesseg() * 5);
				if (tempo >= colors.length) {
					tempo = colors.length - 1;
				}
				String szinKod = colors[tempo].toString().substring(colors[tempo].toString().length()-8, colors[tempo].toString().length()-2);
				vonalNode.setStyle("-fx-stroke: #" + szinKod + ";");
			}
			
		}

	}
	
	public static void gyorsulasGorbe(LineChart<Number, Number> linechart, Szoveg szoveg) {
		for (int i = 0; i < szoveg.getVonalak().size(); i++) {
			Series <Number, Number> sorozat = new Series<Number, Number>();
			sorozat = szoveg.getVonalak().get(i).getGyorsulasSeries();
			linechart.getData().add(sorozat);
			Node vonalNode = sorozat.getNode().lookup(".chart-series-line");
			
			if(szoveg.getVonalak().get(i).isTollFent() == true) {
				vonalNode.setStyle("-fx-stroke: #AAAAAA;");
			}else {
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
			line.setStroke(Color.TRANSPARENT);
			line.setStrokeWidth(10);
		} else {
			Color[] colors = intervalColors(0, 120, 60); // green to red

			int tempo = (int) Math.floor(vonal.getSzakaszSebesseg() * 5);
			if (tempo >= colors.length) {
				tempo = colors.length - 1;
			}
			line.setStroke(colors[tempo]);
			line.setStrokeWidth(45);
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
	
	public static final void saveAsPng(final Node NODE, String fileNev) {
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
