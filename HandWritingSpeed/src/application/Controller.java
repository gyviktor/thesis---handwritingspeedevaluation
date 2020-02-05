package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;

public class Controller {
	
	public static void beolvass(File selectedFile, AnchorPane anchorpane, LineChart<Number, Number> linechart, Text text) throws Exception {
		anchorpane.getChildren().clear();
		System.out.println(selectedFile);
		List<List<String>> records = new ArrayList<>();
		text.setText(selectedFile.getName());
		
		BufferedReader br = new BufferedReader(new FileReader(selectedFile));
		String l;
		while ((l = br.readLine()) != null) {
			String[] values = l.split(";");
			records.add(Arrays.asList(values));
		}
		br.close();

		Group group = new Group();
		//group.setAutoSizeChildren(false);		
		List<Double> points = new ArrayList<Double>();
		List<Double> speeds = new ArrayList<Double>();
		int i = 0;
		int penUp = 0;
		int penDown = 0;
		double t1 = Double.parseDouble(records.get(1).get(1));
		double x1 = Double.parseDouble(records.get(1).get(3));
		double y1 = Double.parseDouble(records.get(1).get(2));
		double p1 = Double.parseDouble(records.get(1).get(4));
		points.add(x1);
		points.add(y1);
		double sumSpeed = 0;

		for (List<String> p : records) {
			if (i > 1) {
				double t2 = Double.parseDouble(p.get(1));
				double x2 = Double.parseDouble(p.get(3));
				double y2 = Double.parseDouble(p.get(2));
				double p2 = Double.parseDouble(p.get(4));
				double s = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
				double t = t2 - t1;
				double v;
				v = t == 0 ?  0 : s / t;
				if((p2 != p1 && (p1 == 0 || p2 == 0)) || t > 10) {
					
				}else {
					Line line = vonal(x1,y1, x2, y2, p1, p2, v);
					group.getChildren().add(line);
				}
				
				/*
				System.out.println("X: " + x1 + " Y: " + y1);
				System.out.println("X: " + x2 + " Y: " + y2);
				System.out.println("Speed: " + v);
				*/
				if(p2 == 0) {
					penDown++;
					sumSpeed+=v;
				}else {
					penUp++;
				}				

				
				/*
				System.out.println("p1: " + p1 + " p2: " + p2);
				if (p2 != p1 && (p1 == 0 || p2 == 0)) {
					double sumSpeed = 0;
					double averageSpeed = 0;
					
					for (double temp : speeds) {
						sumSpeed += temp;
					}
					
					averageSpeed = sumSpeed / speeds.size();
					System.out.println(averageSpeed + " = " + sumSpeed + " / " + speeds.size());
					Polyline polyline = vonal(points, p1, p2, averageSpeed);
					group.getChildren().add(polyline);
					//pane.getChildren().add(polyline);
					points.clear();					
					speeds.clear();

				} else {
					points.add(x2);
					points.add(y2);
					speeds.add(v);
				}				
				*/
				t1 = t2;
				x1 = x2;
				y1 = y2;
				p1 = p2;
			}
			i++;
		}
		System.out.println(i);
		text.setText(text.getText() + "\n Átlag: " + sumSpeed/penDown);
		//anchorpane.setScaleX(0.1);
		//anchorpane.setScaleY(0.1);		
		AnchorPane.setRightAnchor(group, 0.0);
		AnchorPane.setBottomAnchor(group, 0.0);		
		AnchorPane.setLeftAnchor(group, 0.0);
		AnchorPane.setTopAnchor(group, 0.0);
		
		//group.relocate(0, 0);
		anchorpane.setStyle("-fx-background-color: white");
		anchorpane.getChildren().add(group);		
		return;
	}
	
	public static LineChart<Number, Number> speedFunction(){
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
		xAxis.setLabel("Idõ");
		yAxis.setLabel("Sebesség");		
		lineChart.setTitle("Sebességfüggvény");
		return lineChart;		
	}
	
	public static LineChart<Number, Number> speedFunctionData(LineChart<Number, Number> linechart, File selectedFile) throws Exception{
	
		//defining a series
        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
        //populating the series with data
        series.getData().add(new Data<Number, Number>(1, 23));

        
        linechart.getData().add(series);
        
		return linechart;
		
	}

	public static Polyline vonal(List<Double> points, double p1, double p2, double v) {
		Polyline polyline = new Polyline();
		double[] pointsArray = new double[points.size()];
		for (double xy : points) {
			int index = 0;
			pointsArray[index] = xy;
			polyline.getPoints().add(xy);			
		}
		Color[] colors = intervalColors(0, 120, 10); // red to green
		if (p1 == 0) {
			polyline.setStroke(Color.LIGHTGREY);
		} else if (v < 5) {
			polyline.setStroke(Color.GREEN);
		} else if (5 <= v && v <= 10) {
			polyline.setStroke(Color.YELLOW);
		} else if (v > 10) {
			polyline.setStroke(Color.RED);
		}
		polyline.setStrokeWidth(25);
		return polyline;
	}
	
	
	public static Line vonal(double x1,double y1,double x2,double y2,double p1,double p2,double v) {
		Line line = new Line();
		Color[] colors = intervalColors(0, 120, 60); // green to red
		
		int a = (int) Math.floor(v*5);
		if (a>= colors.length) {
			a = colors.length - 1;
		}
		if(p1 == 0 && p2 == 0) {
			line.setStroke(Color.LIGHTGREY);
			line.setStrokeWidth(5);
		}else {
			line.setStroke(colors[a]);
			line.setStrokeWidth(20);
		}
		
		line.setStrokeLineCap(StrokeLineCap.ROUND);
		line.setStartX(x1); 
		line.setStartY(y1); 
		line.setEndX(x2); 
		line.setEndY(y2);
		return line;
		
	}
	
	public static Color[] intervalColors(float angleFrom, float angleTo, int n) {
	    float angleRange = angleTo - angleFrom;
	    float stepAngle = angleRange / n;
	    Color[] colors = new Color[n];
	    
	    for (int i = 0; i < n; i++) {
	        float angle = angleFrom + i*stepAngle;
	        colors[i] = Color.hsb(angle, 1.0, 0.95);        
	    }
	    
	    for(int i=0; i<colors.length/2; i++){
			  Color temp = colors[i];
			  colors[i] = colors[colors.length -i -1];
			  colors[colors.length -i -1] = temp;
			}
	    
	    return colors;
	}

}
