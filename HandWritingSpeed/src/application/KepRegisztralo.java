package application;

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

	public void regisztracio() {
		this.getStackpane().getChildren().clear();
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// Mat onlineMindenPont = Imgcodecs.imread("onlineSnapshot.png");
		Mat onlinePic = Imgcodecs.imread("onlineDataTollLent.png");
		Mat offlinePic = Imgcodecs.imread("offlineKep.png");

		Mat offlineBinary = binarisKonverzioOffline(offlinePic);
		Mat onlineBinary = binarisKonverzioOnline(onlinePic);

		Mat offlineBinaryMorph = Morfologia(offlineBinary);
		Mat kernel = Mat.ones(new Size(1.0, 1.0), Imgproc.MORPH_RECT);

		Mat onlineBinaryMorph = new Mat();
		Imgproc.erode(onlineBinary, onlineBinaryMorph, kernel);
		Imgcodecs.imwrite("onlineBinaryMorph.png", onlineBinaryMorph);

		int max = 0;
		Image imageOff = null;
		Image imageOn = null;

		Mat imRegBin = new Mat();
		Mat imReg = new Mat();
		// for (int features = 1500; features >= 50; features -= 50) {
		for (double topMatch = 1; topMatch <= 25; topMatch += 0.5) {
			// Variables to store keypoints and descriptors
			MatOfKeyPoint keyPointsOffline = new MatOfKeyPoint();
			MatOfKeyPoint keyPointsOnline = new MatOfKeyPoint();
			Mat descriptorsOnline = new Mat();
			Mat descriptorsOffline = new Mat();

			// Detect ORB features and compute descriptors.
			ORB orbDetector = ORB.create();

			orbDetector.detectAndCompute(onlineBinaryMorph, new Mat(), keyPointsOnline, descriptorsOnline);
			orbDetector.detectAndCompute(offlineBinaryMorph, new Mat(), keyPointsOffline, descriptorsOffline);

			// Match features.
			try {
				MatOfDMatch matches = new MatOfDMatch();
				DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

				matcher.match(descriptorsOffline, descriptorsOnline, matches, new Mat());

				// Sort matches by score
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
				// Remove not so good matces

				int top = (int) (matchesArray.length * (topMatch / 100));
				DMatch[] topMatchesArray = new DMatch[top];
				for (int i = 0; i < top; i++) {
					topMatchesArray[i] = matchesArray[i];
					// System.out.println("Index: " + i + ", " + topMatchesArray[i]);
				}
				MatOfDMatch topMatches = new MatOfDMatch();
				topMatches.fromArray(topMatchesArray);

				// Draw top matches
				Mat imMatches = new Mat();
				Features2d.drawMatches(offlineBinaryMorph, keyPointsOffline, onlineBinaryMorph, keyPointsOnline,
						topMatches, imMatches);
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

				// Find homography
				Mat homography = new Mat();
				homography = Calib3d.findHomography(points1, points2, Calib3d.RANSAC);

				// Use homography to warp image

				Imgproc.warpPerspective(offlineBinaryMorph, imRegBin, homography, onlinePic.size());

				Imgproc.warpPerspective(offlinePic, imReg, homography, onlinePic.size());

				Mat comp = new Mat();
				Core.compare(onlineBinary, imRegBin, comp, Core.CMP_EQ);
				int similiarPixels = Core.countNonZero(comp);

				if (similiarPixels > max) {
					max = similiarPixels;
//					System.out.println("Igen");

				}
				Imgcodecs.imwrite("illesztettBinaris.png", imRegBin);
				Imgcodecs.imwrite("illesztett.png", imReg);
				imageOff = new Image("file:illesztett.png");
				imageOn = new Image("file:onlineSnapshot.png");
//				System.out.println("Size: " + (onlineBinary.rows() * onlineBinary.cols()) + " Best: " + max);

				offlineIllesztettKep.setImage(imageOff);

				onlineIllesztettKep.setImage(imageOn);
				getStackpane().getChildren().clear();
				getStackpane().getChildren().addAll(offlineIllesztettKep, onlineIllesztettKep);

			} catch (Exception e) {
				getStackpane().getChildren().clear();
				getStackpane().getChildren().add(new Text("Nem sikerült az illesztés"));

			}

			// }
		}

	}

	private static Mat binarisKonverzioOnline(Mat onlinePic) {
		// Konvertálás szürkeárnyalatossá
		Mat onlineGray = new Mat();
		Imgproc.cvtColor(onlinePic, onlineGray, Imgproc.COLOR_RGB2GRAY);

		// Konvertálás binárissá
		Mat onlineBinary = new Mat();
		Imgproc.threshold(onlineGray, onlineBinary, 0, 255, Imgproc.THRESH_BINARY);
		Imgcodecs.imwrite("onlineBinary.png", onlineBinary);

		return onlineBinary;

	}

	public static Mat binarisKonverzioOffline(Mat offlinePic) {
		Mat offlineGray = new Mat();
		Imgproc.cvtColor(offlinePic, offlineGray, Imgproc.COLOR_RGB2GRAY);
		Mat offlineBinary = new Mat();
		Imgproc.threshold(offlineGray, offlineBinary, 247, 255, Imgproc.THRESH_BINARY_INV);
		Imgcodecs.imwrite("offlineBinary.png", offlineBinary);
		return offlineBinary;
	}

	public static Mat Morfologia(Mat offlineBinary) {
		// Morfológiai mûveletek
		Mat offlineBinaryMorph = new Mat();
		Size size = new Size(3.0, 3.0);
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, size);
		Imgproc.morphologyEx(offlineBinary, offlineBinaryMorph, Imgproc.MORPH_OPEN, kernel);
//		Size size2 = new Size(4.0, 4.0);
//		Mat kernel2 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, size2);
//		Imgproc.morphologyEx(offlineBinaryMorph, offlineBinaryMorph, Imgproc.MORPH_CLOSE, kernel2);
//		
//		Mat kernel3 = Mat.ones(new Size(4.0, 4.0), Imgproc.MORPH_RECT);		
//		Imgproc.erode(offlineBinary, offlineBinaryMorph, kernel3);
		Imgcodecs.imwrite("offlineBinaryMorph.png", offlineBinaryMorph);

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

	public void setOfflineIllesztettKep(ImageView bottom) {
		this.offlineIllesztettKep = bottom;
	}

	public ImageView getOnlineIllesztettKep() {
		return onlineIllesztettKep;
	}

	public void setOnlineIllesztettKep(ImageView topp) {
		this.onlineIllesztettKep = topp;
	}

}
