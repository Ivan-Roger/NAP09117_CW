package main;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class NearestNeighbour extends Algo {

	public NearestNeighbour(ArrayList<Point2D> cities) {
		super(cities);
	}

	protected ArrayList<Point2D> applyAlgo() {
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
