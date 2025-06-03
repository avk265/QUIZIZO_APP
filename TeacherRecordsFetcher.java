package com.app.quizizo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherRecordsFetcher {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb"; // Update with your DB URL
    private static final String USER = "root"; // Update with your DB username
    private static final String PASSWORD = "kiran"; // Update with your DB password

    public static List<String[]> fetchTeacherRecords(String department) {
        List<String[]> records = new ArrayList<>();

        String query = "SELECT full_name, mobile_number, email, gender FROM teacher_records WHERE LOWER(department) = ?"; // Ensure department matches lowercase

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, department); // Set the department parameter
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String full_name = rs.getString("full_name");
                String mobile_number = rs.getString("mobile_number");
                String email = rs.getString("email");
                String gender = rs.getString("gender");

                records.add(new String[]{full_name, mobile_number, email, gender});
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions properly in production code
        }

        return records;
    }
}
