package src.Client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Authentication extends JFrame implements ActionListener {
    private Socket cSocket = null;
    DataOutputStream passchk = null;
    DataInputStream verification = null;
    String verify = "";
    JButton submit;
    JPanel panel;
    JLabel label, label1;
    String width = "", height = "";
    JTextField text1;

    Authentication(Socket cSocket) {
        System.out.println("Initializing Authentication frame");
        this.cSocket = cSocket;

        label1 = new JLabel("Password");
        text1 = new JTextField(15);
        label = new JLabel("");
        submit = new JButton("Submit");

        panel = new JPanel(new GridLayout(3, 1));
        panel.add(label1);
        panel.add(text1);
        panel.add(submit);

        add(panel, BorderLayout.CENTER);
        submit.addActionListener(this);

        setTitle("Login form");
        setSize(300, 100); // Set the size of the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensure the application exits on close
        setVisible(true); // Make the frame visible
        System.out.println("Authentication frame initialized and visible");
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println("Submit button clicked");
        String value1 = text1.getText();
        System.out.println("Button clicked, password entered: " + value1); // Debug statement
        try {
            passchk = new DataOutputStream(cSocket.getOutputStream());
            verification = new DataInputStream(cSocket.getInputStream());
            passchk.writeUTF(value1);
            System.out.println("Password sent to server");
            verify = verification.readUTF();
            System.out.println("Verification response: " + verify); // Debug statement
        } catch (IOException e) {
            System.err.println("Error during authentication");
            e.printStackTrace();
        }
        if (verify.equals("valid")) {
            try {
                width = verification.readUTF();
                height = verification.readUTF();
                System.out.println("Width: " + width + ", Height: " + height); // Debug statement
            } catch (IOException e) {
                System.err.println("Error reading width and height");
                e.printStackTrace();
            }
            @SuppressWarnings("unused")
            CreateFrame abc = new CreateFrame(cSocket, width, height);
            dispose();
        } else {
            System.out.println("Please enter valid password");
            JOptionPane.showMessageDialog(this, "Password is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }
}
