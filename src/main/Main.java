package main;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Main {
	private static final String TSP_FILES_PATH = System.getProperty("user.dir").concat("\\src\\data\\");
	private static String[] tspFiles = {"pr152.tsp","att48.tsp"};
	private static final int TSP_PROBLEM = 0;

	public static void main(String[] args) {
		ArrayList<Point2D> cities = TSPLib.load(TSP_FILES_PATH+tspFiles[TSP_PROBLEM]);
		Algorithm algo = new Algorithm(cities);

		System.out.println("Number of cities: \t"+cities.size());
		System.out.println("Initial route length: \t\t"+TSPLib.routeLength(cities));
		
		System.out.println("\nNearest neighbour length: \t"+TSPLib.routeLength(algo.nearestNeighbour()));
		System.out.println("Time: \t"+algo.lastExecTime()+"ms");
	}

}
