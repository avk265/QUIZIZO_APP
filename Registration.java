package com.app.quizizo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Registration {

    public Registration(Runnable onClose) {
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
        JLabel head = new JLabel("ADVISOR REGISTRATION FORM");
        head.setBounds(550, 10, 300, 40);
        head.setFont(new Font("Times New Roman", Font.BOLD, 13));
        frame.add(head);

        // Form Labels and Fields
        String[] labels = {"USERNAME", "FULL NAME", "GENDER", "MOBILE NUMBER", "EMAIL", "PASSWORD", "DESIGNATION"};
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

        JTextField designationField = new JTextField();
        designationField.setBounds(600, 410, 200, 25);
        frame.add(designationField);

        // OTP and Verification
        sendOtpButton.addActionListener(event -> {
            String phoneNumber = mobileField.getText();
            if (!phoneNumber.isEmpty()) {
                System.out.println("Sending OTP to: " + phoneNumber);
                String generatedOtp = generateOtp();
                
                boolean success = OTPService.sendOtp(phoneNumber, generatedOtp);
                JOptionPane.showMessageDialog(frame, success ? "OTP sent successfully!" : "Failed to send OTP.");
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a phone number.");
            }
        });

        JButton verifyOtpButton = new JButton("VERIFY OTP");
        verifyOtpButton.setBounds(800, 300, 150, 25);
        verifyOtpButton.setBackground(new Color(190, 187, 180));
        verifyOtpButton.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        frame.add(verifyOtpButton);


        // Confirmation Checkbox
        Checkbox c = new Checkbox("All the details given are correct.");
        c.setBounds(500, 480, 500, 30);
        c.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        frame.add(c);

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(650, 510, 100, 30);
        submitButton.setBackground(new Color(76, 175, 80));
        submitButton.setBorder(null);
        submitButton.setEnabled(false);
        frame.add(submitButton);


        verifyOtpButton.addActionListener(e -> {
            String phoneNumber = mobileField.getText(); // Get the mobile number
            String userInputOtp = otpField.getText(); // Get the OTP entered by the user

            // Check if OTP is verified
            if (OTPService.verifyOtp(phoneNumber, userInputOtp)) {
                JOptionPane.showMessageDialog(frame, "OTP verified successfully!");
                submitButton.setEnabled(true); // Enable the submit button after successful verification
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid OTP. Please try again.");
            }
        });

        // Database Submission
        submitButton.addActionListener(e -> {
            String username = usernameField.getText();
            String fullName = fullNameField.getText();
            String gender = male.isSelected() ? "Male" : female.isSelected() ? "Female" : "Other";
            String mobileNumber = mobileField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String designation = designationField.getText();

            if (checkUsernameExists(username)) {
                JOptionPane.showMessageDialog(frame, "Username already exists. Please choose a different username.");
            } else if (!isValidMobileNumber(mobileNumber)) {
                JOptionPane.showMessageDialog(frame, "Mobile number must be exactly 10 digits.");
            } else if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(frame, "Email must end with @gmail.com, @tkmce.ac.in, or @outlook.com");
            } else {
                try {
                    saveToDatabase(username, fullName, gender, mobileNumber, email, password, designation);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
            submitButton.setEnabled(true);
        });

        frame.setVisible(true);
    }
    private String generateOtp() {
        int otp = (int) (Math.random() * 900000) + 100000; // Generate a random 6-digit OTP
        return String.valueOf(otp);
    }
    private void saveToDatabase(String username, String fullName, String gender, String mobileNumber, String email, String password, String designation) throws ClassNotFoundException {
        String url = "jdbc:Mysql://localhost:3306/mydb";
        String user = "root";
        String pass = "kiran";


        try (Connection con = DriverManager.getConnection(url, user, pass)) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS advisor_records (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE, " +
                    "full_name VARCHAR(100), " +
                    "gender VARCHAR(10), " +
                    "mobile_number VARCHAR(10), " +
                    "email VARCHAR(100), " +
                    "password VARCHAR(100), " +
                    "designation VARCHAR(100))";
            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(createTableQuery);
            }

            String insertQuery = "INSERT INTO advisor_records (username, full_name, gender, mobile_number, email, password, designation) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
                pstmt.setString(1, username);
                pstmt.setString(2, fullName);
                pstmt.setString(3, gender);
                pstmt.setString(4, mobileNumber);
                pstmt.setString(5, email);
                pstmt.setString(6, password);
                pstmt.setString(7, designation);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Registration Successful!");
            }
        }catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }

    }
    private boolean checkUsernameExists(String username) {
        String url = "jdbc:Mysql://localhost:3306/mydb";
        String user = "root";
        String pass = "kiran";
        String query = "SELECT COUNT(*) FROM advisor_records WHERE username = ?";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
        return false;
    }

    private boolean isValidMobileNumber(String mobileNumber) {
        return mobileNumber.matches("\\d{10}");
    }

    private boolean isValidEmail(String email) {
        return email.endsWith("@gmail.com") || email.endsWith("@tkmce.ac.in") || email.endsWith("@outlook.com");
    }

    private void addPlaceholderText(JTextField textField, String placeholder) {
        textField.setForeground(Color.GRAY);
        textField.setText(placeholder);
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setForeground(Color.BLACK);
                    textField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });
    }
}