package application;

import java.util.ArrayList;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

public class Vonal {
	private ArrayList<Pont> pontok = new ArrayList<Pont>();
	private Pont szakaszPont1 = new Pont();
	private Pont szakaszPont2 = new Pont();
	public static double osszIdo;
	private double szakaszIdo, vonalIdo;
	private boolean tollFent;
	public double szakaszSebessegElozo = 0;

	private double szakaszHossz, szakaszNyomas;
	private double szakaszSebesseg, szakaszSebessegH, szakaszSebessegV;

	private double vonalHossz, vonalNyomas;
	private double vonalSebessegAtlag, vonalSebessegH, vonalSebessegV;
	private double minH = Double.MAX_VALUE, minV = Double.MAX_VALUE, maxH = 0, maxV = 0;

	private ArrayList<Double> szakaszIdoListaG = new ArrayList<Double>();
	private ArrayList<Double> vonalIdoListaG = new ArrayList<Double>();
	private ArrayList<Double> szakaszNyomasLista = new ArrayList<Double>();
	private ArrayList<Double> szakaszSebessegListaG = new ArrayList<Double>();
	private ArrayList<Double> szakaszSebessegListaH = new ArrayList<Double>();
	private ArrayList<Double> szakaszSebessegListaV = new ArrayList<Double>();
	private ArrayList<Double> szakaszGyorsulasListaG = new ArrayList<Double>();
	private ArrayList<Double> szakaszGyorsulasListaH = new ArrayList<Double>();
	private ArrayList<Double> szakaszGyorsulasListaV = new ArrayList<Double>();

	private XYChart.Series<Number, Number> xSeries = new XYChart.Series<Number, Number>();
	private XYChart.Series<Number, Number> ySeries = new XYChart.Series<Number, Number>();
	private XYChart.Series<Number, Number> nyomasSeriesG = new XYChart.Series<Number, Number>();
	private XYChart.Series<Number, Number> sebessegSeriesG = new XYChart.Series<Number, Number>();
	private XYChart.Series<Number, Number> sebessegSeriesH = new XYChart.Series<Number, Number>();
	private XYChart.Series<Number, Number> sebessegSeriesV = new XYChart.Series<Number, Number>();
	private XYChart.Series<Number, Number> gyorsulasSeriesG = new XYChart.Series<Number, Number>();
	private XYChart.Series<Number, Number> gyorsulasSeriesH = new XYChart.Series<Number, Number>();
	private XYChart.Series<Number, Number> gyorsulasSeriesV = new XYChart.Series<Number, Number>();

	public Vonal(Pont pont) {
		super();
		if (pont.getP() == 0) {
			this.setTollFent(true);
		} else {
			this.setTollFent(false);
		}

		pontok.add(pont);
		this.setxSeries(pont.getX());
		this.setySeries(pont.getY());
		this.setVonalNyomas(pont.getP());
		this.setNyomasSeriesG(osszIdo, pont.getP());
		this.setMinMaxHV(pont.getX(), pont.getY());

	}

	public void addPont(Pont pont) {
		Pont ujPont = new Pont(pont.getX(), pont.getY(), pont.getP(), pont.getT());
		pontok.add(ujPont);

		this.setVonalNyomas(ujPont.getP());
		this.setMinMaxHV(ujPont.getX(), ujPont.getY());

		this.setSzakaszPont1(pontok.get(pontok.size() - 2));
		this.setSzakaszPont2(pontok.get(pontok.size() - 1));

		this.setVonal(this.getSzakaszPont1(), this.getSzakaszPont2());

	}

	public void setVonal(Pont pont1, Pont pont2) {
		this.setSzakaszHossz(pont1, pont2);
		this.setSzakaszIdo(pont1, pont2);
		this.setVonalIdo();
		this.setVonalIdoListaG();
		this.setSzakaszNyomas(pont1, pont2);
		this.setSzakaszSebessegH(pont1, pont2);
		this.setSzakaszSebessegV(pont1, pont2);
		szakaszSebessegElozo = this.getSzakaszSebesseg();
		this.setSzakaszSebesseg();

		this.setxSeries(pont2.getX());
		this.setySeries(pont2.getY());

		this.setVonalHossz();

		this.setVonalSebessegAtlagG();
		this.setVonalSebessegH();
		this.setVonalSebessegV();
	}

	public ArrayList<Pont> getPontok() {
		return pontok;
	}

	public static double getOsszIdo() {
		return osszIdo;
	}

	public static void setOsszIdo(double osszIdo) {
		Vonal.osszIdo = osszIdo;
	}

	public Pont getSzakaszPont1() {
		return szakaszPont1;
	}

	public void setSzakaszPont1(Pont pont) {
		this.szakaszPont1.setXY(pont.getX(), pont.getY());
		this.szakaszPont1.setP(pont.getP());
		this.szakaszPont1.setT(pont.getT());
	}

	public Pont getSzakaszPont2() {
		return szakaszPont2;
	}

	public void setSzakaszPont2(Pont pont) {
		this.szakaszPont2.setXY(pont.getX(), pont.getY());
		this.szakaszPont2.setP(pont.getP());
		this.szakaszPont2.setT(pont.getT());
	}

	public boolean isTollFent() {
		return tollFent;
	}

	private void setTollFent(boolean tollFent) {
		this.tollFent = tollFent;
	}

	public double getSzakaszNyomas() {
		return szakaszNyomas;
	}

	public void setSzakaszNyomas(Pont pont1, Pont pont2) {
		if (this.isTollFent() == false) {
			this.szakaszNyomas = (pont1.getP() + pont2.getP()) / 2;
		}
		setSzakaszNyomasLista(this.getSzakaszNyomas());
	}

	public ArrayList<Double> getSzakaszNyomasLista() {
		return szakaszNyomasLista;
	}

	public void setSzakaszNyomasLista(double szakaszNyomas) {
		this.szakaszNyomasLista.add(szakaszNyomas);
	}

	public double getVonalNyomas() {
		return vonalNyomas / (pontok.size() - 1);
	}

	public void setVonalNyomas(double szakaszNyomas) {
		this.vonalNyomas += szakaszNyomas;
	}

	public double getSzakaszIdo() {
		return szakaszIdo;
	}

	public void setSzakaszIdo(Pont pont1, Pont pont2) {
		if (pont2.getT() - pont1.getT() == 0) {
			this.szakaszIdo = 0.001;
		} else if ((pont2.getT() - pont1.getT()) < 8 && (pont2.getT() - pont1.getT()) > 4) {
			this.szakaszIdo = 0.005;
		} else {
			this.szakaszIdo = Math.abs(pont2.getT() - pont1.getT()) * 0.001;
		}
		this.szakaszIdoListaG.add(this.szakaszIdo);

	}

	public ArrayList<Double> getVonalIdoListaG() {
		return vonalIdoListaG;
	}

	public void setVonalIdoListaG() {
		this.vonalIdoListaG.add(osszIdo);
	}

	public double getVonalIdo() {
		return vonalIdo;
	}

	public void setVonalIdo() {
		osszIdo += this.szakaszIdo;
		this.vonalIdo += this.szakaszIdo;
	}

	public double getSzakaszHossz() {
		return szakaszHossz;
	}

	public void setSzakaszHossz(Pont pont1, Pont pont2) {
		this.szakaszHossz = Math
				.sqrt(Math.pow(pont2.getX() - pont1.getX(), 2) + Math.pow(pont2.getY() - pont1.getY(), 2)) * 0.0005;
	}

	public double getVonalHossz() {
		return vonalHossz;
	}

	public void setVonalHossz() {
		this.vonalHossz += this.getSzakaszHossz();
	}

	//
	// SEBESSEG
	//

	public double getSzakaszSebesseg() {
		return szakaszSebesseg;
	}

	public void setSzakaszSebesseg() {
		this.szakaszSebesseg = this.getSzakaszHossz() / this.getSzakaszIdo();
		this.szakaszSebessegListaG.add(this.szakaszSebesseg);
	}

	public ArrayList<Double> getSzakaszSebessegListaG() {
		return szakaszSebessegListaG;
	}

	public double getVonalSebessegAtlagG() {
		return vonalSebessegAtlag / (this.getSzakaszSebessegListaG().size() - 1);
	}

	public void setVonalSebessegAtlagG() {
		this.vonalSebessegAtlag += this.getSzakaszSebesseg();
	}

	public XYChart.Series<Number, Number> getSebessegSeries() {
		for (int i = 0; i < this.getSzakaszSebessegListaG().size(); i++) {
			if (this.getSzakaszSebessegListaG().get(i) > 30) {
				sebessegSeriesG.getData().add(new Data<Number, Number>(this.getVonalIdoListaG().get(i), 0.0));
			} else {
				sebessegSeriesG.getData().add(new Data<Number, Number>(this.getVonalIdoListaG().get(i),
						this.getSzakaszSebessegListaG().get(i)));
			}

		}
		return sebessegSeriesG;
	}

	public XYChart.Series<Number, Number> getSebessegSeriesH() {
		for (int i = 0; i < this.getSzakaszSebessegListaH().size(); i++) {
			if (this.getSzakaszSebessegListaH().get(i) > 30) {
				sebessegSeriesH.getData().add(new Data<Number, Number>(this.getVonalIdoListaG().get(i), 0.0));
			} else {
				sebessegSeriesH.getData().add(new Data<Number, Number>(this.getVonalIdoListaG().get(i),
						this.getSzakaszSebessegListaH().get(i)));
			}

		}
		return sebessegSeriesH;
	}

	public XYChart.Series<Number, Number> getSebessegSeriesV() {
		for (int i = 0; i < this.getSzakaszSebessegListaV().size(); i++) {
			if (this.getSzakaszSebessegListaV().get(i) > 30) {
				sebessegSeriesV.getData().add(new Data<Number, Number>(this.getVonalIdoListaG().get(i), 0.0));
			} else {
				sebessegSeriesV.getData().add(new Data<Number, Number>(this.getVonalIdoListaG().get(i),
						this.getSzakaszSebessegListaV().get(i)));
			}

		}
		return sebessegSeriesV;
	}

	public double getSzakaszSebessegH() {
		return szakaszSebessegH;
	}

	public void setSzakaszSebessegH(Pont pont1, Pont pont2) {
		this.szakaszSebessegH = Math.abs(pont2.getX() - pont1.getX()) * 0.0005 / this.getSzakaszIdo();
		this.szakaszSebessegListaH.add(this.szakaszSebessegH);
	}

	public ArrayList<Double> getSzakaszSebessegListaH() {
		return szakaszSebessegListaH;
	}

	public double getSzakaszSebessegV() {
		return szakaszSebessegV;
	}

	public void setSzakaszSebessegV(Pont pont1, Pont pont2) {
		this.szakaszSebessegV = Math.abs(pont2.getY() - pont1.getY()) * 0.0005 / this.getSzakaszIdo();
		this.szakaszSebessegListaV.add(this.szakaszSebessegV);
	}

	public ArrayList<Double> getSzakaszSebessegListaV() {
		return szakaszSebessegListaV;
	}

	public double getVonalSebessegH() {
		return vonalSebessegH / (this.getSzakaszSebessegListaH().size() - 1);
	}

	public void setVonalSebessegH() {
		this.vonalSebessegH += this.getSzakaszSebessegH();
	}

	public double getVonalSebessegV() {
		return vonalSebessegV / (this.getSzakaszSebessegListaV().size() - 1);
	}

	public void setVonalSebessegV() {
		this.vonalSebessegV += this.getSzakaszSebessegV();
	}

	//
	// GYORSULÁS
	//

	public XYChart.Series<Number, Number> getGyorsulasSeries() {
		for (int i = 0; i < this.getSzakaszGyorsulasListaG().size(); i++) {
			if (this.getSzakaszGyorsulasListaG().get(i) > 500 || this.getSzakaszGyorsulasListaG().get(i) < -500) {
				this.gyorsulasSeriesG.getData().add(new Data<Number, Number>(this.getVonalIdoListaG().get(i), 0));
			} else {
				this.gyorsulasSeriesG.getData().add(new Data<Number, Number>(this.getVonalIdoListaG().get(i),
						this.getSzakaszGyorsulasListaG().get(i)));
			}
		}
		return gyorsulasSeriesG;
	}

	public XYChart.Series<Number, Number> getGyorsulasSeriesH() {
		for (int i = 0; i < this.getSzakaszGyorsulasListaH().size(); i++) {
			if (this.getSzakaszGyorsulasListaH().get(i) > 500 || this.getSzakaszGyorsulasListaH().get(i) < -500) {
				this.gyorsulasSeriesH.getData().add(new Data<Number, Number>(this.getVonalIdoListaG().get(i), 0));
			} else {
				this.gyorsulasSeriesH.getData().add(new Data<Number, Number>(this.getVonalIdoListaG().get(i),
						this.getSzakaszGyorsulasListaH().get(i)));
			}

		}
		return gyorsulasSeriesH;
	}

	public XYChart.Series<Number, Number> getGyorsulasSeriesV() {
		for (int i = 0; i < this.getSzakaszGyorsulasListaV().size(); i++) {
			if (this.getSzakaszGyorsulasListaV().get(i) > 500 || this.getSzakaszGyorsulasListaV().get(i) < -500) {
				this.gyorsulasSeriesV.getData().add(new Data<Number, Number>(this.getVonalIdoListaG().get(i), 0));
			} else {
				this.gyorsulasSeriesV.getData().add(new Data<Number, Number>(this.getVonalIdoListaG().get(i),
						this.getSzakaszGyorsulasListaV().get(i)));
			}
		}
		return gyorsulasSeriesV;
	}

	public void setSzakaszGyorsulasListaG() {
		this.szakaszGyorsulasListaG.add(0.0);
		if (this.getSzakaszSebessegListaG().size() > 1) {
			for (int i = 1; i < this.getSzakaszSebessegListaG().size() - 2; i++) {
				double sebessegValtozas = this.getSzakaszSebessegListaG().get(i + 1)
						- this.getSzakaszSebessegListaG().get(i);
//				if (sebessegValtozas / this.szakaszIdoListaG.get(i) > 500
//						|| sebessegValtozas / this.szakaszIdoListaG.get(i) < -500) {
//					this.szakaszGyorsulasListaG.add(0.0);
//				} else {
				this.szakaszGyorsulasListaG.add(sebessegValtozas / this.szakaszIdoListaG.get(i));
//				}
			}
			this.szakaszGyorsulasListaG.add(0.0);
		}
	}


	public ArrayList<Double> getSzakaszGyorsulasListaG() {
		return szakaszGyorsulasListaG;
	}

	public void setSzakaszGyorsulasH() {
		this.szakaszGyorsulasListaH.add(0.0);
		if (this.getSzakaszSebessegListaH().size() > 1) {
			for (int i = 1; i < this.getSzakaszSebessegListaH().size() - 2; i++) {
				double sebessegValtozas = this.getSzakaszSebessegListaH().get(i + 1)
						- this.getSzakaszSebessegListaH().get(i);
				this.szakaszGyorsulasListaH.add(sebessegValtozas / this.szakaszIdoListaG.get(i));
			}
			this.szakaszGyorsulasListaH.add(0.0);
		}
	}

	public ArrayList<Double> getSzakaszGyorsulasListaH() {
		return szakaszGyorsulasListaH;
	}

	public void setSzakaszGyorsulasV() {
		this.szakaszGyorsulasListaV.add(0.0);
		if (this.getSzakaszSebessegListaV().size() > 1) {
			for (int i = 1; i < this.getSzakaszSebessegListaV().size() - 2; i++) {
				double sebessegValtozas = this.getSzakaszSebessegListaV().get(i + 1)
						- this.getSzakaszSebessegListaV().get(i);
				this.szakaszGyorsulasListaV.add(sebessegValtozas / this.szakaszIdoListaG.get(i));
			}
			this.szakaszGyorsulasListaV.add(0.0);
		}
	}

	public ArrayList<Double> getSzakaszGyorsulasListaV() {
		return szakaszGyorsulasListaV;
	}

	public void setMinMaxHV(int x, int y) {
		if (x > this.getMaxH())
			this.maxH = x;
		if (x < this.getMinH())
			this.minH = x;
		if (y > this.getMaxV())
			this.maxV = y;
		if (y < this.getMinV())
			this.minV = y;
	}

	public double getMinH() {
		return minH;
	}

	public double getMinV() {
		return minV;
	}

	public double getMaxH() {
		return maxH;
	}

	public double getMaxV() {
		return maxV;
	}

	public XYChart.Series<Number, Number> getxSeries() {
		return xSeries;
	}

	public void setxSeries(int x) {
		xSeries.getData().add(new Data<Number, Number>(osszIdo, x * 0.0005));
	}

	public XYChart.Series<Number, Number> getySeries() {
		return ySeries;
	}

	public void setySeries(int y) {
		ySeries.getData().add(new Data<Number, Number>(osszIdo, y * 0.0005));
	}

	public XYChart.Series<Number, Number> getNyomasSeriesG() {
		nyomasSeriesG.getData().add(new Data<Number, Number>(this.getVonalIdoListaG().get(0), 0));
		for (int i = 1; i < this.getSzakaszNyomasLista().size() - 1; i++) {
			nyomasSeriesG.getData().add(
					new Data<Number, Number>(this.getVonalIdoListaG().get(i), this.getSzakaszNyomasLista().get(i)));
		}
		nyomasSeriesG.getData().add(
				new Data<Number, Number>(this.getVonalIdoListaG().get(this.getSzakaszNyomasLista().size() - 1), 0));

		return nyomasSeriesG;
	}

	public void setNyomasSeriesG(double osszIdo2, int nyomas) {
		this.nyomasSeriesG.getData().add(new Data<Number, Number>(osszIdo2, nyomas));
	}

	//a hibas kiugro pontokat javitja
	public void javit() {
		this.getSzakaszSebessegListaG().set(0, 0.0);
		this.getSzakaszSebessegListaH().set(0, 0.0);
		this.getSzakaszSebessegListaV().set(0, 0.0);

		if (this.getSzakaszSebessegListaG().size() > 2) {
			double elozoG = this.getSzakaszSebessegListaG().get(0);
			double elozoH = this.getSzakaszSebessegListaH().get(0);
			double elozoV = this.getSzakaszSebessegListaV().get(0);

			for (int i = 1; i < this.getSzakaszSebessegListaG().size() - 2; i++) {
				if (elozoG == 0 && this.getSzakaszSebessegListaG().get(i) > 0) {
					elozoG = this.getSzakaszSebessegListaG().get(i);
					this.getSzakaszSebessegListaG().set(i, this.getSzakaszSebessegListaG().get(i + 1) / 2);
				}
				if (elozoH == 0 && this.getSzakaszSebessegListaH().get(i) > 0) {
					elozoH = this.getSzakaszSebessegListaH().get(i);
					this.getSzakaszSebessegListaH().set(i, this.getSzakaszSebessegListaH().get(i + 1) / 2);
				}
				if (elozoV == 0 && this.getSzakaszSebessegListaV().get(i) > 0) {
					elozoV = this.getSzakaszSebessegListaV().get(i);
					this.getSzakaszSebessegListaV().set(i, this.getSzakaszSebessegListaV().get(i + 1) / 2);
				}

			}

			this.getSzakaszSebessegListaG().set(this.getSzakaszSebessegListaG().size() - 2,
					(this.getSzakaszSebessegListaG().get(this.getSzakaszSebessegListaG().size() - 3) / 2));

			this.getSzakaszSebessegListaH().set(this.getSzakaszSebessegListaH().size() - 2,
					(this.getSzakaszSebessegListaH().get(this.getSzakaszSebessegListaH().size() - 3) / 2));

			this.getSzakaszSebessegListaV().set(this.getSzakaszSebessegListaV().size() - 2,
					(this.getSzakaszSebessegListaV().get(this.getSzakaszSebessegListaV().size() - 3) / 2));

			this.getSzakaszSebessegListaG().set(this.getSzakaszSebessegListaG().size() - 1, 0.0);
			this.getSzakaszSebessegListaH().set(this.getSzakaszSebessegListaH().size() - 1, 0.0);
			this.getSzakaszSebessegListaV().set(this.getSzakaszSebessegListaV().size() - 1, 0.0);

		}

		this.setSzakaszGyorsulasListaG();
		this.setSzakaszGyorsulasH();
		this.setSzakaszGyorsulasV();

	}

}
