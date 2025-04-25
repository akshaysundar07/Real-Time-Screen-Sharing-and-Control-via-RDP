package src.Server;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.IIOImage;
import java.util.Iterator;

public class SendScreen extends Thread {
    Socket Socket = null;
    Robot robot = null;
    Rectangle rectangle = null;
    boolean continueLoop = true;
    OutputStream oos = null;

    public SendScreen(Socket socket, Robot robot, Rectangle rect) {
        this.Socket = socket;
        this.robot = robot;
        rectangle = rect;
        start();
    }

    public void run() {
        try {
            oos = Socket.getOutputStream();
            
            while (continueLoop) {
                BufferedImage screenshot = robot.createScreenCapture(rectangle);
                
                // Compress image with better quality
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                
                // Set up image writer with compression
                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
                ImageWriter writer = writers.next();
                ImageWriteParam param = writer.getDefaultWriteParam();
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.8f); // Adjust quality (0.1 = lowest, 1.0 = highest)
                
                ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
                writer.setOutput(ios);
                writer.write(null, new IIOImage(screenshot, null, null), param);
                
                byte[] imageData = baos.toByteArray();
                byte[] sizeAr = new byte[4];
                sizeAr[0] = (byte)(imageData.length >> 24);
                sizeAr[1] = (byte)(imageData.length >> 16);
                sizeAr[2] = (byte)(imageData.length >> 8);
                sizeAr[3] = (byte)(imageData.length);
                
                oos.write(sizeAr);
                oos.write(imageData);
                oos.flush();
                
                Thread.sleep(10); // Reduced delay for smoother updates
                
                writer.dispose();
                ios.close();
                baos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


