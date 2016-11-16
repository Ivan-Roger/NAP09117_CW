package main.algo;

import java.util.ArrayList;

import main.City;
import main.GUI;

public class TwoOpt extends Algo {

	public TwoOpt(ArrayList<City> cities) {
		super(cities);
	}

	@SuppressWarnings("unchecked")
	protected ArrayList<City> applyAlgo() {
		ArrayList<City> res = (ArrayList<City>) cities.clone();
		res.add(res.get(0));
		
		boolean improvementMade;
		do {
			improvementMade = false;
			for (int i=1; i<res.size()-1; i++) {
				if (!doProcess) break;
				for (int j=i+1; j<res.size()-1; j++) {
					if (!doProcess) break;
					
					double dist_old = res.get(i).distance(res.get(i+1)) + res.get(j).distance(res.get(j+1));
					double dist_new = res.get(i).distance(res.get(j+1)) + res.get(j).distance(res.get(i+1));
					if (dist_new<dist_old) {
						res = swapOpt(res, i, j);
						System.out.println("Swap: \t"+i+", "+j+"\t Improvement: "+(dist_old-dist_new));
						improvementMade = true;
						
						GUI.getInstance().drawCities(res);
					}
				}
			}
			System.out.println("Finished loop.");
		} while (improvementMade && doProcess);
		
		//*  // Remove closing line
		if (res.get(res.size()-1)==res.get(0)) {
			res.remove(res.size()-1);
		} //*/
		
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
