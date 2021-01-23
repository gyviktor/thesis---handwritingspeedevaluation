package application;

import java.util.ArrayList;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class SzinSkala {
	HBox HBox;
	ArrayList<TextFlow> skala;
	Color[] colors;

	// Letrehoz egy megjelenitett szinskalat
	public SzinSkala() {
		HBox = new HBox(0);
		colors = SzinSkala.intervalColors(0, 120, 60);
		skala = new ArrayList<TextFlow>();
		String szinKod;
		for (int i = 0; i < 60; i++) {

			skala.add(new TextFlow(new Text("   ")));

			szinKod = colors[i].toString().substring(colors[i].toString().length() - 8,
					colors[i].toString().length() - 2);
			skala.get(i).setStyle("-fx-background-color: #" + szinKod + ";");
			HBox.getChildren().add(skala.get(i));
		}
	}

	// Szín intervallum tömbje
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

}
