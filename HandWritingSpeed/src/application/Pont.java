package application;

public class Pont {
	
	public Pont() {
		super();
	}

	private int x, y, p, t;
	

	public Pont(int x, int y, int p, int t) {
		this.setXY(x, y);
		this.setP(p);
		this.setT(t);
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

}
