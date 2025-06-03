package com.app.quizizo;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class main_gui {

    public JFrame mainFrame;
    public CardLayout cardLayout;
    public JPanel cardPanel;

    public static void main(String[] args) {


        SwingUtilities.invokeLater(main_gui::new);  // Launch GUI

    }

    public main_gui() {
        // Set up the main frame
        mainFrame = new JFrame("ONLINE EXAMINATION SYSTEM");
        mainFrame.setSize(800, 800);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set up CardLayout and main panel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        mainFrame.add(cardPanel);

        // Add different panels to the CardLayout
        cardPanel.add(new WelcomePanel(), "WelcomePanel");
        cardPanel.add(new Login(mainFrame), "LoginPanel");
        cardPanel.add(new Advisordash(null),"Advisordash");

        // Show the initial panel
        cardLayout.show(cardPanel, "WelcomePanel");


        // Display the main frame
        mainFrame.setVisible(true);
    }

    // Welcome panel with the start button to switch to login panel
    private class WelcomePanel extends JPanel {
        public WelcomePanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(new Color(190, 187, 180));

            // Set application icon
            ImageIcon icon = new ImageIcon("C:\\Users\\Kiran\\Desktop\\myjdbc\\java_app\\QUIZIZO\\src\\main\\java\\com\\app\\quizizo\\icon.png");
            Image scaledImage = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            ImageIcon ico = new ImageIcon(scaledImage);
            JLabel img = new JLabel(ico);
            img.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Space before image for centering
            add(Box.createRigidArea(new Dimension(0, 50)));
            add(img);
            add(Box.createRigidArea(new Dimension(0, 20)));

            // Start Button
            JButton startButton = new JButton("START");
            startButton.setFont(new Font("Verdana", Font.BOLD, 16));
            startButton.setForeground(Color.black);
            startButton.setBackground(new Color(190, 187, 180));
            startButton.setFocusPainted(false);
            startButton.setBorderPainted(false);
            startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Add action listener to switch to the Login panel
            startButton.addActionListener(e -> cardLayout.show(cardPanel, "LoginPanel"));

            // Add the start button to the panel
            add(startButton);

            // Space after the button
            add(Box.createRigidArea(new Dimension(0, 50)));
        }
    }

    // Login panel
    // Method to check if the network connection is available

}
