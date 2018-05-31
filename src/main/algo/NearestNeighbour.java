package main.algo;

import java.util.ArrayList;
import java.util.List;

import main.City;

public class NearestNeighbour extends Algo {

	public NearestNeighbour(List<City> cities) {
		super(cities);
	}

	protected List<City> applyAlgo() {
		ArrayList<City> res = new ArrayList<>();
		
		City currentCity = cities.get(0);
		cities.remove(0);
		res.add(currentCity);
		
		while (!cities.isEmpty()) {
			if (!doProcess) break;
			
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
