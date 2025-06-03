package com.app.quizizo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRecordsFetcher {

    public static List<String[]> fetchStudentRecords(String className) {
        List<String[]> records = new ArrayList<>();
        String url = "jdbc:mysql://localhost:3306/mydb";
        String user = "root";
        String password = "kiran";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Query the dynamically named table based on className
            String query = "SELECT full_name, mobile_number, email, gender FROM " + className + "_record ORDER BY full_name";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String fullName = rs.getString("full_name");
                String mobileNumber = rs.getString("mobile_number");
                String email = rs.getString("email");
                String gender = rs.getString("gender");

                // Add each record to the list
                records.add(new String[]{fullName, mobileNumber, email, gender});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }
}
