package com.app.quizizo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.*;

import static com.sun.java.accessibility.util.AWTEventMonitor.addActionListener;
import static java.sql.DriverManager.getConnection;

public class Login extends JPanel {

    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JComboBox<String> roleComboBox;
    private final JLabel messageLabel;
    private final JPanel mainPanel; // Container for main GUI components

    private boolean isStudentRegistrationOpen = false;
    private boolean isTeacherRegistrationOpen = false;
    private boolean isAdvisorRegistrationOpen = false;

    public Login(JFrame frame) {
        setLayout(null);
        setBackground(new Color(255, 255, 255));

        // Gradient background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(35, 95, 115);
                Color color2 = new Color(175, 25, 235);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBounds(0, 0, 800, 800); // Updated size
        mainPanel.setLayout(null);
        add(mainPanel);


        // Title Label
        JLabel titleLabel = new JLabel("MEMBER LOGIN", SwingConstants.CENTER);
        titleLabel.setBounds(300, 50, 200, 40); // Centered position
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(Color.BLACK);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        mainPanel.add(titleLabel);

        ImageIcon icon = new ImageIcon("C:\\Users\\Kiran\\Desktop\\myjdbc\\java_app\\QUIZIZO\\src\\main\\java\\com\\app\\quizizo\\bgr.png");
        Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); // Adjust size as needed
        JLabel imgLabel = new JLabel(new ImageIcon(scaledImage));
        imgLabel.setBounds(50, 100, 150, 150); // Adjust position as needed
        mainPanel.add(imgLabel);

        // Role selection Label and ComboBox
        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setBounds(300, 120, 200, 20); // Centered position
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(roleLabel);

        roleComboBox = new JComboBox<>(new String[]{" ", "Student", "Teacher", "Advisor"});
        roleComboBox.setBounds(300, 150, 200, 30); // Centered position
        roleComboBox.setOpaque(false);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(roleComboBox);

        JLabel name = new JLabel("Username:");
        name.setForeground(Color.WHITE);
        name.setBounds(300, 200, 200, 20); // Centered position
        name.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(name);

        JLabel password = new JLabel("Password:");
        password.setForeground(Color.WHITE);
        password.setBounds(300, 280, 200, 20); // Centered position
        password.setFont(new Font("Arial", Font.PLAIN, 14));
        mainPanel.add(password);

        // Username and password fields
        usernameField = createTextField("enter your username", 300, 230); // Centered position
        passwordField = new JPasswordField();
        setupPasswordField(passwordField, "Password", 300, 310); // Centered position

        // Login and Register Buttons
        JButton loginButton = createButton("LOGIN", 300, 380, 200, 40); // Centered position
        JButton registerButton = createButton("REGISTER", 300, 480, 200, 40); // Centered position

        // Message label
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setBounds(250, 550, 300, 30); // Centered position
        messageLabel.setForeground(Color.RED);
        mainPanel.add(messageLabel);

        // Add action listeners
        loginButton.addActionListener(e -> {
            handleLogin(frame);
        });
        registerButton.addActionListener(e -> handleRegister());

        // Add components
        mainPanel.add(loginButton);
        mainPanel.add(registerButton);

        // Optional forgot password link
        JLabel forgotPasswordLabel = createLinkLabel("Forgot Password?", 300, 430); // Centered position
        forgotPasswordLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    handlePasswordReset();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        mainPanel.add(forgotPasswordLabel);
    }


    private JTextField createTextField(String placeholder, int x, int y) {
        JTextField field = new JTextField(placeholder);
        field.setBounds(x, y, 200, 30);
        field.setForeground(Color.GRAY);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        addPlaceholderText(field, placeholder);
        mainPanel.add(field);
        return field;
    }

    private void setupPasswordField(JPasswordField field, String placeholder, int x, int y) {
        field.setBounds(x, y, 200, 30);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        field.setBackground(new Color(240, 248, 255));
        mainPanel.add(field);
    }

    private JLabel createLinkLabel(String text, int x, int y) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setBounds(x, y, 200, 30);
        label.setForeground(Color.WHITE);
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return label;
    }

    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(34, 139, 34));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }


    private void addPlaceholderText(JTextField field, String placeholder) {
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
    }
    private void handleLogin(JFrame frame) {
        try {
            String selectedRole = (String) roleComboBox.getSelectedItem();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Username and password cannot be empty.");
                return;
            }

            if (checkUsernameExists(username, selectedRole)) {
                if (validateCredentials(username, password, selectedRole)) {
                    if (selectedRole.equals("Advisor")) {
                        frame.getContentPane().removeAll();
                        frame.getContentPane().add(new Advisordash(username));
                        frame.revalidate();
                        frame.repaint();
                    }
                    else if (selectedRole.equals("Teacher")) {
                        frame.getContentPane().removeAll();
                        frame.getContentPane().add(new Teacherdash(username,frame));
                        frame.revalidate();
                        frame.repaint();
                    }
                    else{
                        frame.getContentPane().removeAll();
                        frame.getContentPane().add(new Studentdash(username));
                        frame.revalidate();
                        frame.repaint();
                    }
                } else {
                    messageLabel.setText("Invalid credentials. Please try again.");
                }
            } else {
                messageLabel.setText("Username not found.");
            }
        } catch (ClassNotFoundException e) {
            messageLabel.setText("Database driver not found.");
            e.printStackTrace();
        } catch (Exception e) {
            messageLabel.setText("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void handlePasswordReset() throws ClassNotFoundException {
        String username = JOptionPane.showInputDialog(mainPanel, "Enter your username:");

        if (username == null || username.trim().isEmpty()) {
            return; // user cancelled or did not input username
        }

        String selectedRole = (String) roleComboBox.getSelectedItem();

        if (checkUsernameExists(username, selectedRole)) {
            String newPassword = JOptionPane.showInputDialog(mainPanel, "Enter your new password:");

            if (newPassword != null && !newPassword.trim().isEmpty()) {
                if (resetPassword(username, newPassword, selectedRole)) {
                    JOptionPane.showMessageDialog(mainPanel, "Password reset successful!");
                } else {
                    messageLabel.setText("Error resetting password. Please try again.");
                }
            }
        } else {
            messageLabel.setText("Username not found.");
        }
    }

    // Register Button Action: Open respective registration mainPanels
    private void handleRegister() {
        String selectedRole = (String) roleComboBox.getSelectedItem();

        if (selectedRole.equals("Student")) {
            if (!isStudentRegistrationOpen) {
                isStudentRegistrationOpen = true;  // Mark as open
                new StudentRegistration(() -> isStudentRegistrationOpen = false);  // Pass a callback to reset the flag
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Student Registration form is already open.");
            }

        } else if (selectedRole.equals("Teacher")) {
            if (!isTeacherRegistrationOpen) {
                isTeacherRegistrationOpen = true;  // Mark as open
                new TeacherRegistration(() -> isTeacherRegistrationOpen = false);  // Pass a callback to reset the flag
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Teacher Registration form is already open.");
            }

        } else if (selectedRole.equals("Advisor")) {
            if (!isAdvisorRegistrationOpen) {
                isAdvisorRegistrationOpen = true;  // Mark as open
                new Registration(() -> isAdvisorRegistrationOpen = false);  // Pass a callback to reset the flag
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Advisor Registration form is already open.");
            }
        }
    }


    private boolean checkUsernameExists(String username, String role) throws ClassNotFoundException {
        String url = "jdbc:Mysql://localhost:3306/mydb";
        String dbUsername = "root";
        String dbPassword = "kiran";


        String query = "SELECT COUNT(*) FROM " + role.toLowerCase() + "_records WHERE username = ?";


        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean resetPassword(String username, String newPassword, String role) throws ClassNotFoundException {
        String url = "jdbc:mysql://localhost:3306/mydb"; // Update with your database URL
        String user = "root"; // Update with your database username
        String pass = "kiran"; // Update with your database password

        String query = "UPDATE " + role.toLowerCase() + "_records SET password = ? WHERE username = ?";
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = getConnection(url, user, pass);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean validateCredentials(String username, String password, String role) {
        String url = "jdbc:mysql://localhost:3306/mydb";
        String dbUsername = "root";
        String dbPassword = "kiran";
        String query = "SELECT COUNT(*) FROM " + role.toLowerCase() + "_records WHERE username = ? AND password = ?";

        try (Connection conn = getConnection(url, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
