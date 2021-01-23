package application;

import java.util.ArrayList;

public class Szoveg {

	private ArrayList<Vonal> vonalak = new ArrayList<Vonal>();
	private double sebessegAtlagG, sebessegAtlagH, sebessegAtlagV, globalisNyomas;
	private double gyorsulasAtlagG, gyorsulasAtlagH, gyorsulasAtlagV;
	private double szovegOsszIdo, szovegTollIdo, szovegEmeltIdo;
	private double szovegHossz, minH = Double.MAX_VALUE, minV = Double.MAX_VALUE, maxH = 0, maxV = 0;
	private double sebessegSzorasG, sebessegSzorasH, sebessegSzorasV;
	private double gyorsulasSzorasG, gyorsulasSzorasH, gyorsulasSzorasV;

	private int tollLentVonalSzam, tollFentVonalSzam;

	public void addVonal(Vonal vonal) {
		this.vonalak.add(vonal);
		this.szovegOsszIdo += vonal.getVonalIdo();
		if (vonal.isTollFent()) {
			this.szovegEmeltIdo += vonal.getVonalIdo();
			this.tollFentVonalSzam++;
		} else {
			this.tollLentVonalSzam++;
			this.szovegTollIdo += vonal.getVonalIdo();
			this.szovegHossz += vonal.getVonalHossz();
			this.setMinMaxHV(vonal.getMinH(), vonal.getMinV(), vonal.getMaxH(), vonal.getMaxV());
		}
	}

	public ArrayList<Vonal> getVonalak() {
		return vonalak;
	}

	public void setAtlagok() {
		int sG = 0, sH = 0, sV = 0;
		int gyG = 0, gyH = 0, gyV = 0;
		int nyG = 0;

		for (int i = 0; i < this.getVonalak().size(); i++) {
			if (!this.getVonalak().get(i).isTollFent()) {

				this.globalisNyomas += listaOsszeg(this.getVonalak().get(i).getSzakaszNyomasLista());
				nyG += this.getVonalak().get(i).getSzakaszNyomasLista().size();

				this.sebessegAtlagG += listaOsszeg(this.getVonalak().get(i).getSzakaszSebessegListaG());
				sG += this.getVonalak().get(i).getSzakaszSebessegListaG().size();

				this.sebessegAtlagH += listaOsszeg(this.getVonalak().get(i).getSzakaszSebessegListaH());
				sH += this.getVonalak().get(i).getSzakaszSebessegListaH().size();

				this.sebessegAtlagV += listaOsszeg(this.getVonalak().get(i).getSzakaszSebessegListaV());
				sV += this.getVonalak().get(i).getSzakaszSebessegListaV().size();

				this.gyorsulasAtlagG += listaOsszeg(this.getVonalak().get(i).getSzakaszGyorsulasListaG());
				gyG += this.getVonalak().get(i).getSzakaszGyorsulasListaG().size();

				this.gyorsulasAtlagH += listaOsszeg(this.getVonalak().get(i).getSzakaszGyorsulasListaH());
				gyH += this.getVonalak().get(i).getSzakaszGyorsulasListaH().size();

				this.gyorsulasAtlagV += listaOsszeg(this.getVonalak().get(i).getSzakaszGyorsulasListaV());
				gyV += this.getVonalak().get(i).getSzakaszGyorsulasListaV().size();

			}

		}
		this.globalisNyomas /= nyG;
		this.sebessegAtlagG /= sG;
		this.sebessegAtlagH /= sH;
		this.sebessegAtlagV /= sV;
		this.gyorsulasAtlagG /= gyG;
		this.gyorsulasAtlagH /= gyH;
		this.gyorsulasAtlagV /= gyV;

	}

	public int getSebessegAtlagSzinG() {
		return (int) Math.floor(((this.getSebessegAtlagG()-0.5)*20));
	}

	public double getSebessegAtlagG() {
		return this.sebessegAtlagG;
	}

	public int getSebessegAtlagSzinH() {
		return (int) Math.floor(this.getSebessegAtlagH() * 30);
	}

	public double getSebessegAtlagH() {
		return this.sebessegAtlagH;
	}

	public int getSebessegAtlagSzinV() {
		return (int) Math.floor(this.getSebessegAtlagV() * 20);
	}

	public double getSebessegAtlagV() {
		return this.sebessegAtlagV;
	}

	public int getGyorsulasAtlagSzinG() {
		return (int) Math.floor(this.getGyorsulasAtlagG());
	}

	public double getGyorsulasAtlagG() {
		return gyorsulasAtlagG;
	}

	public int getGyorsulasAtlagSzinH() {
		return (int) Math.floor(this.getGyorsulasAtlagH() * 2);
	}

	public double getGyorsulasAtlagH() {
		return gyorsulasAtlagH;
	}

	public int getGyorsulasAtlagSzinV() {
		return (int) Math.floor(this.getGyorsulasAtlagV());
	}

	public double getGyorsulasAtlagV() {
		return gyorsulasAtlagV;
	}

	public void setSzoras() {
		int sG = 0, sH = 0, sV = 0;
		int gyG = 0, gyH = 0, gyV = 0;

		for (int i = 0; i < this.getVonalak().size(); i++) {
			if (!this.getVonalak().get(i).isTollFent()) {
				this.sebessegSzorasG += szorasSzamol(this.getVonalak().get(i).getSzakaszSebessegListaG(),
						this.getSebessegAtlagG());
				sG += this.getVonalak().get(i).getSzakaszSebessegListaG().size();

				this.sebessegSzorasH += szorasSzamol(this.getVonalak().get(i).getSzakaszSebessegListaH(),
						this.getSebessegAtlagH());
				sH += this.getVonalak().get(i).getSzakaszSebessegListaH().size();

				this.sebessegSzorasV += szorasSzamol(this.getVonalak().get(i).getSzakaszSebessegListaV(),
						this.getSebessegAtlagV());
				sV += this.getVonalak().get(i).getSzakaszSebessegListaV().size();

				this.gyorsulasSzorasG += szorasSzamol(this.getVonalak().get(i).getSzakaszGyorsulasListaG(),
						this.getGyorsulasAtlagG());
				gyG += this.getVonalak().get(i).getSzakaszGyorsulasListaG().size();

				this.gyorsulasSzorasH += szorasSzamol(this.getVonalak().get(i).getSzakaszGyorsulasListaH(),
						this.getGyorsulasAtlagH());
				gyH += this.getVonalak().get(i).getSzakaszGyorsulasListaH().size();

				this.gyorsulasSzorasV += szorasSzamol(this.getVonalak().get(i).getSzakaszGyorsulasListaV(),
						this.getGyorsulasAtlagV());
				gyV += this.getVonalak().get(i).getSzakaszGyorsulasListaV().size();
			}
		}
		this.sebessegSzorasG /= sG;
		this.sebessegSzorasG = Math.sqrt(this.sebessegSzorasG);
		this.sebessegSzorasH /= sH;
		this.sebessegSzorasH = Math.sqrt(this.sebessegSzorasH);
		this.sebessegSzorasV /= sV;
		this.sebessegSzorasV = Math.sqrt(this.sebessegSzorasV);

		this.gyorsulasSzorasG /= gyG;
		this.gyorsulasSzorasG = Math.sqrt(this.gyorsulasSzorasG);
		this.gyorsulasSzorasH /= gyH;
		this.gyorsulasSzorasH = Math.sqrt(this.gyorsulasSzorasH);
		this.gyorsulasSzorasV /= gyV;
		this.gyorsulasSzorasV = Math.sqrt(this.gyorsulasSzorasV);
	}

	public int getSebessegSzorasSzinG() {
		return (int) Math.floor((this.getSebessegSzorasG() -0.5) * 20);
	}

	public double getSebessegSzorasG() {
		return sebessegSzorasG;
	}

	public int getSebessegSzorasSzinH() {
		return (int) Math.floor(this.getSebessegSzorasH() * 30);
	}

	public double getSebessegSzorasH() {
		return sebessegSzorasH;
	}

	public int getSebessegSzorasSzinV() {
		return (int) Math.floor(this.getSebessegSzorasV() * 20);
	}

	public double getSebessegSzorasV() {
		return sebessegSzorasV;
	}

	public int getGyorsulasSzorasSzinG() {
		return (int) Math.floor(this.getGyorsulasSzorasG() / 30);
	}

	public double getGyorsulasSzorasG() {
		return gyorsulasSzorasG;
	}

	public int getGyorsulasSzorasSzinH() {
		return (int) Math.floor(this.getGyorsulasSzorasH() / 10);
	}

	public double getGyorsulasSzorasH() {
		return gyorsulasSzorasH;
	}

	public int getGyorsulasSzorasSzinV() {
		return (int) Math.floor(this.getGyorsulasSzorasV() / 30);
	}

	public double getGyorsulasSzorasV() {
		return gyorsulasSzorasV;
	}

	public double getGlobalisNyomas() {
		return this.globalisNyomas;
	}

	public double getSzovegOsszIdo() {
		return szovegOsszIdo;
	}

	public double getSzovegTollIdo() {
		return szovegTollIdo;
	}

	public double getSzovegEmeltIdo() {
		return szovegEmeltIdo;
	}

	public int getTollLentVonalSzam() {
		return tollLentVonalSzam;
	}

	public int getTollFentVonalSzam() {
		return tollFentVonalSzam;
	}

	public double getSzovegHossz() {
		return szovegHossz;
	}

	public double getSzovegMeretH() {
		return (maxH - minH) * 0.0005;
	}

	public double getSzovegMeretV() {
		return (maxV - minV) * 0.0005;
	}

	public void setMinMaxHV(double minX, double minY, double maxX, double maxY) {
		if (maxX > maxH)
			this.maxH = maxX;
		if (minX < minH)
			this.minH = minX;
		if (maxY > maxV)
			this.maxV = maxY;
		if (minY < minV)
			this.minV = minY;
	}

	public double szorasSzamol(ArrayList<Double> lista, double atlag) {
		double szoras = 0;
		for (double elem : lista) {
			szoras += Math.pow(elem - atlag, 2);
		}
		return szoras;
	}

	public double listaOsszeg(ArrayList<Double> lista) {
		double osszeg = 0;

		for (double elem : lista) {
			osszeg += elem;
		}

		return osszeg;
	}

}
