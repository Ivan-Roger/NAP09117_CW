package main.algo;

import java.util.ArrayList;

import main.City;
import main.GUI;
import main.TSPLib;

public class TwoOpt extends Algo {

	public TwoOpt(ArrayList<City> cities) {
		super(cities);
	}

	@SuppressWarnings("unchecked")
	protected ArrayList<City> applyAlgo() {
		ArrayList<City> res = (ArrayList<City>) cities.clone();
		//res.add(res.get(0));
		
		double best_dist = TSPLib.routeLength(res); 
		boolean improvementMade;
		do {
			improvementMade = false;
			for (int i=1; i<res.size()-1; i++) {
				if (!doProcess) break;
				for (int j=i+1; j<res.size()-1; j++) {
					if (!doProcess) break;
					
					ArrayList<City> new_route = swapOpt(res, i, j);
					double new_dist = TSPLib.routeLength(new_route);
					if (new_dist<best_dist) {
						res = new_route;
						System.out.println("New dist: \t"+new_dist+"\t Improvement: "+(best_dist-new_dist));
						best_dist = new_dist;
						improvementMade = true;
						
						GUI.getInstance().drawCities(res);
					}
				}
			}
			System.out.println("Finished loop.");
		} while (improvementMade && doProcess);
		
		if (res.get(res.size()-1)==res.get(0)) {
			res.remove(res.size()-1);
		}
		
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<City> swapOpt(ArrayList<City> in, int i, int j) {
		ArrayList<City> out = (ArrayList<City>) in.clone();
		
		City tmp = out.get(i);
		out.set(i, out.get(j));
		out.set(j, tmp);
		
		return out;
	}
}
