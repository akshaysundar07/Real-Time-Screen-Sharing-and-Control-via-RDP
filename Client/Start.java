package src.Client;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.net.Socket;

public class Start {
    static String port = "5555";

    public static void main(String[] args) {
        String ip = JOptionPane.showInputDialog("Please enter server ip");

        new Start().initialize(ip, Integer.parseInt(port));
    }

    public void initialize(String ip, int port) {
        try {
            System.out.println("Attempting to connect to server at " + ip + ":" + port);
            Socket s = new Socket(ip, port);
            System.out.println("Connection established with server");

            SwingUtilities.invokeLater(() -> {
                Authentication frame1 = new Authentication(s);
                frame1.setSize(300, 100); 
                frame1.setLocation(500, 300);
                frame1.setVisible(true);
                System.out.println("Authentication frame displayed");
            });
        } catch (Exception e) {
            System.err.println("Failed to connect to server at " + ip + ":" + port);
            e.printStackTrace();
        }
    }
}
