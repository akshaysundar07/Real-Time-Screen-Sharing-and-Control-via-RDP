package src.Server;

import java.awt.Robot;
// import java.awt.event.InputEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ReceiveEvents extends Thread{ 
    Socket socket = null;
    Robot robot = null;
    boolean continueLoop = true;

    public ReceiveEvents(Socket socket, Robot robot){
        this.socket = socket;
        this.robot = robot;
        start();
    }

    public void run(){
        Scanner scanner = null;
        try{
            scanner = new Scanner(socket.getInputStream());

            while(continueLoop){
                int command = scanner.nextInt();
                switch(command){
                    case -1: // PRESS_MOUSE
                        robot.mousePress(scanner.nextInt());
                        break;
                    case -2: // RELEASE_MOUSE
                        robot.mouseRelease(scanner.nextInt());
                        break;
                    case -3: // PRESS_KEY
                        robot.keyPress(scanner.nextInt());
                        break;
                    case -4: // RELEASE_KEY
                        robot.keyRelease(scanner.nextInt());
                        break;
                    case -5: // MOVE_MOUSE
                        robot.mouseMove(scanner.nextInt(), scanner.nextInt());
                        break;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
}
