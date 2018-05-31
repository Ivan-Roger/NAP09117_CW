package main.gui;

import main.City;
import main.Main;
import main.TSPLib;
import main.algo.Algo;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

class InfosPanel extends JPanel {
    private JTextArea infoArea;
    private JLabel infoLoading;

    private boolean isLoading = false;

    private String nameDataset;
    private int nbCities;
    private Algo algo;
    private double routeLength = 0;
    private double improvement;

    public InfosPanel(int problem, List<City> cities) {
        this.setLayout(new BorderLayout());
        this.setBackground(Color.LIGHT_GRAY);
        JLabel title = new JLabel("Informations", JLabel.CENTER);
        this.add(title, BorderLayout.NORTH);
        this.setPreferredSize(new Dimension(200,500));
        this.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Infos
        nameDataset = Main.dataFiles[problem].getName();
        nbCities = cities.size();
        algo = null;
        routeLength = TSPLib.routeLength(cities);
        improvement = 0;

        infoArea = new JTextArea();
        infoArea.setText(this.getFullInfoString());
        infoArea.setEditable(false);
        infoArea.setBackground(Color.LIGHT_GRAY);
        infoArea.setBorder(BorderFactory.createEmptyBorder(30, 5, 0, 5));
        this.add(infoArea, BorderLayout.CENTER);

        infoLoading = new JLabel("Processing ...", JLabel.CENTER);
        infoLoading.setVisible(false);
        this.add(infoLoading, BorderLayout.SOUTH);
    }

    public String getInfoString() {
        return nameDataset+"("+nbCities+")"+(algo!=null?"_"+algo.getClass().getSimpleName():"");
    }

    public String getFullInfoString() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss:SSS");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        String res = "";
        res += "Dataset: "+nameDataset+"\n";
        res += "Nb. of cities: "+nbCities+"\n";
        if (algo!=null) res += "Algorithm: "+algo.getClass().getSimpleName()+"\n";

        if (!isLoading) {
            if (algo != null) {
                double time = algo.lastExecTime();
                if (time!=0.0) res += "Processing time: "+df.format(time)+"\n";
            }
            res += "Route length: "+routeLength+"\n";
            if (improvement!=0) res += "Improvement: "+(improvement==0?"...":improvement)+"\n";
            if (algo != null) res += algo.getDetails();
        }

        return res;
    }

    public void updateAlgo(Algo algo, List<City> cities) {
        this.algo = algo;

        double newRouteLength = TSPLib.routeLength(cities);
        if (routeLength!=0) {
            improvement = routeLength-newRouteLength;
        }
        routeLength = newRouteLength;

        infoArea.setText(this.getFullInfoString());
    }

    public void updateDataset(int problem, List<City> cities) {
        nameDataset = Main.dataFiles[problem].getName();
        nbCities = cities.size();
        algo = null;
        routeLength = TSPLib.routeLength(cities);
        improvement = 0;

        infoArea.setText(this.getFullInfoString());
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
        infoLoading.setVisible(loading);

        infoArea.setText(this.getFullInfoString());
    }
}
