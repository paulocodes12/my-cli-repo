package model;

import dao.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class Student {

    private int studentID;
    private int userID;
    private String studentNumber;
    private String name;
    private String address;
    private int yearLevel;
    private String status;

    public Student() {
    }

    public Student(int studentID, int userID, String studentNumber, String name, String address, int yearLevel, String status) {
        this.studentID = studentID;
        this.userID = userID;
        this.studentNumber = studentNumber;
        this.name = name;
        this.address = address;
        this.yearLevel = yearLevel;
        this.status = status;
    }

    public Student(int userID, String studentNumber, String name, String address, int yearLevel, String status) {
        this.userID = userID;
        this.studentNumber = studentNumber;
        this.name = name;
        this.address = address;
        this.yearLevel = yearLevel;
        this.status = status;
    }

    public boolean addStudent() {
        String sql = "INSERT INTO students (user_id, student_number, name, address, year_level, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (this.userID > 0) {
                ps.setInt(1, this.userID);
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, this.studentNumber);
            ps.setString(3, this.name);
            ps.setString(4, this.address);
            ps.setInt(5, this.yearLevel);
            ps.setString(6, this.status);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Add student error: " + e.getMessage());
            return false;
        }
    }

    public static List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT student_id, user_id, student_number, name, address, year_level, status FROM students ORDER BY student_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("student_id"),
                        rs.getInt("user_id"),
                        rs.getString("student_number"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getInt("year_level"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Fetch students error: " + e.getMessage());
        }
        return list;
    }

    public static List<Student> searchStudents(String keyword) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT student_id, user_id, student_number, name, address, year_level, status FROM students " +
                     "WHERE student_number LIKE ? OR name LIKE ? OR address LIKE ? OR status LIKE ? ORDER BY student_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Student(
                            rs.getInt("student_id"),
                            rs.getInt("user_id"),
                            rs.getString("student_number"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getInt("year_level"),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Search students error: " + e.getMessage());
        }
        return list;
    }

    public boolean updateStudent() {
        String sql = "UPDATE students SET student_number = ?, name = ?, address = ?, year_level = ?, status = ? WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, this.studentNumber);
            ps.setString(2, this.name);
            ps.setString(3, this.address);
            ps.setInt(4, this.yearLevel);
            ps.setString(5, this.status);
            ps.setInt(6, this.studentID);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update student error: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteStudent(int studentID) {
        String sql = "DELETE FROM students WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete student error: " + e.getMessage());
            return false;
        }
    }
 
    public int getStudentID() { return studentID; }
    public void setStudentID(int studentID) { this.studentID = studentID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getYearLevel() { return yearLevel; }
    public void setYearLevel(int yearLevel) { this.yearLevel = yearLevel; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}