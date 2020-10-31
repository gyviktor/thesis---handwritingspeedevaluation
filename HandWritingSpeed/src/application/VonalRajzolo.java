package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public final class VonalRajzolo {
	
	public static Line vonalRajz(Vonal vonal) {

		Line line = new Line();
		
		if (vonal.isTollFent() == true) {
			line.setStroke(Color.LIGHTGRAY);
			line.setStrokeWidth(15);
		} else {
			Color[] colors = SzinSkala.intervalColors(0, 120, 60); // green to red			

			int tempo = (int) Math.floor(vonal.rajzSebesseg * 5);
			if (tempo >= colors.length) {
				tempo = colors.length - 1;
			}
			line.setStroke(colors[tempo]);
			line.setStrokeWidth(vonal.getSzakaszNyomas()/240);
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
			Color[] colors = SzinSkala.intervalColors(0, 120, 60); // green to red

			int tempo = (int) Math.floor(vonal.getSzakaszSebesseg() * 5);
			if (tempo >= colors.length) {
				tempo = colors.length - 1;
			}
			line.setStroke(colors[tempo]);
			line.setStrokeWidth(64);
		}

		line.setStrokeLineCap(StrokeLineCap.ROUND);
		line.setStartX(vonal.getSzakaszPont1().getX());
		line.setStartY(vonal.getSzakaszPont1().getY());
		line.setEndX(vonal.getSzakaszPont2().getX());
		line.setEndY(vonal.getSzakaszPont2().getY());
		return line;

	}

}
