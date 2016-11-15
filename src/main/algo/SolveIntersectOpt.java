package main.algo;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import main.City;
import main.GUI;

public class SolveIntersectOpt extends Algo {

	public SolveIntersectOpt(ArrayList<City> cities) {
		super(cities);
	}

	@SuppressWarnings("unchecked")
	protected ArrayList<City> applyAlgo() {
		ArrayList<City> res = (ArrayList<City>) cities.clone();
		
		boolean intersectFound;
		do {
			intersectFound = false;
			// Loop through all cities (except the first)
			for (int i=1; i<res.size()-1; i++) {
				if (!doProcess) break;
				
				City startA = res.get(i);
				City endA = res.get(i+1);
				// Create a line to represent the edge
				Line2D lineA = new Line2D.Float(startA, endA);
				
				// Loop through all cities
				for (int j=i; j<res.size()-1; j++) {
					if (!doProcess) break;
					// If it's the previous, the same, or the next skip.
					if (Math.abs(i-j)<2) continue;
					
					City startB = res.get(j);
					City endB = res.get(j+1);
					// Create a line to represent the edge
					Line2D lineB = new Line2D.Float(startB, endB);
					
					// If the two edge intersects
					if (lineA.intersectsLine(lineB)) {
						// Update the loop flag
						intersectFound = true;
						System.out.println("Intersect between "+i+" and "+j);
						System.out.println("   Lines:\t "+startA.getID()+"-"+endA.getID()+"\t "+startB.getID()+"-"+endB.getID());
						
						// Swap the nodes
						res = TwoOpt.swapOpt(res, i, j);						
						System.out.println("   Result:\t "+res.get(i).getID()+"-"+res.get(i+1).getID()+"\t "+res.get(j).getID()+"-"+res.get(j+1).getID());
						
						// Update data for the next loop
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
