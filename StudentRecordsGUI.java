package com.app.quizizo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentRecordsGUI extends JPanel {
    private JComboBox<String> classDropdown;
    private JTable recordsTable;

    public StudentRecordsGUI(String username) {
        setLayout(null); // Use null layout for precise positioning
        setBackground(new Color(190, 187, 180)); // Set the main panel background color

        // Create main panel
        JPanel classPanel = new JPanel();
        classPanel.setLayout(null); // Use null layout for absolute positioning
        classPanel.setBackground(new Color(190, 187, 180));
        classPanel.setBounds(0, 0, 800, 800); // Set bounds for the main panel
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
        iconLabel.setBounds(350, 50, 100, 100); // Set bounds for the icon
        classPanel.add(iconLabel);

        // Title
        JLabel titleLabel = new JLabel("STUDENT RECORDS");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        titleLabel.setBounds(300, 160, 200, 30); // Set bounds for the title
        classPanel.add(titleLabel);

        // Class Selection Label
        JLabel classLabel = new JLabel("Select Class:");
        classLabel.setFont(new Font("Times New Roman", Font.BOLD, 13));
        classLabel.setForeground(Color.BLACK); // Set label color for visibility
        classLabel.setBounds(50, 210, 150, 30); // Set bounds for the label
        classPanel.add(classLabel); // Add the label to the panel

        // Create the dropdown for classes
        classDropdown = new JComboBox<>(new String[]{"R3A", "R3B", "R2A", "R2B"}); // Example classes
        classDropdown.setBounds(200, 210, 150, 30); // Set bounds for the dropdown
        classDropdown.addActionListener(e -> updateTable());
        classPanel.add(classDropdown);

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
        tablePanel.setBounds(50, 260, 700, 500); // Set bounds for the table panel
        classPanel.add(tablePanel);
    }

    private void updateTable() {
        String selectedClass = (String) classDropdown.getSelectedItem();

        // Fetch records for the selected class
        List<String[]> records = StudentRecordsFetcher.fetchStudentRecords(selectedClass);

        // Table setup
        String[] columnNames = {"Name", "Mobile", "Email","Gender",};
        String[][] data = records.toArray(new String[0][]);
        recordsTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
    }
}
