package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.ORB;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public final class KepRegisztralo {
	private StackPane stackpane;
	private ImageView offlineIllesztettKep;
	private ImageView onlineIllesztettKep;

	public KepRegisztralo() {
		stackpane = new StackPane();
		offlineIllesztettKep = new ImageView();
		onlineIllesztettKep = new ImageView();
	}

	public void regisztracio(String fileNev) {
		this.getStackpane().getChildren().clear();
		// opencv betoltese
		File lib = new File("opencv-3.4.9/" + System.mapLibraryName("opencv_java349"));
		System.load(lib.getAbsolutePath());

		// on-line es off-line kep betoltese
		Mat onlinePic = Imgcodecs.imread("images/" + fileNev + "_onlineDataTollLent.png");
		Mat offlinePic = Imgcodecs.imread("images/" + fileNev + "_offlineKep.png");

		// binarissa alakitas
		Mat offlineBinary = binarisKonverzioOffline(offlinePic, fileNev);
		Mat onlineBinary = binarisKonverzioOnline(onlinePic, fileNev);

		// morfologia
		Mat offlineBinaryMorph = Morfologia(offlineBinary, fileNev);

		int max = 0;
		Image imageOff = null;
		Image imageOn = null;

		Mat imRegBin = new Mat();
		Mat imReg = new Mat();
		Mat best = new Mat();

		for (double topMatch = 1; topMatch <= 25; topMatch += 0.5) {
			// kulcspontokat and descriptor-okat tarolo valtozok
			MatOfKeyPoint keyPointsOffline = new MatOfKeyPoint();
			MatOfKeyPoint keyPointsOnline = new MatOfKeyPoint();
			Mat descriptorsOnline = new Mat();
			Mat descriptorsOffline = new Mat();

			// ORB feature detektalas es descriptor szamitas
			ORB orbDetector = ORB.create();

			orbDetector.detectAndCompute(onlineBinary, new Mat(), keyPointsOnline, descriptorsOnline);
			orbDetector.detectAndCompute(offlineBinaryMorph, new Mat(), keyPointsOffline, descriptorsOffline);

			// feature parositas
			try {
				MatOfDMatch matches = new MatOfDMatch();
				DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

				matcher.match(descriptorsOffline, descriptorsOnline, matches, new Mat());

				// Parok rendezese ertek alapjan
				DMatch[] matchesArray = matches.toArray();
				boolean sorted = false;
				DMatch temp = new DMatch();
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

				// Kevesbe jo parok eldobasa
				int top = (int) (matchesArray.length * (topMatch / 100));
				DMatch[] topMatchesArray = new DMatch[top];
				for (int i = 0; i < top; i++) {
					topMatchesArray[i] = matchesArray[i];
				}
				MatOfDMatch topMatches = new MatOfDMatch();
				topMatches.fromArray(topMatchesArray);

				// Legjobb talalatok osszekotese
				Mat imMatches = new Mat();
				Features2d.drawMatches(offlineBinaryMorph, keyPointsOffline, onlineBinary, keyPointsOnline, topMatches,
						imMatches);
				Imgcodecs.imwrite("images/" + fileNev + "matches.png", imMatches);

				// A jo parok poziciojanak kinyerese
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

				// Homografia megallapitasa
				Mat homography = new Mat();
				homography = Calib3d.findHomography(points1, points2, Calib3d.RANSAC);

				// Kepek illesztese a homografia hasznalataval
				Imgproc.warpPerspective(offlinePic, imReg, homography, onlinePic.size());
				Imgproc.warpPerspective(offlineBinaryMorph, imRegBin, homography, onlinePic.size());

				// On-line binaris es off-line illesztett binaris hasonlo pixeleinek
				// osszehasonlitasa
				Mat comp = new Mat();
				Core.compare(onlineBinary, imRegBin, comp, Core.CMP_EQ);
				int similiarPixels = Core.countNonZero(comp);

				// Ha tobb hasonlosagot talalunk eltaroljuk
				if (similiarPixels > max) {
					max = similiarPixels;
					best = imReg.clone();
				}		

			} catch (Exception e) {
//				System.out.println(e);
				getStackpane().getChildren().clear();
				getStackpane().getChildren().add(new Text("Nem sikerült az illesztés"));

			}

		}
		// A legjobb illesztest egymasra tesszuk
		Imgcodecs.imwrite("images/" + fileNev + "_illesztett.png", best);
		imageOff = new Image("file:images/" + fileNev + "_illesztett.png");
		imageOn = new Image("file:images/" + fileNev + "_onlineSnapshot.png");

		offlineIllesztettKep.setImage(imageOff);
		onlineIllesztettKep.setImage(imageOn);

		getStackpane().getChildren().clear();
		getStackpane().getChildren().addAll(offlineIllesztettKep, onlineIllesztettKep);

	}

	// on-line kep binarissa alakitasa
	private static Mat binarisKonverzioOnline(Mat onlinePic, String fileNev) {
		// Konvertálás szürkeárnyalatossá
		Mat onlineGray = new Mat();
		Imgproc.cvtColor(onlinePic, onlineGray, Imgproc.COLOR_RGB2GRAY);

		// Konvertálás binárissá
		Mat onlineBinary = new Mat();
		Imgproc.threshold(onlineGray, onlineBinary, 0, 255, Imgproc.THRESH_BINARY);
//		Imgcodecs.imwrite("images/" + fileNev + "_onlineBinary.png", onlineBinary);
		return onlineBinary;

	}

	// off-line kep binarissa alakitasa
	public static Mat binarisKonverzioOffline(Mat offlinePic, String fileNev) {
		// Konvertálás szürkeárnyalatossá
		Mat offlineGray = new Mat();
		Imgproc.cvtColor(offlinePic, offlineGray, Imgproc.COLOR_RGB2GRAY);

		// Konvertálás binárissá
		Mat offlineBinary = new Mat();
		Imgproc.threshold(offlineGray, offlineBinary, 247, 255, Imgproc.THRESH_BINARY_INV);
//		Imgcodecs.imwrite("images/" + fileNev + "_offlineBinary.png", offlineBinary);
		return offlineBinary;
	}

	// Morfologiai nyitas muvelet
	public static Mat Morfologia(Mat offlineBinary, String fileNev) {
		Mat offlineBinaryMorph = new Mat();
		Size size = new Size(3.0, 3.0);
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, size);
		Imgproc.morphologyEx(offlineBinary, offlineBinaryMorph, Imgproc.MORPH_OPEN, kernel);
//		Imgcodecs.imwrite("images/" + fileNev + "_offlineBinaryMorph.png", offlineBinaryMorph);
		return offlineBinaryMorph;
	}

	public StackPane getStackpane() {
		return stackpane;
	}

	public void setStackpane(StackPane stackpane) {
		this.stackpane = stackpane;
	}

	public ImageView getOfflineIllesztettKep() {
		return offlineIllesztettKep;
	}

	public ImageView getOnlineIllesztettKep() {
		return onlineIllesztettKep;
	}

}
