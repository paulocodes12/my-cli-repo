package ui;

import dao.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {

            System.out.println("Connected to MySQL!");

            while (rs.next()) {
                System.out.println(rs.getInt("student_id") + " | "
                        + rs.getString("student_number") + " | "
                        + rs.getString("name") + " | Year "
                        + rs.getInt("year_level"));
            }

        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}