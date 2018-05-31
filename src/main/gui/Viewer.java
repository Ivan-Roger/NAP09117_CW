package main.gui;

import main.City;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class Viewer extends JPanel {
    private List<City> cities = new ArrayList<>();
    private double citiesWidth;
    private double citiesHeight;
    private double startX;
    private double startY;

    public void drawCities(List<City> cities) {
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
            citiesWidth = maxX-minX;
            citiesHeight = maxY-minY;
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

    @Override
    protected void paintComponent(Graphics g) {
        // System.out.println("Updating component.");
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.WHITE);
        g.fillRect(5, 5, this.getWidth()-10, this.getHeight()-10);

        // System.out.println("Cities: width="+origW+", height="+origH);
        // System.out.println("Frame: width="+this.getWidth()+", height="+this.getHeight());
        // System.out.println("Ratios: width="+ratioWidth+", height="+ratioHeight);

        draw(g);
    }

    public abstract void draw(Graphics g);

    public void drawCity(Graphics g, City c, Color color) {
        if (color!=null) g.setColor(color);
        int cX = (int) ((c.getX()-getStartX()) * getRatioWidth()) + 13;
        int cY = (int) ((c.getY()-getStartY()) * getRatioHeight()) + 23;
        g.fillOval(cX-3, cY-3, 6, 6);
        if (getCities().size()<250)
            g.drawString(Integer.toString(c.getID()), cX, cY-6);
    }

    public void drawLine(Graphics g, City a, City b, Color color) {
        if (color!=null) g.setColor(color);
        int cX = (int) ((a.getX()-getStartX()) * getRatioWidth()) + 13;
        int cY = (int) ((a.getY()-getStartY()) * getRatioHeight()) + 23;
        int prevX = (int) ((b.getX()-getStartX()) * getRatioWidth()) + 13;
        int prevY = (int) ((b.getY()-getStartY()) * getRatioHeight()) + 23;
        g.drawLine(prevX, prevY, cX, cY);
    }

    public List<City> getCities() {
        return cities;
    }

    public double getCitiesWidth() {
        return citiesWidth;
    }

    public double getCitiesHeight() {
        return citiesHeight;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public double getRatioWidth() {
        return (double)(this.getWidth()-40)/ getCitiesWidth();
    }

    public double getRatioHeight() {
        return (double)(this.getHeight()-40)/ getCitiesHeight();
    }
}
