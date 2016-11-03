package main;

import java.awt.geom.Line2D;
import java.util.ArrayList;

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
				for (int j=i+1; j<res.size()-1; j++) {
					ArrayList<City> new_route = swap2Opt(res, i, j);
					double new_dist = TSPLib.routeLength(new_route);
					if (new_dist<best_dist) {
						res = new_route;
						best_dist = new_dist;
						improvementMade = true;
						System.out.println("Old dist: \t"+best_dist+"\t New dist: "+new_dist);
						
						/*try {
							Thread.currentThread().sleep(250);
						} catch (InterruptedException e) {e.printStackTrace();}*/
						
						GUI.getInstance().drawCities(res);
					}
				}
			}
			System.out.println("New loop.");
		} while (improvementMade);
		
		if (res.get(res.size()-1)==res.get(0)) {
			res.remove(res.size()-1);
		}
		
		return res;
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<City> swap2Opt(ArrayList<City> in, int i, int j) {
		ArrayList<City> out = (ArrayList<City>) in.clone();
		
		City tmp = out.get(i);
		out.set(i, out.get(j));
		out.set(j, tmp);
		
		return out;
	}
}
