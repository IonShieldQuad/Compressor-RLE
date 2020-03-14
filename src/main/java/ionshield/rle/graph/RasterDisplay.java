package ionshield.rle.graph;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RasterDisplay extends JPanel {

    private static final int MARGIN_X = 50;
    private static final int MARGIN_Y = 50;
    
    private BufferedImage image;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (image != null) {
         
            int imageWidth = image.getWidth();
            int imageHeight = image.getHeight();
            
            double dx = (getWidth() - MARGIN_X * 2) / (double)imageWidth;
            double dy = (getHeight() - MARGIN_Y * 2) / (double)imageHeight;
            
            for (int i = 0; i < imageHeight; i++) {
                for (int j = 0; j < imageWidth; j++) {
                    g.setColor(new Color(image.getRGB(j, i)));
                    g.fillRect((int)Math.round(MARGIN_X + dx * j), (int)Math.round(MARGIN_Y + dy * i), (int)Math.round(dx), (int)Math.round(dy));
                }
            }
            
        }
    }
    
    public BufferedImage getImage() {
        return image;
    }
    
    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
