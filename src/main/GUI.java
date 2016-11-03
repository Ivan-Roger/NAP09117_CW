package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	private static GUI instance;
	private ArrayList<City> cities;
	private Algo algorithm;
	private TSP_Viewer viewer;
	private JPanel ctrlDatasetP;
	private JPanel ctrlAlgoP;
	private InfosPanel infos;
	private JButton btn_stop;
	private int problem;
	
	private GUI(int problem) {
		super("Traveling Salesman Problem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        this.setLocationRelativeTo(null);
        
        loadProblem(problem);
        
        initContent();
	}

	public static GUI getInstance(int tspProblem) {
		if (instance==null) {
			instance = new GUI(tspProblem);
		}
		return instance;
	}

	public static GUI getInstance() {
		return instance;
	}
	
	public void drawCities(ArrayList<City> cities) {
		viewer.drawCities(cities);
	}
	
	public void update(Algo algo, ArrayList<City> cities) {
		this.cities = cities;
		drawCities(cities);
		infos.updateAlgo(algo);
		processingEnd();
	}

	private void processingStart() {
		infos.setLoading(true);
		infos.updateAlgo(algorithm);
		btn_stop.setVisible(true);
		ctrlDatasetP.setVisible(false);
		ctrlAlgoP.setVisible(false);
	}

	private void processingEnd() {
		infos.setLoading(false);
		btn_stop.setVisible(false);
		ctrlDatasetP.setVisible(true);
		ctrlAlgoP.setVisible(true);
	}
	
	public void loadProblem(int problem) {
		this.cities = TSPLib.load(Main.TSP_FILES_PATH+Main.TSP_FILES[problem]);
		this.problem = problem;
	}
	
	public void initContent() {
		JPanel body = new JPanel();
		body.setLayout(new BorderLayout());
		body.setBackground(Color.LIGHT_GRAY);
		
		viewer = new TSP_Viewer();
		body.add(viewer, BorderLayout.CENTER);
		
		JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(0,1));
		controls.setBackground(Color.LIGHT_GRAY);

		// --- Datasets ---
		ctrlDatasetP = new JPanel();
		ctrlDatasetP.setLayout(new GridLayout(0, 1));
		ctrlDatasetP.setBackground(Color.LIGHT_GRAY);
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
		ctrlAlgoP = new JPanel();
		ctrlAlgoP.setLayout(new GridLayout(0, 1));
		ctrlAlgoP.setBackground(Color.LIGHT_GRAY);
		JLabel ctrlAlgoTitle = new JLabel("Select algorithm", JLabel.CENTER);
		ctrlAlgoP.add(ctrlAlgoTitle);
		// Base
		JButton btn_basic = new JButton("Reload");
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
				algorithm = new NearestNeighbour(cities);
				processingStart();
				algorithm.start();
			}
		});
		ctrlAlgoP.add(btn_nn);
		// Solve Intersections
		JButton btn_inn = new JButton("Solve Intersections");
		btn_inn.setAlignmentX(CENTER_ALIGNMENT);
		btn_inn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				algorithm = new SolveIntersect(cities);
				processingStart();
				algorithm.start();
			}
		});
		ctrlAlgoP.add(btn_inn);
		// Two Opt
		JButton btn_2opt = new JButton("Two opt");
		btn_2opt.setAlignmentX(CENTER_ALIGNMENT);
		btn_2opt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				algorithm = new TwoOpt(cities);
				processingStart();
				algorithm.start();
			}
		});
		ctrlAlgoP.add(btn_2opt);
		controls.add(ctrlAlgoP);
		// --- Infos ---
		infos = new InfosPanel();
		controls.add(infos);
		// Stop algo
		btn_stop = new JButton("Stop Algo");
		btn_stop.setVisible(false);
		btn_stop.setAlignmentX(CENTER_ALIGNMENT);
		btn_stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				algorithm.stop();
				processingEnd();
			}
		});
		controls.add(btn_stop);
		// --- --- ---
		body.add(controls, BorderLayout.EAST);
		
		this.add(body);
	}
	
	class InfosPanel extends JPanel {
		private JLabel info_nameDataset;
		private JLabel info_nbCities;
		private JLabel info_algo;
		private JLabel info_time;
		private JLabel info_routeLen;
		private JLabel info_improvement;
		private JLabel info_loading;
		private double routeLength = 0;
		
		public InfosPanel() {
			this.setLayout(new GridLayout(0,1));
			this.setBackground(Color.LIGHT_GRAY);
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
			routeLength = TSPLib.routeLength(cities);
			info_routeLen = new JLabel("Route length: "+routeLength);
			this.add(info_routeLen);
			info_improvement = new JLabel("Improvement: ...");
			this.add(info_improvement);
			info_loading = new JLabel("Processing ... ", JLabel.CENTER);
			info_loading.setVisible(false);
			this.add(info_loading);
		}

		public void updateAlgo(Algo algo) {
			if (algo!=null) {
				info_algo.setText("Algorithm: "+algo.getClass().getSimpleName());
				info_time.setText("Processing time: "+algo.lastExecTime()+"ms");
			} else {
				info_algo.setText("Algorithm: none");
				info_time.setText("Processing time: 0ms");
			}
			double newRouteLength = TSPLib.routeLength(cities);
			info_routeLen.setText("Route length: "+newRouteLength);
			if (routeLength!=0) {
				info_improvement.setText("Improvement: "+(int)Math.ceil(routeLength-newRouteLength));
			}
			routeLength = newRouteLength;
		}
		
		public void updateDataset() {
			info_nameDataset.setText("Dataset: "+Main.TSP_FILES[problem]);
			info_nbCities.setText("Nb. of cities: "+cities.size());
			info_routeLen.setText("Route length: "+TSPLib.routeLength(cities));
			info_improvement.setText("Improvement: ...");
			routeLength=0;
		}
		
		public void setLoading(boolean loading) {
			info_time.setVisible(!loading);
			info_routeLen.setVisible(!loading);
			info_improvement.setVisible(!loading);
			info_loading.setVisible(loading);
		}
	}
	
	class TSP_Viewer extends JPanel {
		private ArrayList<City> cities = new ArrayList<>();
		private double origW;
		private double origH;
		private double startX;
		private double startY;
		
		public void drawCities(ArrayList<City> cities) {
			this.cities = cities;
			// System.out.println("Starting to draw "+cities.size()+" cities.");
			
			if (!cities.isEmpty()) {
				double minX = cities.get(0).getX();
				double maxX = cities.get(0).getX();
				double minY = cities.get(0).getY();
				double maxY = cities.get(0).getY();
				for (City c : cities) {
					minX = Math.min(minX, c.getX());
					maxX = Math.max(maxX, c.getX());
					minY = Math.min(minY, c.getY());
					maxY = Math.max(maxY, c.getY());
				}
				origW = maxX-minX;
				origH = maxY-minY;
				startX = minX;
				startY = minY;
				// System.out.println("X: min="+minX+", max="+maxX);
				// System.out.println("Y: min="+minY+", max="+maxY);
			}
			
			this.repaint();
		}
		
		public void paintComponent(Graphics g) {
			// System.out.println("Updating component.");
			g.setColor(Color.GRAY);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.setColor(Color.WHITE);
			g.fillRect(5, 5, this.getWidth()-10, this.getHeight()-10);

			double ratioWidth = (double)(this.getWidth()-40)/origW;
			double ratioHeight = (double)(this.getHeight()-40)/origH;

			// System.out.println("Cities: width="+origW+", height="+origH);
			// System.out.println("Frame: width="+this.getWidth()+", height="+this.getHeight());
			// System.out.println("Ratios: width="+ratioWidth+", height="+ratioHeight);
			
			for (int i=1; i<cities.size(); i++) {
				City c = cities.get(i);
				int cX = (int) ((c.getX()-startX) * ratioWidth) + 13;
				int cY = (int) ((c.getY()-startY) * ratioHeight) + 23;
				
				City prevC = cities.get(i-1);
				int prevX = (int) ((prevC.getX()-startX) * ratioWidth) + 13;
				int prevY = (int) ((prevC.getY()-startY) * ratioHeight) + 23;
				
				g.setColor(Color.RED);
				g.drawLine(prevX, prevY, cX, cY);
			}
			
			for (int i=0; i<cities.size(); i++) {
				City c = cities.get(i);
				int cX = (int) ((c.getX()-startX) * ratioWidth) + 13;
				int cY = (int) ((c.getY()-startY) * ratioHeight) + 23;
				
				Color color = Color.BLUE;
				if (i==0) color = Color.GREEN;
				else if (i==cities.size()-1) color = Color.RED;
				g.setColor(color);
				g.fillOval(cX-5, cY-5, 10, 10);
				g.drawString(Integer.toString(c.getID()), cX, cY-6);
			}
		}
	}
}
