package main.gui;

import main.City;
import main.algo.AntColony;

import java.awt.*;
import java.util.Map;

public class AntColonyViewer extends Viewer {
    private Map<City, Map<City, AntColony.Edge>> map;
    private double maxScore;

    public void setMap(Map<City, Map<City, AntColony.Edge>> map) {
        this.map = map;

        double max = 0;
        for (City a : map.keySet()) {
            Map<City, AntColony.Edge> routes = map.get(a);
            for (City b : routes.keySet()) {
                max = Math.max(max, routes.get(b).getScore());
            }
        }
        maxScore = max;
    }

    @Override
    public void draw(Graphics g) {
        if (map!=null)
        for (City a : map.keySet()) {
            Map<City, AntColony.Edge> routes = map.get(a);
            for (City b : routes.keySet()) {
                int redLevel = 44 + (int)Math.round((routes.get(b).getScore()/maxScore)*211);
                drawLine(g, a, b, new Color(redLevel, 0, 0));
            }
        }

        g.setColor(new Color(44,44,255));
        for (int i=1; i<getCities().size()-1; i++) {
            drawCity(g, getCities().get(i), null);
        }

        g.setColor(Color.GREEN);
        drawCity(g, getCities().get(0), null);

        g.setColor(Color.RED);
        drawCity(g, getCities().get(getCities().size()-1), null);
    }
}
