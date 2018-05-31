package main.gui;
import java.awt.*;

public class DefaultViewer extends Viewer {
    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(72,128,232));
        for (int i=1; i<getCities().size(); i++) {
            drawLine(g, getCities().get(i-1), getCities().get(i), null);
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
