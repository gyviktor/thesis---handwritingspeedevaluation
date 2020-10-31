package application;

import java.util.ArrayList;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

public class Vonal {
	private Pont szakaszPont1 = new Pont();
	private Pont szakaszPont2 = new Pont();
	public static int osszIdo;
	private int szakaszIdo, vonalIdo;
	private boolean tollFent;
	public double szakaszSebessegElozo = 0;

	private double szakaszHossz, szakaszNyomas;
	private double szakaszSebesseg, szakaszSebessegH, szakaszSebessegV;
	private double szakaszGyorsulas, szakaszGyorsulasH, szakaszGyorsulasV;

	private double vonalHossz, vonalNyomas;
	private double vonalSebessegAtlag, vonalSebessegH, vonalSebessegV;
	private double vonalGyorsulas, vonalGyorsulasH, vonalGyorsulasV;
	private double minH = Double.MAX_VALUE, minV = Double.MAX_VALUE, maxH = 0, maxV = 0;

//	private double sebessegSzorasG, sebessegSzorasH, sebessegSzorasV;
//	private double gyorsulasSzorasG, gyorsulasSzorasH, gyorsulasSzorasV;

	private ArrayList<Pont> pontok = new ArrayList<Pont>();
	public double rajzSebesseg;

	private ArrayList<Integer> szakaszIdoListaG = new ArrayList<Integer>();
	private ArrayList<Double> szakaszNyomasLista = new ArrayList<Double>();
	private ArrayList<Double> szakaszSebessegListaG = new ArrayList<Double>();
	private ArrayList<Double> szakaszSebessegListaH = new ArrayList<Double>();
	private ArrayList<Double> szakaszSebessegListaV = new ArrayList<Double>();
	private ArrayList<Double> szakaszGyorsulasListaG = new ArrayList<Double>();
	private ArrayList<Double> szakaszGyorsulasListaH = new ArrayList<Double>();
	private ArrayList<Double> szakaszGyorsulasListaV = new ArrayList<Double>();
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
		this.setVonalNyomas(pont.getP());
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
		this.setSzakaszIdoListaG();
		this.setSzakaszNyomas(pont1, pont2);
		this.setSzakaszSebessegH(pont1, pont2);
		this.setSzakaszSebessegV(pont1, pont2);
		szakaszSebessegElozo = this.getSzakaszSebesseg();
		this.setSzakaszSebesség();

		this.setSzakaszGyorsulas();
		this.setSzakaszGyorsulasH();
		this.setSzakaszGyorsulasV();

		this.setVonalHossz();

		this.setVonalSebessegAtlagG();
		this.setVonalSebessegH();
		this.setVonalSebessegV();
	}

	public ArrayList<Pont> getPontok() {
		return pontok;
	}

	public static int getOsszIdo() {
		return osszIdo;
	}

	public static void setOsszIdo(int osszIdo) {
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

	public int getSzakaszIdo() {
		return szakaszIdo;
	}

	public void setSzakaszIdo(Pont pont1, Pont pont2) {
		if (pont2.getT() - pont1.getT() == 0) {
			this.szakaszIdo = 1;
		} else if ((pont2.getT() - pont1.getT()) < 8 && (pont2.getT() - pont1.getT()) > 4) {
			this.szakaszIdo = 5;
		} else {
			this.szakaszIdo = Math.abs(pont2.getT() - pont1.getT());
		}

	}

	public ArrayList<Integer> getSzakaszIdoListaG() {
		return szakaszIdoListaG;
	}

	public void setSzakaszIdoListaG() {
		this.szakaszIdoListaG.add(osszIdo);
	}

	public int getVonalIdo() {
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
				.sqrt(Math.pow(pont2.getX() - pont1.getX(), 2) + Math.pow(pont2.getY() - pont1.getY(), 2));
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

	public void setSzakaszSebesség() {
		this.szakaszSebesseg = this.getSzakaszHossz() / this.getSzakaszIdo();
		this.szakaszSebessegListaG.add(this.szakaszSebesseg);

		if (szakaszSebessegElozo == 0 && this.szakaszSebesseg > 0) {
			rajzSebesseg = this.szakaszSebesseg / 3;
		} else {
			rajzSebesseg = this.szakaszSebesseg;
		}
//		this.setSebessegSeries();
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
			if (i > 1 && this.getSzakaszSebessegListaG().get(i - 1) == 0
					&& this.getSzakaszSebessegListaG().get(i) > 0) {
				sebessegSeriesG.getData().add(new Data<Number, Number>(this.getSzakaszIdoListaG().get(i), 1));
			} else {
				sebessegSeriesG.getData().add(new Data<Number, Number>(this.getSzakaszIdoListaG().get(i),
						this.getSzakaszSebessegListaG().get(i)));
			}

		}
		return sebessegSeriesG;
	}

	public void setSebessegSeries() {
		if (this.getSzakaszGyorsulas() <= 2 && this.getSzakaszGyorsulas() >= -2) {
			this.getSebessegSeries().getData().add(new Data<Number, Number>(osszIdo, this.getSzakaszSebesseg()));
		}
	}

	public XYChart.Series<Number, Number> getSebessegSeriesH() {
		return sebessegSeriesH;
	}

	public void setSebessegSeriesH() {
		if (this.getSzakaszGyorsulasH() <= 2 && this.getSzakaszGyorsulasH() >= -2) {
			this.sebessegSeriesH.getData().add(new Data<Number, Number>(osszIdo, this.getSzakaszSebessegH()));
		}
	}

	public XYChart.Series<Number, Number> getSebessegSeriesV() {
		return sebessegSeriesV;
	}

	public void setSebessegSeriesV() {
		this.sebessegSeriesV.getData().add(new Data<Number, Number>(osszIdo, this.getSzakaszSebessegV()));
	}

	public double getSzakaszSebessegH() {
		return szakaszSebessegH;
	}

	public void setSzakaszSebessegH(Pont pont1, Pont pont2) {
		this.szakaszSebessegH = Math.abs(pont2.getX() - pont1.getX()) / this.getSzakaszIdo();
		this.szakaszSebessegListaH.add(this.szakaszSebessegH);
		this.setSebessegSeriesH();
	}

	public ArrayList<Double> getSzakaszSebessegListaH() {
		return szakaszSebessegListaH;
	}

	public double getSzakaszSebessegV() {
		return szakaszSebessegV;
	}

	public void setSzakaszSebessegV(Pont pont1, Pont pont2) {
		this.szakaszSebessegV = Math.abs(pont2.getY() - pont1.getY()) / this.getSzakaszIdo();
		this.szakaszSebessegListaV.add(this.szakaszSebessegV);
		this.setSebessegSeriesV();
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
		return gyorsulasSeriesG;
	}

	public void setGyorsulasSeries() {
		if (this.getSzakaszGyorsulas() <= 2 && this.getSzakaszGyorsulas() >= -2) {
			this.gyorsulasSeriesG.getData().add(new Data<Number, Number>(osszIdo, this.getSzakaszGyorsulas()));
		}
	}

	public XYChart.Series<Number, Number> getGyorsulasSeriesH() {
		return gyorsulasSeriesH;
	}

	public void setGyorsulasSeriesH() {
		if (this.getSzakaszGyorsulasH() <= 2 && this.getSzakaszGyorsulasH() >= -2) {
			this.gyorsulasSeriesH.getData().add(new Data<Number, Number>(osszIdo, this.getSzakaszGyorsulasH()));
		}
	}

	public XYChart.Series<Number, Number> getGyorsulasSeriesV() {
		return gyorsulasSeriesV;
	}

	public void setGyorsulasSeriesV() {
		if (this.getSzakaszGyorsulasV() <= 2 && this.getSzakaszGyorsulasV() >= -2) {
			this.gyorsulasSeriesV.getData().add(new Data<Number, Number>(osszIdo, this.getSzakaszGyorsulasV()));
		}
	}

	public void setSzakaszGyorsulas() {
		if (this.getSzakaszSebessegListaG().size() > 1) {
			double sebessegValtozas = this.getSzakaszSebessegListaG().get(this.getSzakaszSebessegListaG().size() - 1)
					- this.getSzakaszSebessegListaG().get(this.getSzakaszSebessegListaG().size() - 2);

			this.szakaszGyorsulas = sebessegValtozas / this.getSzakaszIdo();
			this.vonalGyorsulas += this.getSzakaszGyorsulas();
			this.szakaszGyorsulasListaG.add(this.getSzakaszGyorsulas());
		}
		this.setGyorsulasSeries();
	}

	public double getSzakaszGyorsulas() {
		return szakaszGyorsulas;
	}

	public double getVonalGyorsulas() {
		return vonalGyorsulas / this.getSzakaszGyorsulasListaG().size();
	}

	public ArrayList<Double> getSzakaszGyorsulasListaG() {
		return szakaszGyorsulasListaG;
	}

	public void setSzakaszGyorsulasH() {
		if (this.getSzakaszSebessegListaH().size() > 1) {
			double gyorsulasValtozas = this.getSzakaszSebessegListaH().get(this.getSzakaszSebessegListaH().size() - 1)
					- this.getSzakaszSebessegListaH().get(this.getSzakaszSebessegListaH().size() - 2);
			this.szakaszGyorsulasH = gyorsulasValtozas / this.szakaszIdo;
		}

		this.szakaszGyorsulasListaH.add(this.szakaszGyorsulasH);
		this.vonalGyorsulasH += this.getSzakaszGyorsulasH();
		this.setGyorsulasSeriesH();

	}

	public double getSzakaszGyorsulasH() {
		return szakaszGyorsulasH;
	}

	public double getVonalGyorsulasH() {
		return vonalGyorsulasH / this.getSzakaszGyorsulasListaH().size();
	}

	public ArrayList<Double> getSzakaszGyorsulasListaH() {
		return szakaszGyorsulasListaH;
	}

	public void setSzakaszGyorsulasV() {
		if (this.getSzakaszSebessegListaV().size() > 1) {
			double gyorsulasValtozas = this.getSzakaszSebessegListaV().get(this.getSzakaszSebessegListaV().size() - 1)
					- this.getSzakaszSebessegListaV().get(this.getSzakaszSebessegListaV().size() - 2);
			this.szakaszGyorsulasV = gyorsulasValtozas / this.szakaszIdo;
		}
		this.szakaszGyorsulasListaV.add(this.szakaszGyorsulasV);
		this.vonalGyorsulasV += this.getSzakaszGyorsulasV();
		this.setGyorsulasSeriesV();
	}

	public double getSzakaszGyorsulasV() {
		return szakaszGyorsulasV;
	}

	public double getVonalGyorsulasV() {
		return vonalGyorsulasV / this.getSzakaszGyorsulasListaV().size();
	}

	public ArrayList<Double> getSzakaszGyorsulasListaV() {
		return szakaszGyorsulasListaV;
	}

	public double szorasSzamol(ArrayList<Double> lista, double atlag) {
		double szoras = 0;
		for (double elem : lista) {
			szoras += Math.pow(elem - atlag, 2);
		}
		szoras /= lista.size();
		szoras = Math.sqrt(szoras);
		return szoras;
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

}
