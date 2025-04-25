package src.Server;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class InitConnection {
    ServerSocket socket = null;
    DataInputStream password = null;
    DataOutputStream verify = null;
    String width = "";
    String height = "";

    InitConnection(int port, String value1) {
        Robot robot = null;
        Rectangle rectangle = null;
        try {
            System.out.println("Starting server on port " + port);
            socket = new ServerSocket(port);
            System.out.println("Server started and listening on port " + port);
            GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gDev = gEnv.getDefaultScreenDevice();
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            width = "" + dim.getWidth();
            height = "" + dim.getHeight();
            rectangle = new Rectangle(dim);
            robot = new Robot(gDev);
            drawGUI();
            while (true) {
                System.out.println("Waiting for client to connect...");
                Socket sc = socket.accept();
                System.out.println("Client connected");

                password = new DataInputStream(sc.getInputStream());
                verify = new DataOutputStream(sc.getOutputStream());

                String passwordReceived = password.readUTF();
                System.out.println("Password received: " + passwordReceived);

                if (passwordReceived.equals(value1)) {
                    System.out.println("Password is valid");
                    verify.writeUTF("valid");
                    verify.writeUTF(width);
                    verify.writeUTF(height);
                    System.out.println("Sent valid response and dimensions to client");
                    new SendScreen(sc, robot, rectangle);
                    new ReceiveEvents(sc, robot);
                } else {
                    System.out.println("Password is invalid");
                    verify.writeUTF("Invalid");
                    System.out.println("Sent invalid response to client");
                }
            }
        } catch (Exception e) {
            System.err.println("Server encountered an error");
            e.printStackTrace();
        }
    }

    private void drawGUI() {
        // Implement GUI drawing if needed
    }

    public static void main(String[] args) {
        int port = 4907; // Ensure this matches the client port
        String password = "your_password"; // Replace with your actual password
        new InitConnection(port, password);
    }
}



