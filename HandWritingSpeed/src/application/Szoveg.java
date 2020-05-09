package application;

import java.util.ArrayList;

public class Szoveg {
	private int pontokSzama;
	private double globalisSebesseg, globalisGyorsulas, globalisNyomas, szoras;
	private ArrayList<Vonal> vonalak = new ArrayList<Vonal>();
		
	
	public void addVonal(Vonal vonal) {
		this.vonalak.add(vonal);
		this.setPontokSzama(vonal);
		this.setGlobalisSebesseg(vonal.getVonalSebesseg());
		this.setGlobalisGyorsulas(vonal.getVonalGyorsulas());
		//this.setGlobalisNyomas();
		
	}
	
	public ArrayList<Vonal> getVonalak() {
		return vonalak;
	}

	public String getGlobalisSebesseg() {		
		return String.valueOf(this.globalisSebesseg/this.getVonalak().size());
	}
	public void setGlobalisSebesseg(double vonalSebesseg) {
		this.globalisSebesseg += vonalSebesseg;
	}
	public String getGlobalisGyorsulas() {
		return String.valueOf(globalisGyorsulas);
	}
	public void setGlobalisGyorsulas(double vonalGyorsulas) {
		this.globalisGyorsulas += vonalGyorsulas;
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

	public double getGlobalisNyomas() {
		return globalisNyomas;
	}

	public void setGlobalisNyomas(double globalisNyomas) {
		this.globalisNyomas = globalisNyomas;
	}
	
}
