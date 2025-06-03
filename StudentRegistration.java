package com.app.quizizo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentRegistration {

    private String generatedOtp; // Store the generated OTP
    private final OTPService otpService; // Create an instance of OTPService

    public StudentRegistration(Runnable onClose) {
        otpService = new OTPService(); // Initialize the OTPService
        JFrame frame = new JFrame("ONLINE EXAMINATION SYSTEM");
        frame.setSize(1100, 800);
        frame.getContentPane().setBackground(new Color(190, 187, 180));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        // Adding Image
        ImageIcon icon = new ImageIcon("C:\\Users\\Kiran\\Desktop\\myjdbc\\java_app\\QUIZIZO\\src\\main\\java\\com\\app\\quizizo\\icon.png");
        Image scaledImage = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        ImageIcon ico = new ImageIcon(scaledImage);
        JLabel img = new JLabel(ico);
        img.setBounds(10, 10, 400, 400);
        frame.add(img);

        // Header
        JLabel head = new JLabel("STUDENT REGISTRATION FORM");
        head.setBounds(550, 10, 300, 40);
        head.setFont(new Font("Times New Roman", Font.BOLD, 13));
        frame.add(head);

        // Form Labels and Fields
        String[] labels = {"USERNAME", "FULL NAME", "GENDER", "MOBILE NUMBER", "EMAIL", "PASSWORD", "COURSE", "CLASS", "DEPARTMENT"};
        int yPosition = 100;
        for (String label : labels) {
            JLabel lbl = new JLabel(label);
            lbl.setBounds(350, yPosition, 200, 40);
            lbl.setFont(new Font("Times New Roman", Font.PLAIN, 13));
            frame.add(lbl);
            yPosition += 50;
        }

        JButton sendOtpButton = new JButton("SEND OTP");
        sendOtpButton.setBounds(800, 260, 100, 25);
        sendOtpButton.setBackground(new Color(190, 187, 180));
        sendOtpButton.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        frame.add(sendOtpButton);

        // Fields
        JTextField usernameField = new JTextField();
        usernameField.setBounds(600, 110, 200, 25);
        frame.add(usernameField);

        JTextField fullNameField = new JTextField();
        fullNameField.setBounds(600, 160, 200, 25);
        frame.add(fullNameField);

        // Gender Radio Buttons
        JRadioButton male = new JRadioButton("Male");
        male.setBounds(600, 210, 70, 25);
        male.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        male.setBackground(new Color(190, 187, 180));
        frame.add(male);

        JRadioButton female = new JRadioButton("Female");
        female.setBounds(670, 210, 80, 25);
        female.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        female.setBackground(new Color(190, 187, 180));
        frame.add(female);

        JRadioButton other = new JRadioButton("Other");
        other.setBounds(750, 210, 70, 25);
        other.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        other.setBackground(new Color(190, 187, 180));
        frame.add(other);

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(male);
        genderGroup.add(female);
        genderGroup.add(other);

        JTextField mobileField = new JTextField();
        mobileField.setBounds(600, 260, 200, 25);
        frame.add(mobileField);

        JTextField otpField = new JTextField();
        otpField.setBounds(910, 260, 100, 25);
        addPlaceholderText(otpField, "OTP");
        frame.add(otpField);

        JTextField emailField = new JTextField();
        emailField.setBounds(600, 310, 200, 25);
        frame.add(emailField);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(600, 360, 200, 25);
        frame.add(passwordField);

        JTextField courseField = new JTextField();
        courseField.setBounds(600, 410, 200, 25);
        frame.add(courseField);

        // Dropdown for Class
        String[] classes = {"R3B", "R2B", "R2A", "R3A"}; // Updated class options
        JComboBox<String> classDropdown = new JComboBox<>(classes);
        classDropdown.setBounds(600, 460, 200, 25);
        frame.add(classDropdown);

        // Dropdown for Department
        String[] departments = {"Science", "Arts", "Commerce", "Engineering"}; // Example departments
        JComboBox<String> departmentDropdown = new JComboBox<>(departments);
        departmentDropdown.setBounds(600, 510, 200, 25);
        frame.add(departmentDropdown);

        // OTP and Verification
        sendOtpButton.addActionListener(event -> {
            String phoneNumber = mobileField.getText();
            if (!phoneNumber.isEmpty() && isValidMobileNumber(phoneNumber)) {
                generatedOtp = generateOtp();
                System.out.println("Generated OTP: " + generatedOtp); // Debugging log

                boolean success = otpService.sendOtp(phoneNumber, generatedOtp);
                JOptionPane.showMessageDialog(frame, success ? "OTP sent successfully!" : "Failed to send OTP.");
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a valid phone number.");
            }
        });



        JButton verifyOtpButton = new JButton("VERIFY OTP");
        verifyOtpButton.setBounds(800, 310, 150, 25);
        verifyOtpButton.setBackground(new Color(190, 187, 180));
        verifyOtpButton.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        frame.add(verifyOtpButton);

        // Confirmation Checkbox
        Checkbox c = new Checkbox("All the details given are correct.");
        c.setBounds(500, 560, 500, 30);
        c.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        frame.add(c);

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(650, 600, 100, 30);
        submitButton.setBackground(new Color(76, 175, 80));
        submitButton.setBorder(null);
        submitButton.setEnabled(false);
        frame.add(submitButton);

        // Database Submission
        submitButton.addActionListener(e -> {
            String username = usernameField.getText();
            String fullName = fullNameField.getText();
            String gender = male.isSelected() ? "Male" : female.isSelected() ? "Female" : "Other";
            String mobileNumber = mobileField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String course = courseField.getText();
            String selectedClass = (String) classDropdown.getSelectedItem();
            String selectedDepartment = (String) departmentDropdown.getSelectedItem();

            if (checkUsernameExists(username)) {
                JOptionPane.showMessageDialog(frame, "Username already exists. Please choose a different username.");
            } else if (!isValidMobileNumber(mobileNumber)) {
                JOptionPane.showMessageDialog(frame, "Mobile number must be exactly 10 digits.");
            } else if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(frame, "Email must end with @gmail.com, @tkmce.ac.in, or @outlook.com");
            } else {
                saveToDatabase(username, fullName, gender, mobileNumber, email, password, course, selectedClass, selectedDepartment);
                JOptionPane.showMessageDialog(frame, "Student registration successful!");
            }
        });




// Verification button logic
        verifyOtpButton.addActionListener(e -> {
            String userInputOtp = otpField.getText();

            if (otpService.verifyOtp(mobileField.getText(), userInputOtp)) {
                JOptionPane.showMessageDialog(frame, "OTP verified successfully!");
                submitButton.setEnabled(true);

                // Enable submit button only if all fields are valid
                if (allFieldsValid(usernameField, fullNameField, mobileField, emailField, passwordField, courseField, genderGroup)) {
                    submitButton.setEnabled(true);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid OTP. Please try again.");
            }
        });


// Finalize and show the frame
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }

    private String generateOtp() {
        int otp = (int) (Math.random() * 9000) + 1000; // Generates a 4-digit OTP
        return String.valueOf(otp);
    }

    private boolean isValidMobileNumber(String mobile) {
        return mobile.matches("\\d{10}"); // Validates that the number is 10 digits
    }

    private boolean isValidEmail(String email) {
        return email.endsWith("@gmail.com") || email.endsWith("@tkmce.ac.in") || email.endsWith("@outlook.com");
    }

    private void saveToDatabase(String username, String fullName, String gender, String mobileNumber,
                                String email, String password, String course, String selectedClass,
                                String selectedDepartment) {

        // Define SQL to create student_records table if it doesn’t exist
        String createStudentTableSQL = "CREATE TABLE IF NOT EXISTS student_records (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL UNIQUE, " +
                "full_name VARCHAR(100) NOT NULL, " +
                "gender VARCHAR(10) NOT NULL, " +
                "mobile_number VARCHAR(15) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "password VARCHAR(100) NOT NULL, " +
                "course VARCHAR(100) NOT NULL, " +
                "class VARCHAR(50) NOT NULL, " +
                "department VARCHAR(50) NOT NULL" +
                ")";

        // SQL to insert data into student_records table
        String insertStudentSQL = "INSERT INTO student_records (username, full_name, gender, mobile_number, email, password, course, class, department) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Create a new table specific to the selected class for record tracking
        String createClassTableSQL = "CREATE TABLE IF NOT EXISTS " + selectedClass + "_record (" +
                "full_name VARCHAR(100) NOT NULL, " +
                "mobile_number VARCHAR(15) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "gender VARCHAR(10) NOT NULL, " +
                "PRIMARY KEY (email)" +
                ")";

        // SQL to insert data into the specific class table
        String insertClassSQL = "INSERT INTO " + selectedClass + "_record (full_name, mobile_number, email, gender) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "kiran");
             Statement stmt = conn.createStatement()) {

            // Step 1: Create the student_records table if it doesn’t exist
            stmt.execute(createStudentTableSQL);

            // Step 2: Insert data into student_records table
            try (PreparedStatement pstmtStudent = conn.prepareStatement(insertStudentSQL)) {
                pstmtStudent.setString(1, username);
                pstmtStudent.setString(2, fullName);
                pstmtStudent.setString(3, gender);
                pstmtStudent.setString(4, mobileNumber);
                pstmtStudent.setString(5, email);
                pstmtStudent.setString(6, password);
                pstmtStudent.setString(7, course);
                pstmtStudent.setString(8, selectedClass);
                pstmtStudent.setString(9, selectedDepartment);
                pstmtStudent.executeUpdate();
            }

            // Step 3: Create the class-specific table if it doesn’t exist
            stmt.execute(createClassTableSQL);

            // Step 4: Insert data into the class-specific table
            try (PreparedStatement pstmtClass = conn.prepareStatement(insertClassSQL)) {
                pstmtClass.setString(1, fullName);
                pstmtClass.setString(2, mobileNumber);
                pstmtClass.setString(3, email);
                pstmtClass.setString(4, gender);
                pstmtClass.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Data saved successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error while saving to the database. Please try again.");
        }
    }



    private boolean checkUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM student_records WHERE username = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "kiran"); // Corrected password
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean allFieldsValid(JTextField usernameField, JTextField fullNameField, JTextField mobileField,
                                   JTextField emailField, JPasswordField passwordField, JTextField courseField,
                                   ButtonGroup genderGroup) {
        return !usernameField.getText().isEmpty() &&
                !fullNameField.getText().isEmpty() &&
                !mobileField.getText().isEmpty() && isValidMobileNumber(mobileField.getText()) &&
                !emailField.getText().isEmpty() && isValidEmail(emailField.getText()) &&
                passwordField.getPassword().length > 0 &&
                !courseField.getText().isEmpty() &&
                (genderGroup.getSelection() != null);// Check if any gender option is selected
    }

    private void addPlaceholderText(JTextField textField, String placeholder) {
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }
}
