package main;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public abstract class Algo {
	protected ArrayList<Point2D> cities;
	private double execTime;
	
	public Algo(ArrayList<Point2D> cities) {
		this.cities = (ArrayList<Point2D>) cities.clone();
	}
	
	public double lastExecTime() { return execTime; }
	
	abstract ArrayList<Point2D> applyAlgo();
	
	public ArrayList<Point2D> process() {
		long startTime = System.nanoTime();
		ArrayList<Point2D> res = this.applyAlgo();
		execTime = (System.nanoTime()-startTime)/1000000.0;
		
		return res;
	}

}
