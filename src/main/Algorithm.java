package main;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Algorithm {
	private ArrayList<Point2D> cities;
	private double execTime;
	
	public Algorithm(ArrayList<Point2D> cities) {
		this.cities = cities;
	}
	
	public double lastExecTime() { return execTime; }
	
	// --- Algorithms ---
	
	// NEAREST NEIGHBOUR: for each node find the closest one and go to the next
	public ArrayList<Point2D> nearestNeighbour() {
	
		long startTime = System.nanoTime();
		ArrayList<Point2D> res = nearestNeighbourWorker(this.cities);
		execTime = (System.nanoTime()-startTime)/1000000.0;
		
		return res;
	}
	
	private static ArrayList<Point2D> nearestNeighbourWorker(ArrayList<Point2D> cities){
		ArrayList<Point2D> res = new ArrayList<>();
		
		Point2D currentCity = cities.get(0);
		cities.remove(0);
		
		while (!cities.isEmpty()) {
			res.add(currentCity);
			double distance = Float.MAX_VALUE;
			Point2D closest = currentCity; // Because we are forced to give an initial value
			for (Point2D p : cities) {
				if (currentCity.distance(p) < distance) {
					closest = p;
					distance = currentCity.distance(p);
				}
			}
			cities.remove(closest);
			res.add(closest);
			currentCity = closest;
		}
		
		return res;		
	}
}
