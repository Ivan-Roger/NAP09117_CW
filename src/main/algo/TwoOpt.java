package main.algo;

import java.util.ArrayList;

import main.City;
import main.GUI;
import main.TSPLib;

public class TwoOpt extends Algo {
	private int nbImprovements;
	private int nbTests;

	public TwoOpt(ArrayList<City> cities) {
		super(cities);
	}

	@SuppressWarnings("unchecked")
	protected ArrayList<City> applyAlgo() {
		nbImprovements = 0; nbTests = 0;
		ArrayList<City> res = (ArrayList<City>) cities.clone();
		res.add(res.get(0));
		double routeLength = TSPLib.routeLength(res);
		
		boolean improvementMade;
		do {
			improvementMade = false;
			for (int i=1; i<res.size()-1; i++) {
				if (!doProcess) break;
				for (int j=i+1; j<res.size()-1; j++) {
					if (!doProcess) break;
					nbTests++;

                    ArrayList<City> routeNew = swapOpt(res, i, j);
                    double distNew = TSPLib.routeLength(routeNew);
					
					/*
					// O-A, (A-X, X-B), B-Y
					double distOld = res.get(i-1).distance(res.get(i)) + res.get(j).distance(res.get(j+1));
					// O-B, (B-X, X-A), A-Y
					double distNew = res.get(i-1).distance(res.get(j)) + res.get(i).distance(res.get(j+1));
					double diff = distOld-distNew;
					*/
					double diff = routeLength-distNew;
					if (diff>0) {
						res = routeNew;
						improvementMade = true;
						nbImprovements++;
						routeLength=distNew;
						System.out.println("Swap: "+i+", "+j+"\t Improvement: "+diff+"     \t Route length: "+routeLength+"     \t\tCount: "+nbImprovements);
						
						GUI.getInstance().drawCities(res);
						break;
					}
				}
				if (improvementMade) break;
			}
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
	
	@Override
	public String getDetails() {
		String res = super.getDetails();
		res+= "Nb of tests: "+nbTests+"\n";
		res+= "Nb of improvements: "+nbImprovements+"\n";
		return res;
	}
}
