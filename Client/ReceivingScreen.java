package src.Client;

// import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.ByteArrayInputStream;
// import java.io.IOException;
import java.io.InputStream;
// import java.io.ObjectInput;
import java.io.ObjectInputStream;

import javax.swing.JPanel;

class ReceivingScreen extends Thread{
    @SuppressWarnings("unused")
    private ObjectInputStream cObjectInputStream = null;
    private JPanel cPanel = null;
    @SuppressWarnings("unused")
    private boolean continueLoop = true;
    InputStream oIn = null;
    public static Image image1 = null;  // Make it static so CreateFrame can access it

    public ReceivingScreen(InputStream in, JPanel p){
        oIn = in;
        cPanel = p;
        start();
    }

    public void run() {
        try {
            while(true) {
                byte[] sizeAr = new byte[4];
                int bytesRead = oIn.read(sizeAr);
                if (bytesRead < 0) break;
                
                // Calculate size with unsigned values to prevent negative numbers
                int size = 0;
                size = ((sizeAr[0] & 0xFF) << 24) |
                       ((sizeAr[1] & 0xFF) << 16) |
                       ((sizeAr[2] & 0xFF) << 8) |
                       (sizeAr[3] & 0xFF);
                
                // Validate size to prevent memory issues
                if (size <= 0 || size > 1000000000) {
                    System.out.println("Invalid size received: " + size);
                    continue;
                }

                byte[] imageData = new byte[size];
                bytesRead = 0;
                int remainingBytes = size;
                while (bytesRead < size) {
                    int n = oIn.read(imageData, bytesRead, remainingBytes);
                    if (n < 0) break;
                    bytesRead += n;
                    remainingBytes -= n;
                }

                ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                BufferedImage bufferedImage = ImageIO.read(bis);
                if(bufferedImage != null) {
                    image1 = bufferedImage;
                    cPanel.repaint();  // Request immediate repaint
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
