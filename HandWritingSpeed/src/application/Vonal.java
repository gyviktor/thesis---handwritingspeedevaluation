package application;

import java.util.ArrayList;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;

public class Vonal {
	private Pont szakaszPont1 = new Pont();
	private Pont szakaszPont2 = new Pont();
	public static int osszIdo;
	private int pontokSzama;
	private int szakaszIdo, vonalIdo;
	private double szakaszHossz, szakaszSebesseg, szakaszSebessegElozo, sebessegValtozas, szakaszNyomas;
	private double gyorsulas;

	private double vonalHossz, vonalSebesseg, vonalSebessegH, vonalSebessegV, vonalGyorsulas, vonalGyorsulasH,
			vonalGyorsulasV, vonalNyomas;
	private boolean tollFent;
	// private double xSebesseg, ySebesseg;

	private ArrayList<Pont> pontok = new ArrayList<Pont>();
	private XYChart.Series<Number, Number> sebessegSeries = new XYChart.Series<Number, Number>();
	private XYChart.Series<Number, Number> gyorsulasSeries = new XYChart.Series<Number, Number>();

	public Vonal(Pont pont) {
		super();
		this.pontokSzama = 0;
		this.szakaszIdo = 0;
		this.szakaszHossz = 0;
		this.szakaszSebesseg = 0;
		this.sebessegValtozas = 0;
		this.vonalSebesseg = 0;
		this.vonalGyorsulas = 0;
		this.vonalIdo = 0;
		this.vonalHossz = 0;
		this.addPont(pont);
		if (pont.getP() == 0) {
			this.setTollFent(true);
		} else {
			this.setTollFent(false);
		}
	}

	public void addPont(Pont pont) {
		Pont ujPont = new Pont(pont.getX(), pont.getY(), pont.getP(), pont.getT());
		this.getPontok().add(ujPont);
		this.setPontokSzama();

		if (this.getPontokSzama() > 1) {
			// System.out.println("Pontok szama: " + this.getPontokSzama());
			this.setSzakaszPont1(this.getPont(this.getPontokSzama() - 2));
			this.setSzakaszPont2(this.getPont(this.getPontokSzama() - 1));
			this.setSzakaszHossz(this.getSzakaszPont1(), this.getSzakaszPont2());
			this.setSzakaszIdo(this.getSzakaszPont1(), this.getSzakaszPont2());
			this.setSzakaszNyomas(this.getSzakaszPont1(), this.getSzakaszPont2());
//			if(this.getSzakaszIdo() < 5) {
//				//ne csináljon semmit
//			}else {
			this.setSzakaszSebessegElozo();
			this.setSzakaszSebesség();
			this.setSebessegValtozas();
			this.setVonalHossz();
			this.setVonalIdo();
			this.setVonalSebessegAtlag();
			this.setGyorsulasSeries();
			this.setSebessegSeries();

//			}

		}
	}

	public Pont getPont(int index) {
		return pontok.get(index);
	}

	public ArrayList<Pont> getPontok() {
		return pontok;
	}

	public int getPontokSzama() {
		return pontokSzama;
	}

	public void setPontokSzama() {
		this.pontokSzama++;
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

	public void setTollFent(boolean tollFent) {
		this.tollFent = tollFent;
	}

	public int getSzakaszIdo() {
		return szakaszIdo;
	}

	public void setSzakaszIdo(Pont pont1, Pont pont2) {
		if ((pont2.getT() - pont1.getT()) < 10) {
			this.szakaszIdo = 5;
		} else {
			this.szakaszIdo = Math.abs(pont2.getT() - pont1.getT());
		}

//		this.szakaszIdo = Math.abs(pont2.getT() - pont1.getT());

	}

	public double getSzakaszHossz() {
		return szakaszHossz;
	}

	public void setSzakaszHossz(Pont pont1, Pont pont2) {
		this.szakaszHossz = Math
				.sqrt(Math.pow(pont2.getX() - pont1.getX(), 2) + Math.pow(pont2.getY() - pont1.getY(), 2));
	}

	public double getSzakaszSebesseg() {
		return szakaszSebesseg;
	}

	public void setSzakaszSebesség() {
		if (this.getSzakaszIdo() == 0) {
			this.szakaszSebesseg = 0;
		} else {
			this.szakaszSebesseg = this.getSzakaszHossz() / this.getSzakaszIdo();
		}
	}

	public double getSzakaszSebessegElozo() {
		return szakaszSebessegElozo;
	}

	public void setSzakaszSebessegElozo() {
		this.szakaszSebessegElozo = this.getSzakaszSebesseg();
	}

	public double getSebessegValtozas() {
		return sebessegValtozas;
	}

	public void setSebessegValtozas() {
		this.sebessegValtozas = this.getSzakaszSebesseg() - this.getSzakaszSebessegElozo();
	}

	public double getVonalIdo() {
		return vonalIdo;
	}

	public void setVonalIdo() {
		osszIdo += this.szakaszIdo;
		this.vonalIdo += this.szakaszIdo;
	}

	public double getVonalHossz() {
		return vonalHossz;
	}

	public void setVonalHossz() {
		this.vonalHossz += this.getSzakaszHossz();
	}

	public double getVonalSebesseg() {
		return vonalSebesseg;
	}

	public void setVonalSebessegAtlag() {
		this.vonalSebesseg = this.getVonalHossz() / this.getVonalIdo();
	}

	public double getVonalGyorsulas() {
		return vonalGyorsulas;
	}

	public void setVonalGyorsulas() {
		if (this.getGyorsulas() <= 1 && this.getGyorsulas() >= -1) {
			this.vonalGyorsulas += this.getGyorsulas();
		}
	}

	public XYChart.Series<Number, Number> getSebessegSeries() {
		return sebessegSeries;
	}

	public void setSebessegSeries() {
		if (this.getGyorsulas() <= 1 && this.getGyorsulas() >= -1) {
			this.getSebessegSeries().getData().add(new Data<Number, Number>(osszIdo, this.getSzakaszSebesseg()));
		}
	}

	public XYChart.Series<Number, Number> getGyorsulasSeries() {
		return gyorsulasSeries;
	}

	public void setGyorsulasSeries() {
		this.setGyorsulas(this.getSebessegValtozas() / this.getSzakaszIdo());
		// System.out.println("Gyorsulas: " + gyorsulas);
		if (this.getGyorsulas() <= 1 && this.getGyorsulas() >= -1) {
			this.gyorsulasSeries.getData().add(new Data<Number, Number>(osszIdo, this.getGyorsulas()));
			this.setVonalGyorsulas();
		}
	}

	public double getGyorsulas() {
		return gyorsulas;
	}

	public void setGyorsulas(double gyorsulas) {
		this.gyorsulas = gyorsulas;
	}

	public double getSzakaszNyomas() {
		return szakaszNyomas;
	}

	public void setSzakaszNyomas(Pont pont1, Pont pont2) {
		this.szakaszNyomas = (pont1.getP() + pont2.getP()) / 2;
	}

	public double getVonalSebessegH() {
		return vonalSebessegH;
	}

	public void setVonalSebessegH(double vonalSebessegH) {
		this.vonalSebessegH = vonalSebessegH;
	}

	public double getVonalSebessegV() {
		return vonalSebessegV;
	}

	public void setVonalSebessegV(double vonalSebessegV) {
		this.vonalSebessegV = vonalSebessegV;
	}

	public double getVonalGyorsulasH() {
		return vonalGyorsulasH;
	}

	public void setVonalGyorsulasH(double vonalGyorsulasH) {
		this.vonalGyorsulasH = vonalGyorsulasH;
	}

	public double getVonalGyorsulasV() {
		return vonalGyorsulasV;
	}

	public void setVonalGyorsulasV(double vonalGyorsulasV) {
		this.vonalGyorsulasV = vonalGyorsulasV;
	}

	public double getVonalNyomas() {
		return vonalNyomas;
	}

	public void setVonalNyomas(double vonalNyomas) {
		this.vonalNyomas = vonalNyomas;
	}

}
