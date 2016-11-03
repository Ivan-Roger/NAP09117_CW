package main;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public abstract class Algo extends Thread {
	private int initialSize;
	protected ArrayList<City> cities;
	private double execTime;
	
	@SuppressWarnings("unchecked")
	public Algo(ArrayList<City> cities) {
		this.cities = (ArrayList<City>) cities.clone();
		this.initialSize = cities.size();
	}
	
	public double lastExecTime() { return execTime; }
	
	abstract ArrayList<City> applyAlgo();
	
	public ArrayList<City> process() throws InvalidSizeEx {
		System.out.println("\n-------------------- "+this.getClass().getSimpleName()+" --------------------\n");
		
		long startTime = System.nanoTime();
		cities = this.applyAlgo();
		execTime = (System.nanoTime()-startTime)/1000000.0;
		
		if (cities.size()!=initialSize) throw new InvalidSizeEx(initialSize,cities.size());
		
		return cities;
	}
	
	public ArrayList<City> getCities() {
		return cities;
	}
	
	public void run() {
		try {
			this.process();

			GUI.getInstance().update(this, cities);
		} catch (InvalidSizeEx ex) {
			JOptionPane.showMessageDialog(
				null,
				"Algorithm didn't return the right number of cities !\nExpected: "+ex.getExpectedSize()+"\nFound: "+ex.getFoundSize(),
				"Invalid size !",
				JOptionPane.ERROR_MESSAGE
			);
		}
	}

}
