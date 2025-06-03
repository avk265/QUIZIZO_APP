package com.app.quizizo;

import javax.swing.*;
import java.awt.*;

public class question extends JPanel {
    JFrame frame;
    public question(JFrame frame, String username) {
        setLayout(null); // Use null layout
        setBackground(new Color(190, 187, 180));

        // Load and scale the logo
        ImageIcon icon = new ImageIcon("C:\\Users\\Kiran\\Desktop\\myjdbc\\java_app\\QUIZIZO\\src\\main\\java\\com\\app\\quizizo\\icon.png");
        Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel img = new JLabel(new ImageIcon(scaledImage));
        img.setBounds(350, 10, 100, 100);
        add(img);

        // Create and configure the label
        JLabel label = new JLabel("QUESTION EDIT");
        label.setFont(new Font("Times New Roman", Font.BOLD, 35));
        label.setHorizontalAlignment(SwingConstants.CENTER); // Center alignment for label text
        int labelWidth = 400; // Set the width of the label
        label.setBounds((800 - labelWidth) / 2, 160, labelWidth, 50); // Position below the logo
        add(label);
        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 10, 80, 30); // Position it in the top left corner
        backButton.setBackground(new Color(190, 187, 180));
        backButton.addActionListener(e -> {

            frame.getContentPane().removeAll(); // Clear current content
            frame.getContentPane().add(new Teacherdash(username,frame)); // Go back to Advisordash
            frame.revalidate();
            frame.repaint();
        });
        add(backButton);
        // Load and scale the images for buttons
        ImageIcon icon1 = new ImageIcon("C:\\Users\\Kiran\\Desktop\\myjdbc\\java_app\\QUIZIZO\\src\\main\\java\\com\\app\\quizizo\\qedit.png");
        Image img1 = icon1.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JButton button1 = new JButton(new ImageIcon(img1));
        button1.setContentAreaFilled(false);
        button1.setFocusPainted(false);
        button1.setBorderPainted(false);

        // Action Listener for Add Question Button
        button1.addActionListener(e -> {
            // Open the AddQuestionPanel when button1 is clicked

            frame.getContentPane().removeAll();
            frame.getContentPane().add(new AddQuestionPanel(frame,username));
            frame.revalidate();
            frame.repaint();
        });

        ImageIcon icon4 = new ImageIcon("C:\\Users\\Kiran\\Desktop\\myjdbc\\java_app\\QUIZIZO\\src\\main\\java\\com\\app\\quizizo\\modify.png");
        Image img4 = icon4.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JButton button4 = new JButton(new ImageIcon(img4));
        button4.setContentAreaFilled(false);
        button4.setFocusPainted(false);
        button4.setBorderPainted(false);

        // Action Listener for Modify Questions Button
        button4.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.getContentPane().add(new ModifyQuestionPanel(frame,username));
            frame.revalidate();
            frame.repaint();
        });

        // Set button bounds
        int buttonWidth = 150; // Width of each button
        int buttonSpacing = 25; // Space between buttons
        button1.setBounds(200, 220, buttonWidth, 150);
        button4.setBounds(400, 220, buttonWidth, 150);

        // Add buttons to the panel
        add(button1);
        add(button4);

        // Create and position labels for each button
        JLabel label1 = new JLabel("Add Question");
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setBounds(200, 370, buttonWidth, 30);
        add(label1);

        JLabel label4 = new JLabel("Modify Question");
        label4.setHorizontalAlignment(SwingConstants.CENTER);
        label4.setBounds(400, 370, buttonWidth, 30);
        add(label4);
    }
}
