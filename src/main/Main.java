package main;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Main {
	public static final String TSP_FILES_PATH = System.getProperty("user.dir").concat("\\src\\data\\");
	public static final String[] TSP_FILES = {"pr152.tsp","att48.tsp","gr120.tsp","a280.tsp"};
	private static final int TSP_PROBLEM = 0;

	public static void main(String[] args) {
		ArrayList<Point2D> cities = TSPLib.load(TSP_FILES_PATH+TSP_FILES[TSP_PROBLEM]);

		System.out.println("Number of cities: \t"+cities.size());
		System.out.println("Initial route length: \t\t"+TSPLib.routeLength(cities));
		
		NearestNeighbour algoNN = new NearestNeighbour(cities);		
		System.out.println("\nNearest neighbour length: \t"+TSPLib.routeLength(algoNN.process()));
		System.out.println("Time: \t"+algoNN.lastExecTime()+"ms");
		
		GUI gui = new GUI(TSP_PROBLEM);
		gui.setVisible(true);
	}

}
