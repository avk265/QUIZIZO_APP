package com.app.quizizo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TeacherRecordsGUI extends JPanel {
    public JTable recordsTable;
    public JComboBox<String> departmentDropdown; // Use JComboBox instead of ComboBox

    public TeacherRecordsGUI(String username) {
        setLayout(null);
        setBackground(new Color(255, 255, 255));

        // Create main panel
        JPanel classPanel = new JPanel();
        classPanel.setLayout(null);
        classPanel.setBackground(new Color(190, 187, 180));
        classPanel.setBounds(0, 0, 800, 800);
        add(classPanel);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 10, 80, 30); // Position it in the top left corner
        backButton.setBackground(new Color(190, 187, 180));
        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.getContentPane().removeAll(); // Clear current content
            frame.getContentPane().add(new Advisordash(username)); // Go back to Advisordash
            frame.revalidate();
            frame.repaint();
        });
        classPanel.add(backButton);

        // Icon
        ImageIcon icon = new ImageIcon("C:\\Users\\Kiran\\Desktop\\myjdbc\\java_app\\QUIZIZO\\src\\main\\java\\com\\app\\quizizo\\icon.png");
        Image scaledIcon = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledIcon));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabel.setBounds(350, 10, 100, 100); // Set bounds for the icon
        classPanel.add(iconLabel);

        // Title
        JLabel titleLabel = new JLabel("TEACHER RECORDS");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBounds(300, 120, 300, 30); // Set bounds for the title
        classPanel.add(titleLabel);

        // Department Selection Label
        JLabel departmentLabel = new JLabel("Select Department:");
        departmentLabel.setFont(new Font("Times New Roman", Font.BOLD, 13));
        departmentLabel.setForeground(Color.BLACK); // Set label color for visibility
        departmentLabel.setBounds(50, 170, 150, 30); // Set bounds for the label
        classPanel.add(departmentLabel); // Add the label to the panel

        // Create the dropdown for departments
        departmentDropdown = new JComboBox<>(new String[]{"CSE", "EEE", "ECE", "MECH", "CE"}); // Example departments
        departmentDropdown.setBounds(200, 170, 150, 30); // Set bounds for the dropdown
        departmentDropdown.addActionListener(e -> updateTable());
        classPanel.add(departmentDropdown);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(190, 187, 180)); // Set the table panel background color
        recordsTable = new JTable();
        recordsTable.setBackground(new Color(190, 187, 180)); // Set the table background color
        recordsTable.setForeground(Color.BLACK); // Set table text color for visibility
        recordsTable.setDefaultEditor(Object.class, null); // Disable editing in the table

        JScrollPane scrollPane = new JScrollPane(recordsTable);
        scrollPane.setBackground(new Color(190, 187, 180)); // Set scroll pane background color
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Add the table panel to the main panel
        tablePanel.setBounds(50, 220, 700, 500); // Set bounds for the table panel
        classPanel.add(tablePanel);
    }

    private void updateTable() {
        String selectedDepartment = (String) departmentDropdown.getSelectedItem();

        // Fetch records for the selected department
        List<String[]> records = TeacherRecordsFetcher.fetchTeacherRecords(selectedDepartment.toLowerCase()); // Fetch records

        // Table setup
        String[] columnNames = {"Name", "Mobile Number", "Email", "Gender"};
        String[][] data = records.toArray(new String[0][]);
        recordsTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
}
