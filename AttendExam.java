package com.app.quizizo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class AttendExam extends JFrame {
    private JComboBox<String> examCodeComboBox;
    public JPanel questionPanel;
    private JLabel timerLabel;
    private JButton startExamButton;
    private int totalTime = 300; // Total exam time in seconds (5 minutes)
    private Timer timer;
    public String username;
    public AttendExam(String user) {
        username=user;
        setTitle("Attend Exam");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initializeGUI();
        fetchExamCodes(); // Fetch exam codes from the database
        setVisible(true); // Make the frame visible
    }

    private void initializeGUI() {
        setBackground(new Color(190, 187, 180));
        setLayout(new BorderLayout());

        // Timer label
        timerLabel = new JLabel("Time left: " + formatTime(totalTime), SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(timerLabel, BorderLayout.NORTH);

        // Create a panel for exam selection
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(null);

        // Label for exam code selection
        JLabel selectionLabel = new JLabel("SELECT EXAM CODE");
        selectionLabel.setBounds(100, 200, 150, 30);
        selectionPanel.add(selectionLabel);

        // ComboBox for exam codes
        examCodeComboBox = new JComboBox<>();
        examCodeComboBox.setBounds(300, 200, 200, 30);
        selectionPanel.add(examCodeComboBox);

        // Button to start the exam
        startExamButton = new JButton("Start Exam");
        startExamButton.setBounds(250, 250, 200, 30);
        startExamButton.addActionListener(e -> startExam());
        selectionPanel.add(startExamButton);

        // Add selection panel to the center of the frame
        add(selectionPanel, BorderLayout.CENTER);
    }

    private List<String> fetchExamCodes() {
        List<String> examCodes = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "kiran")) {

            // Get all table names that start with "new_exam"
            ResultSet rsTables = conn.getMetaData().getTables(null, null, "new_exam_%", null);

            while (rsTables.next()) {
                String tableName = rsTables.getString("TABLE_NAME");

                // Extract the exam code from the table name
                if (tableName.startsWith("new_exam_")) {
                    String examCode = tableName.substring("new_exam_".length()); // Get part after "new_exam_"
                    examCodes.add(examCode); // Add exam code to the list
                }
            }

            rsTables.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return examCodes;
    }


    private void startExam() {
        String selectedExamCode = (String) examCodeComboBox.getSelectedItem();
        if (selectedExamCode != null) {
            startTimer();
            // Open the panel to display the exam
            openExamPanel(selectedExamCode);
        }
    }

    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (totalTime > 0) {
                    totalTime--;
                    timerLabel.setText("Time left: " + formatTime(totalTime));
                } else {
                    timer.cancel();
                    submitExam(); // Auto-submit when time is up
                }
            }
        }, 1000, 1000); // Update every second
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void openExamPanel(String examCode) {
        // Create a new JPanel for the questions and exam code
        JPanel examPanel = new JPanel();
        examPanel.setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Add heading with Exam Code
        JLabel headingLabel = new JLabel("EXAM CODE: " + examCode, SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 24));
        examPanel.add(headingLabel, BorderLayout.NORTH);

        // Panel to hold the questions and options
        questionPanel = new JPanel();
        questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

        // Fetch questions for the selected exam code
        List<Question> questions = getQuestionsFromDatabase(examCode);

        for (Question question : questions) {
            int x=100,y=100;
            JPanel questionContainer = new JPanel();
            questionContainer.setLayout(new BoxLayout(questionContainer, BoxLayout.Y_AXIS));

            // Question label
            JLabel questionLabel = new JLabel(question.questionText);
            questionLabel.setFont(new Font("Arial", Font.BOLD, 13));
            questionLabel.setBounds(x,y,500,30);
            questionContainer.add(questionLabel);


            // Radio buttons for options
            ButtonGroup group = new ButtonGroup();
            JRadioButton optionA = new JRadioButton("A. " + question.optionA);
            optionA.setFont(new Font("Arial", Font.BOLD, 13));
            optionA.setBounds(x+10,y+10,100,30);
            JRadioButton optionB = new JRadioButton("B. " + question.optionB);
            optionB.setBounds(x+10,y+20,100,30);
            JRadioButton optionC = new JRadioButton("C. " + question.optionC);
            optionC.setBounds(x+10,y+30,100,30);
            JRadioButton optionD = new JRadioButton("D. " + question.optionD);
            optionD.setBounds(x+10,y+40,100,30);
            group.add(optionA);
            group.add(optionB);
            group.add(optionC);
            group.add(optionD);

            // Add options to the question container
            questionContainer.add(optionA);
            questionContainer.add(optionB);
            questionContainer.add(optionC);
            questionContainer.add(optionD);

            // Add the question container to the main panel
            questionPanel.add(questionContainer);
            y+=15;
        }
        JButton submit=new JButton("Submit");
        submit.setFont(new Font("Arial", Font.BOLD, 13));
        submit.setBounds(350,750,100,30);
        submit.addActionListener(e -> submitExam());
        questionPanel.add(submit);

        // Add the question panel to the center of the exam panel
        examPanel.add(questionPanel, BorderLayout.CENTER);

        // Add the exam panel to the frame (replaces selection panel)
        getContentPane().removeAll();  // Remove the previous selection panel
        add(examPanel, BorderLayout.CENTER);
        examPanel.add(questionPanel,BoxLayout.X_AXIS);
        revalidate();
        repaint();
    }

    private List<Question> getQuestionsFromDatabase(String examCode) {
        List<Question> questions = new ArrayList<>();
        // Construct the table name based on the exam code

        // SQL query to select questions and options based on the provided exam code
        String query = "SELECT question, option_a, option_b, option_c, option_d FROM examcode where exam_code='" + examCode + "'";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "kiran");
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Execute the query
            ResultSet rs = pstmt.executeQuery();

            // Iterate over the result set and create Question objects
            while (rs.next()) {
                String questionText = rs.getString("question");
                String optionA = rs.getString("option_a");
                String optionB = rs.getString("option_b");
                String optionC = rs.getString("option_c");
                String optionD = rs.getString("option_d");
                // Here, we're assuming the correct answer will be used elsewhere

                // Create a Question object and add it to the list
                questions.add(new Question(questionText, optionA, optionB, optionC, optionD));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return questions; // Return the list of questions
    }


    private void submitExam() {
        timer.cancel(); // Stop the timer
        int score = calculateScore();
        saveScoreToDatabase(score); // Save the score to the database
        displayResult(score);
    }
    private void saveScoreToDatabase(int score) {
        String selectedExamCode = (String) examCodeComboBox.getSelectedItem();


        // Query to get the full name from student_records based on the username
        String getFullNameQuery = "SELECT full_name FROM student_records WHERE username = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "kiran")) {
            // First, retrieve the full name
            String fullName = null;
            String ex="new_exam_"+selectedExamCode;
            try (PreparedStatement pstmt1 = conn.prepareStatement(getFullNameQuery)) {
                pstmt1.setString(1, username); // Set the username parameter

                ResultSet rs = pstmt1.executeQuery();
                if (rs.next()) {
                    fullName = rs.getString("full_name"); // Get the full name from the result set
                }
            }

            if (fullName != null) {
                // Now, insert the score into the new_exam_exam_code table using the full name
                String insertQuery = "INSERT INTO "+ex+" (username, exam_code, score) VALUES (?, ?, ?)";

                try (PreparedStatement pstmt2 = conn.prepareStatement(insertQuery)) {
                    pstmt2.setString(1, fullName); // Use full name instead of username
                    pstmt2.setString(2, selectedExamCode); // Set the selected exam code
                    pstmt2.setInt(3, score); // Set the score

                    pstmt2.executeUpdate(); // Execute the insert
                }
            } else {
                System.out.println("Full name not found for username: " + username);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    private int calculateScore() {
        int score = 0;


        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "kiran")) {
            String query = "SELECT correct_answer FROM examcode WHERE question = ?";

            for (Component component : questionPanel.getComponents()) {
                if (component instanceof JPanel) {
                    JPanel questionContainer = (JPanel) component;
                    JLabel questionLabel = (JLabel) questionContainer.getComponent(0);
                    String questionText = questionLabel.getText();

                    ButtonGroup group = new ButtonGroup();
                    String selectedAnswer = null;

                    // Retrieve each option and check if selected
                    for (int i = 1; i < questionContainer.getComponentCount(); i++) {
                        if (questionContainer.getComponent(i) instanceof JRadioButton) {
                            JRadioButton radioButton = (JRadioButton) questionContainer.getComponent(i);
                            group.add(radioButton);

                            if (radioButton.isSelected()) {
                                selectedAnswer = radioButton.getText().substring(0, 1); // Extract option letter
                            }
                        }
                    }

                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, questionText);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next() && selectedAnswer != null) {
                            String correctAnswer = rs.getString("correct_answer").trim();
                            if (selectedAnswer.equalsIgnoreCase(correctAnswer)) {
                                score++;
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return score;
    }

    private void displayResult(int score) {

        JFrame resultFrame = new JFrame("Exam Results");
        resultFrame.setSize(800, 600);
        JLabel resultLabel = new JLabel("Your Score: " + score, SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 36));
        resultFrame.add(resultLabel);
        resultFrame.setVisible(true);
    }


    // Question class to store question details
    public static class Question {
        String questionText;
        String optionA;
        String optionB;
        String optionC;
        String optionD;

        public Question(String questionText, String optionA, String optionB, String optionC, String optionD) {
            this.questionText = questionText;
            this.optionA = optionA;
            this.optionB = optionB;
            this.optionC = optionC;
            this.optionD = optionD;
        }
    }
}
