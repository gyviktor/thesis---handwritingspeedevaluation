package application;

import java.util.ArrayList;

public class Szoveg {

	private ArrayList<Vonal> vonalak = new ArrayList<Vonal>();
	private int pontokSzama;
	private double sebessegAtlagG, gyorsulasAtlagG, globalisNyomas;
	private double sebessegAtlagV, sebessegAtlagH;
	private double gyorsulasAtlagH, gyorsulasAtlagV;
	private double szovegOsszIdo, szovegTollIdo, szovegEmeltIdo;
	private double szovegHossz, minH = Double.MAX_VALUE, minV = Double.MAX_VALUE, maxH = 0, maxV = 0, szovegMeretH,
			szovegMeretV;
	private double sebessegSzorasG, sebessegSzorasH, sebessegSzorasV;
	private double gyorsulasSzorasG, gyorsulasSzorasH, gyorsulasSzorasV;

	private int tollLentVonalSzam, tollFentVonalSzam;

	public void addVonal(Vonal vonal) {
		this.vonalak.add(vonal);
		this.setPontokSzama(vonal);
		this.setSzovegOsszIdo(vonal.getVonalIdo());
		if (vonal.isTollFent() == true) {
			this.setSzovegEmeltIdo(vonal.getVonalIdo());
			this.setTollFentVonalSzam();
		} else {
			this.setTollLentVonalSzam();
			this.setSzovegTollIdo(vonal.getVonalIdo());
			this.setSebessegAtlagG(vonal.getVonalSebessegAtlagG());
			this.setGyorsulasAtlagG(vonal.getVonalGyorsulas());
			this.setGlobalisNyomas(vonal.getVonalNyomas());
			this.setSebessegAtlagH(vonal.getVonalSebessegH());
			this.setSebessegAtlagV(vonal.getVonalSebessegV());
			this.setGyorsulasAtlagH(vonal.getVonalGyorsulasH());
			this.setGyorsulasAtlagV(vonal.getVonalGyorsulasV());
			this.setSzovegHossz(vonal.getVonalHossz());
			this.setMinMaxHV(vonal.getMinH(), vonal.getMinV(), vonal.getMaxH(), vonal.getMaxV());
			this.setSebessegSzorasG(vonal);
			this.setSebessegSzorasH(vonal);
			this.setSebessegSzorasV(vonal);
			this.setGyorsulasSzorasG(vonal);
			this.setGyorsulasSzorasH(vonal);
			this.setGyorsulasSzorasV(vonal);
			
			

		}
	}

	public ArrayList<Vonal> getVonalak() {
		return vonalak;
	}

	public int getSebessegAtlagSzinG() {
		return (int) Math.floor(this.getSebessegAtlagG() * 7);
	}

	public double getSebessegAtlagG() {
		return this.sebessegAtlagG / this.getTollLentVonalSzam();
	}

	public void setSebessegAtlagG(double vonalSebesseg) {
		this.sebessegAtlagG += vonalSebesseg;
	}

	public int getSebessegAtlagSzinH() {
		return (int) Math.floor(this.getSebessegAtlagH() * 10);
	}

	public double getSebessegAtlagH() {
		return this.sebessegAtlagH / this.getTollLentVonalSzam();
	}

	public void setSebessegAtlagH(double sebessegAtlagH) {
		this.sebessegAtlagH += sebessegAtlagH;
	}

	public int getSebessegAtlagSzinV() {
		return (int) Math.floor(this.getSebessegAtlagV() * 10);
	}

	public double getSebessegAtlagV() {
		return this.sebessegAtlagV / this.getTollLentVonalSzam();
	}

	public void setSebessegAtlagV(double sebessegAtlagV) {
		this.sebessegAtlagV += sebessegAtlagV;
	}

	public int getSebessegSzorasSzinG() {
		return (int) Math.floor(this.getSebessegSzorasG() / this.getSebessegAtlagG() * 30);
	}

	public double getSebessegSzorasG() {
		return sebessegSzorasG / this.getTollLentVonalSzam();
	}

	public void setSebessegSzorasG(Vonal vonal) {
		this.sebessegSzorasG += vonal.szorasSzamol(vonal.getSzakaszSebessegListaG(), vonal.getVonalSebessegAtlagG());
	}

	public int getSebessegSzorasSzinH() {
		return (int) Math.floor(this.getSebessegSzorasH() / this.getSebessegAtlagH() * 30);
	}

	public double getSebessegSzorasH() {
		return sebessegSzorasH / this.getTollLentVonalSzam();
	}

	public void setSebessegSzorasH(Vonal vonal) {
		this.sebessegSzorasH += vonal.szorasSzamol(vonal.getSzakaszSebessegListaH(), vonal.getVonalSebessegH());
	}

	public int getSebessegSzorasSzinV() {
		return (int) Math.floor(this.getSebessegSzorasV() / this.getSebessegAtlagV() * 30);
	}

	public double getSebessegSzorasV() {
		return sebessegSzorasV / this.getTollLentVonalSzam();
	}

	public void setSebessegSzorasV(Vonal vonal) {
		this.sebessegSzorasV += vonal.szorasSzamol(vonal.getSzakaszSebessegListaV(), vonal.getVonalSebessegV());
	}

	public int getGyorsulasAtlagSzinG() {
		System.out.println("A: " + this.getGyorsulasAtlagG() * 60);
		return (int) Math.floor(this.getGyorsulasAtlagG() * 60);
	}

	public double getGyorsulasAtlagG() {
		System.out.println("B: " + gyorsulasAtlagG / this.getTollLentVonalSzam());
		return (gyorsulasAtlagG / this.getTollLentVonalSzam());
	}

	public void setGyorsulasAtlagG(double vonalGyorsulas) {
		this.gyorsulasAtlagG += vonalGyorsulas;
	}

	public int getGyorsulasAtlagSzinH() {
		return (int) Math.floor(this.getGyorsulasAtlagH() * 60);
	}

	public double getGyorsulasAtlagH() {
		return gyorsulasAtlagH / this.getTollLentVonalSzam();
	}

	public void setGyorsulasAtlagH(double vonalGyorsulasH) {
		this.gyorsulasAtlagH += vonalGyorsulasH;
	}

	public int getGyorsulasAtlagSzinV() {
		return (int) Math.floor(this.getGyorsulasAtlagV() * 60);
	}

	public double getGyorsulasAtlagV() {
		return gyorsulasAtlagV / this.getTollLentVonalSzam();
	}

	public void setGyorsulasAtlagV(double vonalGyorsulasV) {
		this.gyorsulasAtlagV += vonalGyorsulasV;
	}

	public int getGyorsulasSzorasSzinG() {
		return (int) Math.floor(this.getGyorsulasSzorasG() / Math.abs(this.getGyorsulasAtlagG()) * 10);
	}

	public double getGyorsulasSzorasG() {
		return gyorsulasSzorasG / this.getTollLentVonalSzam();
	}

	public void setGyorsulasSzorasG(Vonal vonal) {
		this.gyorsulasSzorasG += vonal.szorasSzamol(vonal.getSzakaszGyorsulasListaG(), vonal.getVonalGyorsulas());
	}

	public int getGyorsulasSzorasSzinH() {
		return (int) Math.floor(this.getGyorsulasSzorasH() / Math.abs(this.getGyorsulasAtlagH()) * 10);
	}

	public double getGyorsulasSzorasH() {
		return gyorsulasSzorasH / this.getTollLentVonalSzam();
	}

	public void setGyorsulasSzorasH(Vonal vonal) {
		this.gyorsulasSzorasH += vonal.szorasSzamol(vonal.getSzakaszGyorsulasListaH(), vonal.getVonalGyorsulasH());
	}

	public int getGyorsulasSzorasSzinV() {
		return (int) Math.floor(this.getGyorsulasSzorasV() / Math.abs(this.getGyorsulasAtlagV()) * 10);
	}

	public double getGyorsulasSzorasV() {
		return gyorsulasSzorasV / this.getTollLentVonalSzam();
	}

	public void setGyorsulasSzorasV(Vonal vonal) {
		this.gyorsulasSzorasV = vonal.szorasSzamol(vonal.getSzakaszGyorsulasListaV(), vonal.getVonalGyorsulasV());
	}

	public int getPontokSzama() {
		return pontokSzama;
	}

	public void setPontokSzama(Vonal vonal) {
		this.pontokSzama += vonal.getPontok().size();
	}

	public double getGlobalisNyomas() {
		return this.globalisNyomas / this.getTollLentVonalSzam();
	}

	public void setGlobalisNyomas(double vonalNyomas) {
		this.globalisNyomas += vonalNyomas;
	}

	public double getSzovegOsszIdo() {
		return szovegOsszIdo;
	}

	public void setSzovegOsszIdo(int ido) {
		this.szovegOsszIdo += ido;
	}

	public double getSzovegTollIdo() {
		return szovegTollIdo;
	}

	public void setSzovegTollIdo(int ido) {
		this.szovegTollIdo += ido;
	}

	public double getSzovegEmeltIdo() {
		return szovegEmeltIdo;
	}

	public void setSzovegEmeltIdo(int ido) {
		this.szovegEmeltIdo += ido;
	}

	public int getTollLentVonalSzam() {
		return tollLentVonalSzam;
	}

	public void setTollLentVonalSzam() {
		this.tollLentVonalSzam++;
	}

	public int getTollFentVonalSzam() {
		return tollFentVonalSzam;
	}

	public void setTollFentVonalSzam() {
		this.tollFentVonalSzam++;
	}

	public String getSzovegHosszString() {
		return String.valueOf(szovegHossz).substring(0, 7);
	}

	public double getSzovegHossz() {
		return szovegHossz;
	}

	public void setSzovegHossz(double szovegHossz) {
		this.szovegHossz += szovegHossz;
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

	public double getSzovegMeretH() {
		return szovegMeretH;
	}

	public void setSzovegMeretH(double szovegMeretH) {
		this.szovegMeretH = szovegMeretH;
	}

	public double getSzovegMeretV() {
		return szovegMeretV;
	}

	public void setSzovegMeretV(double szovegMeretV) {
		this.szovegMeretV = szovegMeretV;
	}

	public void setMinMaxHV(double minX, double minY, double maxX, double maxY) {
		if (maxX > this.getMaxH())
			this.maxH = maxX;
		if (minX < this.getMinH())
			this.minH = minX;
		if (maxY > this.getMaxV())
			this.maxV = maxY;
		if (minY < this.getMinV())
			this.minV = minY;
	}

}
