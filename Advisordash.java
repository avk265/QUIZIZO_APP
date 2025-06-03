package com.app.quizizo;

import javax.swing.*;
import java.awt.*;

public class Advisordash extends JPanel {

    public Advisordash(String username) {
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
        JLabel head = new JLabel("ADVISOR DASHBOARD");
        head.setBounds(330, 100, 300, 40);
        head.setFont(new Font("Times New Roman", Font.BOLD, 13));
        mainPanel.add(head);

        // User Icon
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

        // Centered Image Buttons
        int buttonWidth = 300;
        int buttonHeight = 300;
        int startX = 120; // Starting x-coordinate for the first button
        int yPosition = 300; // Y-coordinate to center buttons vertically

        // First Button
        JButton button1 = createImageButton("C:\\Users\\Kiran\\Desktop\\myjdbc\\java_app\\QUIZIZO\\src\\main\\java\\com\\app\\quizizo\\teacher.png");
        button1.setBounds(startX, yPosition, buttonWidth, buttonHeight);
        button1.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this); // Get the current frame
            frame.getContentPane().removeAll(); // Clear current content
            frame.getContentPane().add(new TeacherRecordsGUI(username)); // Add StudentRecordsGUI panel
            frame.revalidate();
            frame.repaint();
        });

        // Second Button
        // Second Button
        JButton button2 = createImageButton("C:\\Users\\Kiran\\Desktop\\myjdbc\\java_app\\QUIZIZO\\src\\main\\java\\com\\app\\quizizo\\student.png");
        button2.setBounds(startX + 250, yPosition, buttonWidth, buttonHeight);
        button2.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this); // Get the current frame
            frame.getContentPane().removeAll(); // Clear current content
            frame.getContentPane().add(new StudentRecordsGUI(username)); // Add StudentRecordsGUI panel
            frame.revalidate();
            frame.repaint();
        });

        // Add buttons to the main panel
        mainPanel.add(button1);
        mainPanel.add(button2);
    }

    // Helper method to create a button with an image icon
    private JButton createImageButton(String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(scaledImage));
        button.setContentAreaFilled(false); // Remove button background
        button.setFocusPainted(false); // Remove focus border
        button.setBorderPainted(false); // Remove border

        return button;
    }
}
