package main.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import javax.swing.Timer;

import main.City;
import main.Main;
import main.TSPLib;
import main.algo.*;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GUI extends JFrame {
	private static GUI instance;
	
	private List<City> cities;
	private int problem;
	
	private Algo algorithm;
	private Viewer viewer;
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

	public JPanel getViewer() {
		return viewer;
	}

	public void setViewer(Viewer viewer) {
		this.viewer = (viewer!=null?viewer:new DefaultViewer());

	}

	public void drawCities(List<City> cities) {
		viewer.drawCities(cities);
	}
	
	public void update(Algo algo, List<City> cities) {
		this.cities = cities;
		drawCities(cities);
		infos.updateAlgo(algo, cities);
		processingEnd();
	}

	private void processingStart() {
		infos.setLoading(true);
		infos.updateAlgo(algorithm, cities);
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
		
		viewer = new DefaultViewer();
		body.add(viewer, BorderLayout.CENTER);
		
		JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(0,1));
		controls.setBackground(Color.LIGHT_GRAY);
		controls.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));

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
				infos.updateDataset(problem, cities);
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
				infos.updateAlgo(null, cities);
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
		JButton btn_intersect = new JButton("Solve Intersections");
		btn_intersect.setAlignmentX(CENTER_ALIGNMENT);
		btn_intersect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				algorithm = new SolveIntersect(cities);
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
		// Ant Colony
		JButton btn_antColony = new JButton("Ant Colony");
		btn_antColony.setAlignmentX(CENTER_ALIGNMENT);
		btn_antColony.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				algorithm = new AntColony(cities);
				processingStart();
				algorithm.start();
			}
		});
		ctrlAlgoP.add(btn_antColony);
		controls.add(ctrlAlgoP);
		// --- Infos ---
		infos = new InfosPanel(problem, cities);
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
}
