package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import javax.swing.Timer;

import main.algo.Algo;
import main.algo.NearestNeighbour;
import main.algo.SolveIntersectOpt;
import main.algo.TwoOpt;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	private static GUI instance;
	
	private ArrayList<City> cities;
	private int problem;
	
	private Algo algorithm;
	private TSP_Viewer viewer;
	private JPanel ctrlAlgoP;
	private InfosPanel infos;
	private JButton btn_stop;
	private JLabel label_time;
	private Timer timer;
	private long timer_startTime;
	
	private GUI(int problem) {
		super("Traveling Salesman Problem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        this.setLocationRelativeTo(null);
        
        loadProblem(problem);
        
        initContent();
        viewer.drawCities(cities);
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
		ctrlAlgoP.setVisible(false);
		timer_startTime = System.nanoTime();
		timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                label_time.setText(df.format((System.nanoTime() - timer_startTime)/1000000));
            }
        });
		timer.setInitialDelay(0);
		timer.start();
		label_time.setVisible(true);
	}

	private void processingEnd() {
		infos.setLoading(false);
		btn_stop.setVisible(false);
		ctrlAlgoP.setVisible(true);
		label_time.setVisible(false);
		timer.stop(); timer = null;
	}
	
	public void loadProblem(int problem) {
		this.cities = TSPLib.load(Main.dataFiles[problem]);
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
		ctrlAlgoP = new JPanel();
		ctrlAlgoP.setLayout(new GridLayout(0, 1));
		ctrlAlgoP.setBackground(Color.LIGHT_GRAY);
		JLabel ctrlDatasetTitle = new JLabel("Select dataset", JLabel.CENTER);
		ctrlAlgoP.add(ctrlDatasetTitle);
		String[] datasetZero = {Main.dataFiles[0].getName()};
		JComboBox<String> datasetPicker = new JComboBox<>(datasetZero);
		datasetPicker.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() != ItemEvent.SELECTED) return;
				loadProblem(datasetPicker.getSelectedIndex());
				viewer.drawCities(cities);
				infos.updateDataset();
			}
		});
		for (int pb=1; pb<Main.dataFiles.length; pb++) {
			datasetPicker.addItem(Main.dataFiles[pb].getName());
		}
		ctrlAlgoP.add(datasetPicker);
		// --- Algorithms ---
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
		// Solve Intersections v0
		/*
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
		ctrlAlgoP.add(btn_inn); // */
		// Solve Intersections
		JButton btn_intersect = new JButton("Solve Intersections");
		btn_intersect.setAlignmentX(CENTER_ALIGNMENT);
		btn_intersect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				algorithm = new SolveIntersectOpt(cities);
				processingStart();
				algorithm.start();
			}
		});
		ctrlAlgoP.add(btn_intersect);
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
		JPanel buttonsP = new JPanel();
		buttonsP.setBackground(Color.LIGHT_GRAY);
		buttonsP.setLayout(new GridLayout(0,1));
		label_time = new JLabel("00:00:00", JLabel.CENTER);
		label_time.setVisible(false);
		label_time.setAlignmentX(CENTER_ALIGNMENT);
		buttonsP.add(label_time);
		btn_stop = new JButton("Stop Algo");
		btn_stop.setVisible(false);
		btn_stop.setAlignmentX(CENTER_ALIGNMENT);
		btn_stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				algorithm.stopProcessing();
			}
		});
		buttonsP.add(btn_stop);
		JButton btn_save = new JButton("Save image");
		btn_save.setAlignmentX(CENTER_ALIGNMENT);
		btn_save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Image img = viewer.getAsImage();
				String prefix = "images/";
				(new File(prefix)).mkdirs();
				String infoName = infos.getInfoString().replace(".tsp", "");
				int num = 1;
				File saveFile = new File(prefix+infoName+"_"+num+".jpg");
				while (saveFile.exists()) {
					num++;
					saveFile = new File(prefix+infoName+"_"+num+".jpg");
				}
				try {
					ImageIO.write((RenderedImage)img, "jpg", saveFile);
				} catch (IOException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Unable to save Image :\n\n"+ex.getMessage(), "Error while saving image", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonsP.add(btn_save);
		controls.add(buttonsP);
		// --- --- ---
		body.add(controls, BorderLayout.EAST);
		
		this.add(body);
		
		// Selections :
		datasetPicker.setSelectedIndex(this.problem);
	}
	
	class InfosPanel extends JPanel {
		private JLabel info_nameDataset;
		private JLabel info_nbCities;
		private JLabel info_algo;
		private JLabel info_time;
		private JLabel info_routeLen;
		private JLabel info_improvement;
		private JLabel info_loading;
		
		private String nameDataset;
		private int nbCities;
		private String nameAlgo;
		private double time;
		private double routeLength = 0;
		private double improvement;
		
		public InfosPanel() {
			this.setLayout(new GridLayout(0,1));
			this.setBackground(Color.LIGHT_GRAY);
			this.add(new JLabel("Informations", JLabel.CENTER));
			this.setPreferredSize(new Dimension(200,500));
			// Infos
			nameDataset = Main.dataFiles[problem].getName();
			info_nameDataset = new JLabel("Dataset: "+nameDataset);
			this.add(info_nameDataset);
			
			nbCities = cities.size();
			info_nbCities = new JLabel("Nb. of cities: "+nbCities);
			this.add(info_nbCities);
			
			nameAlgo = "none";
			info_algo = new JLabel("Algorithm: "+nameAlgo);
			this.add(info_algo);
			
			time = 0.0;
			info_time = new JLabel("Processing time: "+time+"ms");
			this.add(info_time);
			
			routeLength = TSPLib.routeLength(cities);
			info_routeLen = new JLabel("Route length: "+routeLength);
			this.add(info_routeLen);
			
			improvement = 0;
			info_improvement = new JLabel("Improvement: "+(improvement==0?"...":improvement));
			this.add(info_improvement);
			
			info_loading = new JLabel("Processing ...", JLabel.CENTER);
			info_loading.setVisible(false);
			this.add(info_loading);
		}

		public String getInfoString() {
			return nameDataset+"("+nbCities+")_"+nameAlgo;
		}

		public void updateAlgo(Algo algo) {
			if (algo!=null) {
				nameAlgo = algo.getClass().getSimpleName();
				time = algo.lastExecTime();
			} else {
				nameAlgo = "none";
				time = 0.0;
			}
			info_algo.setText("Algorithm: "+nameAlgo);
			info_time.setText("Processing time: "+time+"ms");
			
			double newRouteLength = TSPLib.routeLength(cities);
			info_routeLen.setText("Route length: "+newRouteLength);
			if (routeLength!=0) {
				improvement = routeLength-newRouteLength;
				info_improvement.setText("Improvement: "+(improvement==0?"...":improvement));
			}
			routeLength = newRouteLength;
		}
		
		public void updateDataset() {
			nameDataset = Main.dataFiles[problem].getName();
			info_nameDataset.setText("Dataset: "+nameDataset);
			nbCities = cities.size();
			info_nbCities.setText("Nb. of cities: "+nbCities);
			routeLength = TSPLib.routeLength(cities);
			info_routeLen.setText("Route length: "+TSPLib.routeLength(cities));
			improvement = 0;
			info_improvement.setText("Improvement: "+(improvement==0?"...":improvement));
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
		
		public BufferedImage getAsImage() {
			BufferedImage img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		    Graphics g = img.getGraphics();
		    g.setColor(this.getForeground());
		    g.setFont(this.getFont());
		    this.paintAll(g);
			return img;
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
				g.fillOval(cX-3, cY-3, 6, 6);
				if (cities.size()<250)
					g.drawString(Integer.toString(c.getID()), cX, cY-6);
			}
		}
	}
}
