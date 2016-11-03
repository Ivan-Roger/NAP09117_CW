package main;

import java.util.ArrayList;

public class NearestNeighbour extends Algo {

	public NearestNeighbour(ArrayList<City> cities) {
		super(cities);
	}

	protected ArrayList<City> applyAlgo() {
		ArrayList<City> res = new ArrayList<>();
		
		City currentCity = cities.get(0);
		cities.remove(0);
		res.add(currentCity);
		
		while (!cities.isEmpty()) {
			double distance = Float.MAX_VALUE;
			City closest = currentCity; // Because we are forced to give an initial value
			for (City p : cities) {
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
