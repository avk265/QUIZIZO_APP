package com.app.quizizo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AddQuestionPanel extends JPanel {
    private JComboBox<String> examCodeComboBox;
    static JFrame frame1;
    public AddQuestionPanel(JFrame frame, String username) {
        setLayout(null);
        setBackground(new Color(190, 187, 180));

        // Header for "ADD QUESTIONS"
        JLabel header = new JLabel("ADD QUESTIONS ");
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setBounds(300, 100, 200, 30);
        add(header);
        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 10, 80, 30); // Position it in the top left corner
        backButton.setBackground(new Color(190, 187, 180));
        backButton.addActionListener(e -> {
            frame.getContentPane().removeAll(); // Clear current content
            frame.getContentPane().add(new question(frame,username)); // Go back to Advisordash
            frame.revalidate();
            frame.repaint();
        });
        add(backButton);

        // Exam Code Label and Combo Box
        JLabel examCodeLabel = new JLabel("Exam Code:");
        examCodeLabel.setBounds(50, 160, 100, 30);
        add(examCodeLabel);

        examCodeComboBox = new JComboBox<>();
        examCodeComboBox.setBounds(150, 160, 100, 30);
        examCodeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCode = (String) examCodeComboBox.getSelectedItem();
                if ("New".equals(selectedCode)) {
                    examCodeComboBox.setEditable(true); // Make it editable for new input
                    examCodeComboBox.setSelectedItem(""); // Clear the selection
                } else {
                    examCodeComboBox.setEditable(false); // Set back to non-editable for existing codes
                }
            }
        });
        add(examCodeComboBox);
        loadExistingExamCodes(); // Load existing exam codes from database

        // Question Label and Text Field
        JLabel questionLabel = new JLabel("Question:");
        questionLabel.setBounds(50, 210, 100, 30);
        add(questionLabel);

        JTextField questionField = new JTextField();
        questionField.setBounds(150, 210, 500, 30);
        add(questionField);

        // Option Fields (A, B, C, D)
        JLabel optionALabel = new JLabel("Option A:");
        optionALabel.setBounds(50, 260, 100, 30);
        add(optionALabel);

        JTextField optionAField = new JTextField();
        optionAField.setBounds(150, 260, 500, 30);
        add(optionAField);

        JLabel optionBLabel = new JLabel("Option B:");
        optionBLabel.setBounds(50, 310, 100, 30);
        add(optionBLabel);

        JTextField optionBField = new JTextField();
        optionBField.setBounds(150, 310, 500, 30);
        add(optionBField);

        JLabel optionCLabel = new JLabel("Option C:");
        optionCLabel.setBounds(50, 360, 100, 30);
        add(optionCLabel);

        JTextField optionCField = new JTextField();
        optionCField.setBounds(150, 360, 500, 30);
        add(optionCField);

        JLabel optionDLabel = new JLabel("Option D:");
        optionDLabel.setBounds(50, 410, 100, 30);
        add(optionDLabel);

        JTextField optionDField = new JTextField();
        optionDField.setBounds(150, 410, 500, 30);
        add(optionDField);

        // Correct Answer Selection
        JLabel correctAnswerLabel = new JLabel("Correct Answer:");
        correctAnswerLabel.setBounds(50, 460, 100, 30);
        add(correctAnswerLabel);

        JComboBox<String> correctAnswerComboBox = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        correctAnswerComboBox.setBounds(150, 460, 50, 30);
        add(correctAnswerComboBox);

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(350, 510, 100, 30);
        add(submitButton);

        // Action Listener for Submit Button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String examCode = (String) examCodeComboBox.getSelectedItem();
                if ("New".equals(examCode)) {
                    examCode = examCodeComboBox.getEditor().getItem().toString(); // Get the user-inputted exam code
                }
                String question = questionField.getText();
                String optionA = optionAField.getText();
                String optionB = optionBField.getText();
                String optionC = optionCField.getText();
                String optionD = optionDField.getText();
                String correctAnswer = (String) correctAnswerComboBox.getSelectedItem();

                // Validate inputs
                if (!isValidExamCode(examCode)) {
                    JOptionPane.showMessageDialog(AddQuestionPanel.frame1, "Exam Code must be a 6-digit alphanumeric input!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (question.isEmpty() || optionA.isEmpty() || optionB.isEmpty() ||
                        optionC.isEmpty() || optionD.isEmpty()) {
                    JOptionPane.showMessageDialog(AddQuestionPanel.this.frame1, "All fields must be filled out!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save the question to the database
                saveToDatabase(examCode, question, optionA, optionB, optionC, optionD, correctAnswer);
                JOptionPane.showMessageDialog(AddQuestionPanel.this.frame1, "Question added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Reload exam codes to update the combo box
                loadExistingExamCodes();

                // Clear fields for a new question entry
                examCodeComboBox.setSelectedIndex(0);
                questionField.setText("");
                optionAField.setText("");
                optionBField.setText("");
                optionCField.setText("");
                optionDField.setText("");
                correctAnswerComboBox.setSelectedIndex(0);
            }
        });
    }

    // Method to check if the exam code is valid
    private boolean isValidExamCode(String examCode) {
        return examCode.matches("^[A-Za-z0-9]{6}$");
    }

    // Method to load existing exam codes from the database
    private void loadExistingExamCodes() {
        String url = "jdbc:Mysql://localhost:3306/mydb";
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
            JOptionPane.showMessageDialog(this, "Error loading exam codes from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        // Clear existing items before adding
        examCodeComboBox.removeAllItems();

        // Add existing exam codes and "New" option to combo box
        for (String code : examCodes) {
            examCodeComboBox.addItem(code);
        }
        examCodeComboBox.addItem("New"); // Add the "New" option
    }

    // Method to save question data to the database
    private void saveToDatabase(String examCode, String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        String url = "jdbc:Mysql://localhost:3306/mydb";
        String user = "root";
        String password = "kiran"; // Replace with your MySQL password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Create table if not exists
            String createTableSQL = "CREATE TABLE IF NOT EXISTS examcode (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "exam_code VARCHAR(255), " +
                    "question TEXT, " +
                    "option_a TEXT, " +
                    "option_b TEXT, " +
                    "option_c TEXT, " +
                    "option_d TEXT, " +
                    "correct_answer VARCHAR(1)" +
                    ")";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
            }

            // Insert data into the table
            String insertSQL = "INSERT INTO examcode (exam_code, question, option_a, option_b, option_c, option_d, correct_answer) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, examCode);
                pstmt.setString(2, question);
                pstmt.setString(3, optionA);
                pstmt.setString(4, optionB);
                pstmt.setString(5, optionC);
                pstmt.setString(6, optionD);
                pstmt.setString(7, correctAnswer);
                pstmt.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
