package main;

import java.awt.geom.Line2D;
import java.util.ArrayList;

public class SolveIntersect extends Algo {

	public SolveIntersect(ArrayList<City> cities) {
		super(cities);
	}

	@SuppressWarnings("unchecked")
	protected ArrayList<City> applyAlgo() {
		ArrayList<City> res = (ArrayList<City>) cities.clone();
		
		boolean intersectFound = false;
		do {
			intersectFound = false;
			for (int i=0; i<res.size()-1; i++) {
				City startA = res.get(i);
				City endA = res.get(i+1);
				Line2D lineA = new Line2D.Float(startA, endA);
				for (int j=0; j<res.size()-1; j++) {
					// System.out.println("Testing "+i+","+j+" / check="+(Math.abs(i-j)));
					if (Math.abs(i-j)<2) continue;
					//if ((i==0 || j == 0) && (i==res.size()-2 || j==res.size()-2)) continue;
					if (i==0 || i==res.size()-2) continue;
					if (j==0 || j==res.size()-2) continue;
					City startB = res.get(j);
					City endB = res.get(j+1);
					if (endA==startB) {
						System.out.println(">>>>>>>>>>>> WEIRD POINT <<<<<<<<<<<\n   Intersect to the next\n   i="+i+", j="+j);
						System.out.println("   A,B:\t "+startA.getID()+"-"+endA.getID()+"\t "+startB.getID()+"-"+endB.getID());
						this.suspend();
						continue;
					}
					if (startA==endA) {
						System.out.println(">>>>>>>>>>>> WEIRD POINT <<<<<<<<<<<\n   Null line\n   i="+i+", j="+j);
						System.out.println("   A,B:\t "+startA.getID()+"-"+endA.getID()+"\t "+startB.getID()+"-"+endB.getID());
						this.suspend();
						continue;
					}
					Line2D lineB = new Line2D.Float(startB, endB);
					
					if (lineA.intersectsLine(lineB)) {
						intersectFound = true;
						System.out.println("Intersect between "+i+" and "+j);
						System.out.println("   Lines:\t "+startA.getID()+"-"+endA.getID()+"\t "+startB.getID()+"-"+endB.getID());
						/*try {
							Thread.currentThread().sleep(250);
						} catch (InterruptedException e1) {e1.printStackTrace();} */
						res.set(i+1, endB);
						res.set(j+1, endA);
						System.out.println("   Result:\t "+res.get(i).getID()+"-"+res.get(i+1).getID()+"\t "+res.get(j).getID()+"-"+res.get(j+1).getID());
						
						// Update data
						startA = res.get(i);
						endA = res.get(i+1);
						lineA = new Line2D.Float(startA, endA);
						
						for (int x=0; x<res.size()-1; x++) {
							if (res.get(x)==res.get(x+1)) {
								System.err.println(">>> ERROR: @"+x+"\n   "+res.get(x).getID()+"-"+res.get(x+1).getID());
								System.err.print("\t"+res.get(x-2).getID()+"-"+res.get(x-1).getID()+"\t");
								System.err.print(res.get(x).getID()+"-"+res.get(x+1).getID()+"\t");
								System.err.println(res.get(x+2).getID()+"-"+res.get(x+3).getID());
								this.suspend();
							}
						}
						
						GUI.getInstance().drawCities(res);
					}
				}
			}
		} while (intersectFound);
		
		return res;
	}
}
