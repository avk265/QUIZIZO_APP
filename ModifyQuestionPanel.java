package com.app.quizizo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModifyQuestionPanel extends JPanel {
    private JComboBox<String> examCodeSearchComboBox;
    private JButton qnPaperSearchButton;

    public ModifyQuestionPanel(JFrame frame, String username) {
        setLayout(null);
        setBackground(new Color(190, 187, 180));

        // Label and ComboBox for selecting exam code
        JLabel searchLabel = new JLabel("Select Exam Code:");
        searchLabel.setBounds(50, 50, 150, 30);
        add(searchLabel);

        examCodeSearchComboBox = new JComboBox<>();
        examCodeSearchComboBox.setBounds(200, 20, 150, 30);
        loadExistingExamCodes(examCodeSearchComboBox); // Load existing exam codes for searching
        add(examCodeSearchComboBox);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(370, 50, 100, 30);
        add(searchButton);

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
        // Table for displaying questions
        String[] columnNames = {"ID", "Exam Code", "Question", "Option A", "Option B", "Option C", "Option D", "Correct Answer"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable questionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(questionTable);
        scrollPane.setBounds(50, 90, 700, 400);
        add(scrollPane);

        JButton modifyButton = new JButton("Modify");
        modifyButton.setBounds(50, 490, 100, 30);
        add(modifyButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(160, 490, 100, 30);
        add(deleteButton);

        // Action Listener for Search Button
        searchButton.addActionListener(e -> {
            String selectedExamCode = (String) examCodeSearchComboBox.getSelectedItem();
            loadQuestionsForExamCode(selectedExamCode, tableModel);
        });

        // Action Listener for Modify Button
        modifyButton.addActionListener(e -> {
            int selectedRow = questionTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String question = (String) tableModel.getValueAt(selectedRow, 2);
                String optionA = (String) tableModel.getValueAt(selectedRow, 3);
                String optionB = (String) tableModel.getValueAt(selectedRow, 4);
                String optionC = (String) tableModel.getValueAt(selectedRow, 5);
                String optionD = (String) tableModel.getValueAt(selectedRow, 6);
                String correctAnswer = (String) tableModel.getValueAt(selectedRow, 7);

                // Create a new dialog for modifying the question
                JTextField questionField = new JTextField(question);
                JTextField optionAField = new JTextField(optionA);
                JTextField optionBField = new JTextField(optionB);
                JTextField optionCField = new JTextField(optionC);
                JTextField optionDField = new JTextField(optionD);
                JComboBox<String> correctAnswerComboBox = new JComboBox<>(new String[]{"A", "B", "C", "D"});
                correctAnswerComboBox.setSelectedItem(correctAnswer);

                Object[] message = {
                        "Question:", questionField,
                        "Option A:", optionAField,
                        "Option B:", optionBField,
                        "Option C:", optionCField,
                        "Option D:", optionDField,
                        "Correct Answer:", correctAnswerComboBox
                };

                int option = JOptionPane.showConfirmDialog(frame, message, "Modify Question", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    modifyQuestionInDatabase(id, questionField.getText(), optionAField.getText(),
                            optionBField.getText(), optionCField.getText(), optionDField.getText(),
                            (String) correctAnswerComboBox.getSelectedItem());
                    loadQuestionsForExamCode((String) examCodeSearchComboBox.getSelectedItem(), tableModel); // Refresh table
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a question to modify.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action Listener for Delete Button
        deleteButton.addActionListener(e -> {
            int selectedRow = questionTable.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this question?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deleteQuestionFromDatabase(id);
                    loadQuestionsForExamCode((String) examCodeSearchComboBox.getSelectedItem(), tableModel); // Refresh table
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a question to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Method to load existing exam codes into a ComboBox
    private void loadExistingExamCodes(JComboBox<String> comboBox) {
        comboBox.removeAllItems(); // Clear existing items
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
            JOptionPane.showMessageDialog(this, "Error connecting to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        // Add existing codes
        for (String code : examCodes) {
            comboBox.addItem(code);
        }
        comboBox.addItem("New");
    }

    // Method to load questions for a selected exam code into the table
    private void loadQuestionsForExamCode(String examCode, DefaultTableModel tableModel) {
        String url = "jdbc:Mysql://localhost:3306/mydb";
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

    // Method to modify a question in the database
    private void modifyQuestionInDatabase(int id, String question, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        String url = "jdbc:mysql://localhost:3306/mydb";
        String user = "root";
        String password = "kiran"; // Replace with your MySQL password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String updateSQL = "UPDATE examcode SET question = ?, option_a = ?, option_b = ?, option_c = ?, option_d = ?, correct_answer = ? WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                pstmt.setString(1, question);
                pstmt.setString(2, optionA);
                pstmt.setString(3, optionB);
                pstmt.setString(4, optionC);
                pstmt.setString(5, optionD);
                pstmt.setString(6, correctAnswer);
                pstmt.setInt(7, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error modifying question in the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to delete a question from the database
    private void deleteQuestionFromDatabase(int id) {
        String url = "jdbc:mysql://localhost:3306/mydb";
        String user = "root";
        String password = "kiran"; // Replace with your MySQL password

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String deleteSQL = "DELETE FROM examcode WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting question from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
