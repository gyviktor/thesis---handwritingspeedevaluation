package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public final class VonalRajz {
	Group group;
	static Color[] colors = SzinSkala.intervalColors(0, 120, 60); // green to red

	public VonalRajz() {
		group = new Group();
	}

	// Megrazjolja a szoveget tempo szerinti szinekkel, nyomas szerinti vastagsaggal, megadhato meret skalazassal
	public void rajzol(Szoveg szoveg, double scale, boolean nyomas) {

		for (Vonal vonal : szoveg.getVonalak()) {
			for (int i = 1; i < vonal.getPontok().size()-1; i++) {
				Line line = new Line();
				line.setStrokeLineCap(StrokeLineCap.ROUND);

				if (vonal.isTollFent() && !nyomas) {
					line.setStroke(Color.TRANSPARENT);
					line.setStrokeWidth(0);
				} else if (vonal.isTollFent() && nyomas) {
					line.setStroke(Color.LIGHTGRAY);
					line.setStrokeWidth(15);

				} else if (!vonal.isTollFent() && !nyomas) {
					line.setStroke(Color.BLUE);
					line.setStrokeWidth(70);
				} else {

					int tempo = (int) Math.floor(vonal.getSzakaszSebessegListaG().get(i) * 10);
					if (tempo >= colors.length) {
						tempo = colors.length - 1;
					} else if (tempo < 0) {
						tempo = 0;
					}
					line.setStroke(colors[tempo]);
					line.setStrokeWidth(vonal.getSzakaszNyomasLista().get(i) / 240);
				}

				line.setStartX(vonal.getPontok().get(i).getX());
				line.setStartY(vonal.getPontok().get(i).getY());
				line.setEndX(vonal.getPontok().get(i + 1).getX());
				line.setEndY(vonal.getPontok().get(i + 1).getY());
				group.getChildren().add(line);

			}
		}
		group.setScaleX(scale);
		group.setScaleY(scale);

	}

}
