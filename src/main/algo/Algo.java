package main.algo;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import main.City;
import main.GUI;
import main.InvalidSizeEx;
import main.TSPLib;

public abstract class Algo extends Thread {
	private int initialSize;
	protected ArrayList<City> cities;
	private double execTime;
	protected boolean doProcess;
	
	@SuppressWarnings("unchecked")
	public Algo(ArrayList<City> cities) {
		this.cities = (ArrayList<City>) cities.clone();
		this.initialSize = cities.size();
	}
	
	public double lastExecTime() { return execTime; }
	
	abstract ArrayList<City> applyAlgo();
	
	public ArrayList<City> process() throws InvalidSizeEx {
		doProcess = true;
		System.out.println("\n-------------------- "+this.getClass().getSimpleName()+" --------------------\n");
		
		long startTime = System.nanoTime();
		cities = this.applyAlgo();
		execTime = (System.nanoTime()-startTime)/1000000.0;
		
		if (cities.size()!=initialSize) throw new InvalidSizeEx(initialSize,cities.size());

		System.out.println(" >    --- RESULT ---");
		System.out.println(" > "+"Time: \t"+execTime);
		System.out.println(" > "+"Length: \t"+TSPLib.routeLength(cities));
		
		return cities;
	}
	
	public ArrayList<City> getCities() {
		return cities;
	}
	
	public void run() {
		try {
			this.process();
		} catch (InvalidSizeEx ex) {
			JOptionPane.showMessageDialog(
				null,
				"Algorithm didn't return the right number of cities !\nExpected: "+ex.getExpectedSize()+"\nFound: "+ex.getFoundSize(),
				"Invalid size !",
				JOptionPane.ERROR_MESSAGE
			);
		}

		GUI.getInstance().update(this, cities);
	}
	
	public void stopProcessing() {
		System.out.println(" -- INTERRUPTED PROCESSING !");
		doProcess = false;
	}

}
