package com.app.quizizo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SetExamGUI extends JFrame {
    private JComboBox<String> examCodeComboBox;
    private JButton confirmButton;
    private final String dbUrl = "jdbc:mysql://localhost:3306/mydb";
    private final String dbUser = "root";
    private final String dbPassword = "kiran"; // Replace with your MySQL password

    public SetExamGUI(JFrame frame , String username) {
        setTitle("Set Exam");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on screen

        // Create and set up the panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Centered title label
        JLabel titleLabel = new JLabel("SET EXAM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across 2 columns
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment
        panel.add(titleLabel, gbc);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBounds(10,10,15,20);
        backButton.addActionListener(e -> {
            frame.getContentPane().removeAll(); // Clear current content
            frame.getContentPane().add(new Teacherdash(username,frame)); // Go back to the previous screen
            frame.revalidate();
            frame.repaint();
        });
        gbc.gridwidth = 1; // Reset to 1 column
        gbc.gridy = 1; // Move down to the next row
        gbc.anchor = GridBagConstraints.WEST; // Left alignment
        panel.add(backButton, gbc);

        // Exam Code Label
        JLabel examCodeLabel = new JLabel("Select Exam Code:");
        gbc.gridx = 0; // Reset to first column
        gbc.gridy = 2; // Move down to the next row
        gbc.anchor = GridBagConstraints.WEST; // Left alignment
        panel.add(examCodeLabel, gbc);

        // Exam Code ComboBox
        examCodeComboBox = new JComboBox<>(fetchExamCodesFromDatabase().toArray(new String[0]));
        gbc.gridx = 1; // Move to the second column
        panel.add(examCodeComboBox, gbc);

        // Confirm Button
        confirmButton = new JButton("Confirm Exam");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedExamCode = (String) examCodeComboBox.getSelectedItem();
                if (selectedExamCode != null) {
                    createNewExamTable(selectedExamCode);
                } else {
                    JOptionPane.showMessageDialog(SetExamGUI.this, "Please select a valid exam code.");
                }
            }
        });
        gbc.gridx = 0; // Reset to first column
        gbc.gridy = 3; // Move down to the next row
        gbc.gridwidth = 2; // Span across 2 columns
        panel.add(confirmButton, gbc);

        // Add the panel to the frame
        add(panel);
        setVisible(true); // Make the frame visible
    }

    // Fetch existing exam codes from the database
    private List<String> fetchExamCodesFromDatabase() {
        List<String> examCodes = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "SELECT DISTINCT exam_code FROM examcode"; // Adjust table name if needed
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    examCodes.add(rs.getString("exam_code"));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching exam codes: " + ex.getMessage());
        }
        return examCodes;
    }

    // Create a new exam table
    private void createNewExamTable(String examCode) {
        String newTableName = "new_exam_" + examCode.toLowerCase(); // New table name based on exam code

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS "+newTableName+"(" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(255) NOT NULL," +
                    "exam_code VARCHAR(255) NOT NULL," +
                    "score INT NOT NULL"+
                    ")";

            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createTableSQL);
                JOptionPane.showMessageDialog(this,"PROCEED");
            }
        } catch (SQLException ex) {
            System.err.println("Error creating new exam table: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Failed to create new exam table: " + ex.getMessage());
        }
    }
}
