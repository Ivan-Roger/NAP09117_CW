package main.algo;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import main.City;
import main.GUI;

public class SolveIntersect_OLD extends Algo {

	public SolveIntersect_OLD(ArrayList<City> cities) {
		super(cities);
	}

	@SuppressWarnings("unchecked")
	protected ArrayList<City> applyAlgo() {
		ArrayList<City> res = (ArrayList<City>) cities.clone();
		
		boolean intersectFound = false;
		do {
			intersectFound = false;
			for (int i=1; i<res.size()-1; i++) {
				if (!doProcess) break;
				
				City startA = res.get(i);
				City endA = res.get(i+1);
				Line2D lineA = new Line2D.Float(startA, endA);
				for (int j=1; j<res.size()-1; j++) {
					if (!doProcess) break;
					
					// System.out.println("Testing "+i+","+j+" / check="+(Math.abs(i-j)));
					if (Math.abs(i-j)<2) continue;
					City startB = res.get(j);
					City endB = res.get(j+1);
					Line2D lineB = new Line2D.Float(startB, endB);
					
					if (lineA.intersectsLine(lineB)) {
						intersectFound = true;
						System.out.println("Intersect between "+i+" and "+j);
						System.out.println("   Lines:\t "+startA.getID()+"-"+endA.getID()+"\t "+startB.getID()+"-"+endB.getID());

						res.set(i+1, endB);
						res.set(j+1, endA);
						System.out.println("   Result:\t "+res.get(i).getID()+"-"+res.get(i+1).getID()+"\t "+res.get(j).getID()+"-"+res.get(j+1).getID());
						
						// Update data
						startA = res.get(i);
						endA = res.get(i+1);
						lineA = new Line2D.Float(startA, endA);
						
						GUI.getInstance().drawCities(res);
					}
				}
			}
		} while (intersectFound && doProcess);
		
		return res;
	}
}
