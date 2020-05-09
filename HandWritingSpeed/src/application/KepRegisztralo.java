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

	public static void regisztracio(StackPane stackpane, ImageView bottom, ImageView topp, double features,
			double topMatch) {
		stackpane.getChildren().clear();
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// Mat onlineMindenPont = Imgcodecs.imread("onlineSnapshot.png");
		Mat onlinePic = Imgcodecs.imread("onlineDataTollLent.png");
		Mat offlinePic = Imgcodecs.imread("offlineKep.png");
		// Konvertálás szürkeárnyalatossá
		Mat onlineGray = new Mat();
		Mat offlineGray = new Mat();
		Imgproc.cvtColor(onlinePic, onlineGray, Imgproc.COLOR_RGB2GRAY);
		Imgproc.cvtColor(offlinePic, offlineGray, Imgproc.COLOR_RGB2GRAY);

		// Konvertálás binárissá
		Mat onlineBinary = new Mat();
		Mat offlineBinary = new Mat();
		Imgproc.threshold(onlineGray, onlineBinary, 0, 255, Imgproc.THRESH_BINARY);
		Imgproc.threshold(offlineGray, offlineBinary, 240, 255, Imgproc.THRESH_BINARY_INV);
		System.out.println("Converted to Binary");
		Imgcodecs.imwrite("onlineBinary.png", onlineBinary);
		Imgcodecs.imwrite("offlineBinary.png", offlineBinary);

		// Morfológiai mûveletek
		Mat offlineBinaryMorph = new Mat();
		Size size = new Size(3.0, 3.0);
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, size);
		Imgproc.morphologyEx(offlineBinary, offlineBinaryMorph, Imgproc.MORPH_OPEN, kernel);
		Size size2 = new Size(3.0, 3.0);
		Mat kernel2 = Imgproc.getStructuringElement(Imgproc.MORPH_CROSS, size2);
		Imgproc.morphologyEx(offlineBinaryMorph, offlineBinaryMorph, Imgproc.MORPH_CLOSE, kernel2);
		Imgcodecs.imwrite("offlineBinaryMorph.png", offlineBinaryMorph);

		// Variables to store keypoints and descriptors
		MatOfKeyPoint keyPointsOffline = new MatOfKeyPoint();
		MatOfKeyPoint keyPointsOnline = new MatOfKeyPoint();
		Mat descriptorsOnline = new Mat();
		Mat descriptorsOffline = new Mat();

		// Detect ORB features and compute descriptors.
		ORB orbDetector = ORB.create((int) features);

		orbDetector.detectAndCompute(onlineBinary, new Mat(), keyPointsOnline, descriptorsOnline);
		orbDetector.detectAndCompute(offlineBinaryMorph, new Mat(), keyPointsOffline, descriptorsOffline);

		// Match features.
		try {
			MatOfDMatch matches = new MatOfDMatch();
			DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

			matcher.match(descriptorsOffline, descriptorsOnline, matches, new Mat());
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

			int top = (int) (matchesArray.length * (topMatch / 100));
			DMatch[] topMatchesArray = new DMatch[top];
			for (int i = 0; i < top; i++) {
				topMatchesArray[i] = matchesArray[i];
				System.out.println("Index: " + i + ", " + topMatchesArray[i]);
			}
			MatOfDMatch topMatches = new MatOfDMatch();
			topMatches.fromArray(topMatchesArray);

			// Draw top matches
			Mat imMatches = new Mat();
			Features2d.drawMatches(offlineBinaryMorph, keyPointsOffline, onlineBinary, keyPointsOnline, topMatches,
					imMatches);
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
			Mat h = Calib3d.findHomography(points1, points2, Calib3d.RANSAC);
			System.out.println("Estimated homography : \n" + h);
			
			// Use homography to warp image
			Mat imReg = new Mat();
			Imgproc.warpPerspective(offlinePic, imReg, h, onlinePic.size());
			Imgcodecs.imwrite("illesztett.png", imReg);

			Image imageOff = new Image("file:illesztett.png");
			Image imageOn = new Image("file:onlineSnapshot.png");
			bottom.setImage(imageOff);
			topp.setImage(imageOn);
			stackpane.getChildren().addAll(bottom, topp);
		} catch (Exception e) {
			stackpane.getChildren().add(new Text("Nem sikerült az illesztés"));
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
