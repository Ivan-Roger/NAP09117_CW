package main.algo;

import main.City;
import main.gui.AntColonyViewer;
import main.gui.GUI;
import main.TSPLib;

import java.util.*;

public class AntColony extends Algo {

	private int nbAnts;

	public AntColony(List<City> cities) {
		super(cities);
		nbAnts = 0;
	}

	protected List<City> applyAlgo() {
		GUI.getInstance().setViewer(new AntColonyViewer());
		Map<City, Map<City, Edge>> map = buildMap(cities);
		List<City> bestRoute = new ArrayList<>(cities);
		double bestRouteLength = TSPLib.routeLength(bestRoute);
		Random alea = new Random();

		while (true) {
			if (!doProcess) break;
			nbAnts++;
			System.out.println("--> Ant number "+nbAnts);

			List<City> currentRoute = new ArrayList<>();
			currentRoute.add(cities.get(0));
			System.out.print("  "+currentRoute.get(0).getID());

			for (int i=1; i<cities.size(); i++) {
				City a = currentRoute.get(i-1);
				Map<City, Edge> routes = map.get(a);
				List<Edge> possibleEdges = new ArrayList<>();
				for (Edge e : routes.values()) {
					//System.out.println("    "+e.getCityB().getID()+"\t > "+e.getScore());
					if (currentRoute.contains(e.getCityB())) continue;
					int amount = (int) Math.round(e.getScore())+1;
					for (int k=0; k<amount; k++) {
						possibleEdges.add(e);
					}
				}
				Edge selectedEdge = possibleEdges.get(alea.nextInt(possibleEdges.size()));
				System.out.print(" -> "+selectedEdge.getCityB().getID());
				selectedEdge.addPheromone(5);
				currentRoute.add(selectedEdge.getCityB());
			}

			double currentRouteLength = TSPLib.routeLength(currentRoute);
			System.out.println("\n Length : "+currentRouteLength);
			if (currentRouteLength<bestRouteLength) {
				bestRoute = currentRoute;
				bestRouteLength = currentRouteLength;
			}

			((AntColonyViewer)GUI.getInstance().getViewer()).setMap(map);
		}

		GUI.getInstance().setViewer(null);
		return bestRoute;
	}

	public Map<City, Map<City, Edge>> buildMap(List<City> in) {
		Map<City, Map<City, Edge>> out = new HashMap<>();
		for (City a : in) {
			Map<City, Edge> m = new HashMap<>();
			for (City b : in) {
				if (b==a) continue;
				m.put(b, new Edge(a,b));
			}
			out.put(a,m);
		}
		return out;
	}

	@Override
	public String getDetails() {
		String res = super.getDetails();
		res+= "Number of ants: "+nbAnts+"\n";
		return res;
	}

	public class Edge {
		private City cityA, cityB;
		private int pheromoneAmount;

		public Edge(City cityA, City cityB) {
			this.cityA = cityA;
			this.cityB = cityB;
			pheromoneAmount = 0;
		}

		public City getCityA() {
			return cityA;
		}

		public Edge setCityA(City cityA) {
			this.cityA = cityA;
			return this;
		}

		public City getCityB() {
			return cityB;
		}

		public Edge setCityB(City cityB) {
			this.cityB = cityB;
			return this;
		}

		public int getPheromoneAmount() {
			return pheromoneAmount;
		}

		public Edge setPheromoneAmount(int amount) {
			this.pheromoneAmount = amount;
			return this;
		}

		public Edge addPheromone(int amount) {
			this.pheromoneAmount = pheromoneAmount;
			return this;
		}

		public double getScore() {
			return (1/getCityA().distance(getCityB()))*500 + getPheromoneAmount();
		}
	}
}
