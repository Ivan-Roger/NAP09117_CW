package main;

import java.awt.geom.Point2D;

public class City extends Point2D {
	private int id;
	private float posX;
	private float posY;
	
	public City(int id, float x, float y) {
		this.id = id;
		setLocation(x,y);
	}
	
	public int getID() {
		return id;
	}

	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return posX;
	}

	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return posY;
	}

	@Override
	public void setLocation(double x, double y) {
		this.posX = (float) x;
		this.posY = (float) y;
	}

}
