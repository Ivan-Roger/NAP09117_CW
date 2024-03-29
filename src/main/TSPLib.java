package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TSPLib {

	public static ArrayList<City> load(File tspFile){
		//Load in a TSPLib instance. This example assumes that the Edge weight type //is EUC_2D.
		//It will work for examples such as rl5915.tsp. Other files such as //fri26.tsp .To use a different format, you will have to
		//modify the this code
		ArrayList<City> result = new ArrayList<City>();
		BufferedReader br = null;
		int lineID = 0;
		try {
			String currentLine;
			int dimension =0;//Hold the dimension of the problem
			boolean readingNodes = false;
			br = new BufferedReader(new FileReader(tspFile));
			while ((currentLine = br.readLine()) != null) {
				lineID++;
				//Read the file until the end;
				if (currentLine.contains("EOF")){
					//EOF should be the last line
					readingNodes = false;
					//Finished reading nodes
					if (result.size() != dimension){
						//Check to see if the expected number of cities have been loaded
						System.out.println("Error loading cities");
						System.exit(-1);
					}
				}
				if (readingNodes){
					//If reading in the node data
					String[] tokens = currentLine.split(" ");
					//Split the line by spaces.
					//tokens[0] is the city id and not needed in this example
					int id = Integer.parseInt(tokens[0].trim());
					float x = Float.parseFloat(tokens[1].trim());
					float y = Float.parseFloat(tokens[2].trim());
					//Use Java's built in Point2D type to hold a city
					City city = new City(id,x,y);
					//Add this city into the arraylist
					result.add(city);
				}
				if (currentLine.contains("DIMENSION")){
					//Note the expected problem dimension (number of cities)
					String[] tokens = currentLine.split(":");
					dimension = Integer.parseInt(tokens[1].trim());
				}
				if (currentLine.contains("NODE_COORD_SECTION")){
					//Node data follows this line
					readingNodes = true;
				}
			}
		} catch (NumberFormatException e) {
			System.err.println("Error at line "+lineID);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	public static double routeLength(ArrayList<City> cities){
		//Calculate the length of a TSP route held in an ArrayList as a set of Points
		double result=0;//Holds the route length
		City prev = cities.get(cities.size()-1);
		//Set the previous city to the last city in the ArrayList as we need to measure the length of the entire loop
		for(City city : cities){
			//Go through each city in turn
			result += city.distance(prev);
			//get distance from the previous city
			prev = city;
			//current city will be the previous city next time
		}
		return result;
	}

}
