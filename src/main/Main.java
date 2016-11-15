package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

public class Main {
	public static final String DEFAULT_DATA_PATH = System.getProperty("user.dir").concat("\\data\\");
	
	public static String dataFilesPath = DEFAULT_DATA_PATH;
	public static File[] dataFiles; // {"pr152.tsp","att48.tsp","gr120.tsp","a280.tsp","fnl4461.tsp"}
	private static int tspProblem = 0;

	public static void main(String[] args) {
		try {
			if (args.length>=1) tspProblem = Integer.parseInt(args[0]);
		} catch(NumberFormatException ex) {
			System.err.println("Usage: java -jar TSP.jar [problem [data_folder]]");
			System.exit(-1);
		}
		if (args.length>=2) dataFilesPath = args[1];
		
		File folder = new File(dataFilesPath);
		if (!folder.exists() || !folder.isDirectory()) {
			System.err.println("Data folder doesn't exist or is invalid !");
			System.err.println("Default data folder: "+DEFAULT_DATA_PATH);
			System.err.println("Invalid data folder: "+dataFilesPath);
			System.exit(1);
		}
		
		dataFiles = folder.listFiles();
		if (dataFiles.length==0) {
			System.err.println("Data folder is empty !");
			System.exit(2);
		}
		
		if (tspProblem>=dataFiles.length) {
			System.err.println("Undefined problem "+tspProblem);
			System.exit(3);
		}
		
		graphic();
	}
	
	public static void graphic() {
		GUI gui = GUI.getInstance(tspProblem);
		gui.setVisible(true);
	}
	
	public static <T> ArrayList<T> enumerationToArrayList(Enumeration<T> input) {
		ArrayList<T> res = new ArrayList<>();
		while (input.hasMoreElements()) {
			res.add(input.nextElement());
		}
		
		return res;
	}

}
