package application;

import java.util.ArrayList;

public class Szoveg {
	private int pontokSzama;
	private double globalisSebesseg, globalisGyorsulas, szoras;
	private ArrayList<Vonal> vonalak = new ArrayList<Vonal>();
	
	public void addVonal(Vonal vonal) {
		this.vonalak.add(vonal);
		this.setPontokSzama(vonal);
		this.setGlobalisSebesseg(vonal.getVonalSebesseg());
		
	}
	
	public ArrayList<Vonal> getVonalak() {
		return vonalak;
	}

	public double getGlobalisSebesseg() {		
		return this.globalisSebesseg/this.getVonalak().size();
	}
	public void setGlobalisSebesseg(double vonalSebesseg) {
		this.globalisSebesseg += vonalSebesseg;
	}
	public double getGlobalisGyorsulas() {
		return globalisGyorsulas;
	}
	public void setGlobalisGyorsulas(double globalisGyorsulas) {
		this.globalisGyorsulas = globalisGyorsulas;
	}
	public double getSzoras() {
		return szoras;
	}
	public void setSzoras(double szoras) {
		this.szoras = szoras;
	}
	public int getPontokSzama() {
		return pontokSzama;
	}
	public void setPontokSzama(Vonal vonal) {
		this.pontokSzama += vonal.getPontokSzama();
	}
	
}
