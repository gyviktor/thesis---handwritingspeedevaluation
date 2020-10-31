package application;

import javafx.scene.paint.Color;

public class SzinSkala {
	
	public SzinSkala(float angleFrom, float angleTo, int n) {
		
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
}
