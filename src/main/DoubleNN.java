package main;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class DoubleNN extends Algo {

	public DoubleNN(ArrayList<Point2D> cities) {
		super(cities);
	}

	protected ArrayList<Point2D> applyAlgo() {
		ArrayList<Point2D> res = new ArrayList<>();
		
		Point2D depCity = cities.get(0);
		cities.remove(0);
		
		// We find the city that is the more far away from the beginning point
		Point2D furthestCity = depCity; // Because we are forced to give an initial value
		double furthestDist = 0.0;
		for (Point2D p : cities) {
			double dist = depCity.distance(p);
			if (dist > furthestDist) {
				furthestCity = p; furthestDist = dist;
			}
		}
		cities.remove(furthestCity);
		
		
		
		return res;		
	}
}
