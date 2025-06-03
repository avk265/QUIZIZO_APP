package com.app.quizizo;

import javax.swing.*;
import java.awt.*;

public class Studentdash extends JPanel {


    public Studentdash(String username) {
        setLayout(null);
        setBackground(new Color(255, 255, 255));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(190, 187, 180));
        mainPanel.setBounds(0, 0, 800, 800);
        add(mainPanel);

        // Dashboard Icon
        ImageIcon icon = new ImageIcon("C:\\Users\\Kiran\\Desktop\\myjdbc\\java_app\\QUIZIZO\\src\\main\\java\\com\\app\\quizizo\\icon.png");
        Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel img = new JLabel(new ImageIcon(scaledImage));
        img.setBounds(350, 10, 100, 100);
        mainPanel.add(img);

        // Header
        JLabel head = new JLabel("STUDENT DASHBOARD");
        head.setBounds(330, 100, 300, 40);
        head.setFont(new Font("Times New Roman", Font.BOLD, 13));
        mainPanel.add(head);

        // Centered Image Buttons
        int buttonSize = 200; // Size for the buttons
        int buttonSpacing = 50; // Space between the buttons
        int startX = (800 - buttonSize * 2 - buttonSpacing) / 2; // Calculate starting x-coordinate for centering
        int yPosition = 300; // Y-coordinate for button row

        // Create and add the two buttons with messages
        JButton button1 = createImageButton("C:\\Users\\Kiran\\Desktop\\myjdbc\\java_app\\QUIZIZO\\src\\main\\java\\com\\app\\quizizo\\attend.jpg",150);
        button1.setBounds(startX, yPosition, 150, 300);
        button1.addActionListener(e ->  new AttendExam(username).setVisible(true));

        JButton button2 = createImageButton("C:\\Users\\Kiran\\Desktop\\myjdbc\\java_app\\QUIZIZO\\src\\main\\java\\com\\app\\quizizo\\progress.jpg",250);
        button2.setBounds(startX + buttonSize + buttonSpacing, yPosition, 250, 300);
        button2.addActionListener(e -> new AttendExam(username).setVisible(true));
        // Add buttons to the main panel
        mainPanel.add(button1);
        mainPanel.add(button2);

        ImageIcon user = new ImageIcon("C:\\Users\\Kiran\\Desktop\\myjdbc\\java_app\\QUIZIZO\\src\\main\\java\\com\\app\\quizizo\\user.png");
        Image usersc = user.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        JLabel imgu = new JLabel(new ImageIcon(usersc));
        imgu.setBounds(690, 10, 75, 75);
        mainPanel.add(imgu);

        // Logout Button
        JButton logout = new JButton("Logout");
        logout.setBounds(680, 90, 100, 30);
        logout.setBackground(new Color(190, 187, 180));
        logout.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(new Login(frame));
            frame.revalidate();
            frame.repaint();
        });
        mainPanel.add(logout);
    }

    // Helper method to create a button with an image icon
    private JButton createImageButton(String imagePath,int wid) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(wid, 300, Image.SCALE_SMOOTH); // Changed size to 200x200
        JButton button = new JButton(new ImageIcon(scaledImage));
        button.setContentAreaFilled(false); // Remove button background
        button.setFocusPainted(false); // Remove focus border
        button.setBorderPainted(false); // Remove border

        return button;
    }
}
