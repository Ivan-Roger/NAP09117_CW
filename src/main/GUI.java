package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI extends JFrame {
	private ArrayList<Point2D> cities;
	private TSP_Viewer viewer;
	private InfosPanel infos;
	private int problem;
	
	public GUI(int problem) {
		super("Traveling Salesman Problem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        this.setLocationRelativeTo(null);
        
        loadProblem(problem);
        
        initContent();
	}
	
	public void loadProblem(int problem) {
		this.cities = TSPLib.load(Main.TSP_FILES_PATH+Main.TSP_FILES[problem]);
		this.problem = problem;
	}
	
	public void initContent() {
		JPanel body = new JPanel();
		body.setLayout(new BorderLayout());
		
		viewer = new TSP_Viewer();
		body.add(viewer, BorderLayout.CENTER);
		
		JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(0,1));

		// --- Datasets ---
		JPanel ctrlDatasetP = new JPanel();
		ctrlDatasetP.setLayout(new GridLayout(0, 1));
		JLabel ctrlDatasetTitle = new JLabel("Select dataset", JLabel.CENTER);
		ctrlDatasetP.add(ctrlDatasetTitle);
		for (int pb=0; pb<Main.TSP_FILES.length; pb++) {
			JButton btn_data  = new JButton(Main.TSP_FILES[pb]);
			final int pb_id = pb; 
			btn_data.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					loadProblem(pb_id);
					viewer.drawCities(cities);
					infos.updateDataset();
					
				}
			});
			ctrlDatasetP.add(btn_data);
		}
		controls.add(ctrlDatasetP);
		// --- Algorithm ---
		JPanel ctrlAlgoP = new JPanel();
		ctrlAlgoP.setLayout(new GridLayout(0, 1));
		JLabel ctrlAlgoTitle = new JLabel("Select algorithm", JLabel.CENTER);
		ctrlAlgoP.add(ctrlAlgoTitle);
		// Base
		JButton btn_basic = new JButton("Initial state");
		btn_basic.setAlignmentX(CENTER_ALIGNMENT);
		btn_basic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadProblem(problem);
				viewer.drawCities(cities);
				infos.updateAlgo(null);
			}
		});
		ctrlAlgoP.add(btn_basic);
		// Nearest Neighbour
		JButton btn_nn = new JButton("Nearest Neighbour");
		btn_nn.setAlignmentX(CENTER_ALIGNMENT);
		btn_nn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loadProblem(problem);
				NearestNeighbour algoNN = new NearestNeighbour(cities);
				cities = algoNN.process();
				viewer.drawCities(cities);
				infos.updateAlgo(algoNN);
			}
		});
		ctrlAlgoP.add(btn_nn);
		controls.add(ctrlAlgoP);
		// --- Infos ---
		infos = new InfosPanel();
		controls.add(infos);
		// --- --- ---
		body.add(controls, BorderLayout.EAST);
		
		this.add(body);
	}
	
	private class InfosPanel extends JPanel {
		private JLabel info_nameDataset;
		private JLabel info_nbCities;
		private JLabel info_algo;
		private JLabel info_time;
		private JLabel info_routeLen;
		
		public InfosPanel() {
			this.setLayout(new GridLayout(0,1));
			this.add(new JLabel("Informations", JLabel.CENTER));
			this.setPreferredSize(new Dimension(200,500));
			// Infos
			info_nameDataset = new JLabel("Dataset: "+Main.TSP_FILES[problem]);
			this.add(info_nameDataset);
			info_nbCities = new JLabel("Nb. of cities: "+cities.size());
			this.add(info_nbCities);
			info_algo = new JLabel("Algorithm: none");
			this.add(info_algo);
			info_time = new JLabel("Processing time: 0ms");
			this.add(info_time);
			info_routeLen = new JLabel("Route length: "+TSPLib.routeLength(cities));
			this.add(info_routeLen);
		}

		public void updateAlgo(Algo algo) {
			if (algo!=null) {
				info_algo.setText("Algorithm: "+algo.getClass().getSimpleName());
				info_time.setText("Processing time: "+algo.lastExecTime()+"ms");
			} else {
				info_algo.setText("Algorithm: none");
				info_time.setText("Processing time: 0ms");
			}
			info_routeLen.setText("Route length: "+TSPLib.routeLength(cities));
		}
		
		public void updateDataset() {
			info_nameDataset.setText("Dataset: "+Main.TSP_FILES[problem]);
			info_nbCities.setText("Nb. of cities: "+cities.size());
			info_routeLen.setText("Route length: "+TSPLib.routeLength(cities));
		}
	}
	
	private class TSP_Viewer extends JPanel {
		private ArrayList<Point2D> cities = new ArrayList<>();
		private double origW;
		private double origH;
		private double startX;
		private double startY;
		
		public void drawCities(ArrayList<Point2D> cities) {
			this.cities = cities;
			System.out.println("Starting to draw "+cities.size()+" cities.");
			
			if (!cities.isEmpty()) {
				double minX = cities.get(0).getX();
				double maxX = cities.get(0).getX();
				double minY = cities.get(0).getY();
				double maxY = cities.get(0).getY();
				for (Point2D c : cities) {
					minX = Math.min(minX, c.getX());
					maxX = Math.max(maxX, c.getX());
					minY = Math.min(minY, c.getY());
					maxY = Math.max(maxY, c.getY());
				}
				origW = maxX-minX;
				origH = maxY-minY;
				startX = minX;
				startY = minY;
				System.out.println("X: min="+minX+", max="+maxX);
				System.out.println("Y: min="+minY+", max="+maxY);
			}
			
			this.repaint();
		}
		
		public void paintComponent(Graphics g) {
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, this.getWidth()-10, this.getHeight());
			g.setColor(Color.WHITE);
			g.fillRect(5, 5, this.getWidth()-20, this.getHeight()-10);

			double ratioWidth = (double)(this.getWidth()-40)/origW;
			double ratioHeight = (double)(this.getHeight()-30)/origH;

			System.out.println("Cities: width="+origW+", height="+origH);
			System.out.println("Frame: width="+this.getWidth()+", height="+this.getHeight());
			System.out.println("Ratios: width="+ratioWidth+", height="+ratioHeight);
			
			for (int i=0; i<cities.size(); i++) {
				Point2D c = cities.get(i);
				int cX = (int) ((c.getX()-startX) * ratioWidth) + 15;
				int cY = (int) ((c.getY()-startY) * ratioHeight) + 15;
				
				if (i>0) {
					Point2D prevC = cities.get(i-1);
					int prevX = (int) ((prevC.getX()-startX) * ratioWidth) + 15;
					int prevY = (int) ((prevC.getY()-startY) * ratioHeight) + 15;
					
					g.setColor(Color.RED);
					g.drawLine(prevX, prevY, cX, cY);
					
					Color color = Color.BLUE;
					if ((i-1)==0) color = Color.GREEN;
					g.setColor(color);
					g.fillOval(prevX-5, prevY-5, 10, 10);
				}
				
				Color color = Color.BLUE;
				if (i==0) color = Color.GREEN;
				else if (i==cities.size()-1) color = Color.RED;
				g.setColor(color);
				g.fillOval(cX-5, cY-5, 10, 10);
			}
		}
	}
}
