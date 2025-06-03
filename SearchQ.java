package com.app.quizizo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SearchQ extends JPanel {
    private JComboBox<String> examCodeComboBox;
    private DefaultTableModel tableModel;
    JFrame parentFrame;

    public SearchQ(JFrame frame, String username) {
        setLayout(null);
        setBackground(new Color(190, 187, 180));

        // Label and ComboBox for selecting exam code
        JLabel searchLabel = new JLabel("Select Exam Code:");
        searchLabel.setBounds(50, 50, 150, 30);
        add(searchLabel);

        examCodeComboBox = new JComboBox<>();
        examCodeComboBox.setBounds(200, 50, 150, 30);
        loadExistingExamCodes(examCodeComboBox); // Load existing exam codes for searching
        add(examCodeComboBox);

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
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(370, 50, 100, 30);
        add(searchButton);

        // Table for displaying questions
        String[] columnNames = {"ID", "Exam Code", "Question", "Option A", "Option B", "Option C", "Option D", "Correct Answer"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable questionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(questionTable);
        scrollPane.setBounds(50, 90, 700, 400);
        add(scrollPane);

        // Action Listener for Search Button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedExamCode = (String) examCodeComboBox.getSelectedItem();
                if (selectedExamCode != null) {
                    loadQuestionsForExamCode(selectedExamCode, tableModel);
                } else {
                    JOptionPane.showMessageDialog(SearchQ.this, "Please select an exam code.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    // Method to load existing exam codes into a ComboBox
    private void loadExistingExamCodes(JComboBox<String> comboBox) {
        comboBox.removeAllItems(); // Clear existing items
        String url = "jdbc:mysql://localhost:3306/mydb";
        String user = "root";
        String password = "kiran"; // Replace with your MySQL password
        List<String> examCodes = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT DISTINCT exam_code FROM examcode";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    examCodes.add(rs.getString("exam_code"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        // Add existing codes to comboBox
        for (String code : examCodes) {
            comboBox.addItem(code);
        }
        comboBox.addItem("New");
    }

    // Method to load questions for a selected exam code into the table
    private void loadQuestionsForExamCode(String examCode, DefaultTableModel tableModel) {
        String url = "jdbc:mysql://localhost:3306/mydb";
        String user = "root";
        String password = "kiran"; // Replace with your MySQL password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM examcode WHERE exam_code = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, examCode);
                try (ResultSet rs = pstmt.executeQuery()) {
                    // Clear existing rows
                    tableModel.setRowCount(0);
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String question = rs.getString("question");
                        String optionA = rs.getString("option_a");
                        String optionB = rs.getString("option_b");
                        String optionC = rs.getString("option_c");
                        String optionD = rs.getString("option_d");
                        String correctAnswer = rs.getString("correct_answer");
                        // Add a new row to the table
                        tableModel.addRow(new Object[]{id, examCode, question, optionA, optionB, optionC, optionD, correctAnswer});
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading questions from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
