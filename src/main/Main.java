package main;

import java.util.ArrayList;

public class Main {
	public static final String TSP_FILES_PATH = System.getProperty("user.dir").concat("\\src\\data\\");
	public static final String[] TSP_FILES = {"pr152.tsp","att48.tsp","gr120.tsp","a280.tsp"};
	private static final int TSP_PROBLEM = 1;

	public static void main(String[] args) {
		graphic();
	}
	
	public static void graphic() {
		GUI gui = GUI.getInstance(TSP_PROBLEM);
		gui.setVisible(true);
	}
	
	public static void text() {
		ArrayList<City> cities = TSPLib.load(TSP_FILES_PATH+TSP_FILES[TSP_PROBLEM]);

		System.out.println("Number of cities: \t"+cities.size());
		System.out.println("Initial route length: \t\t"+TSPLib.routeLength(cities));
		
		NearestNeighbour algoNN = new NearestNeighbour(cities);		
		try {
			System.out.println("\nNearest neighbour length: \t"+TSPLib.routeLength(algoNN.process()));
		} catch (InvalidSizeEx ex) {
			System.out.println("Algorithm didn't the right number of cities !\nExpected: "+ex.getExpectedSize()+"\t Found: "+ex.getFoundSize());
		}
		System.out.println("Time: \t"+algoNN.lastExecTime()+"ms");
	}

}
