package model;

import dao.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


public class Student extends User {

    // Record states stored in students.status (archive instead of delete)
    private static final String ACTIVE = "ACTIVE";
    private static final String ARCHIVED = "ARCHIVED";

    private int studentID;
    private String studentNumber;
    private String name;
    private String address;
    private int yearLevel;

    public Student() {
    }

    // Used when loading an existing student from the database
    public Student(int studentID, int userID, String studentNumber, String name, String address, int yearLevel) {
        this.studentID = studentID;
        setUserID(userID);
        this.studentNumber = studentNumber;
        this.name = name;
        this.address = address;
        this.yearLevel = yearLevel;
    }

    // Used when creating a new student (no studentID yet)
    public Student(int userID, String studentNumber, String name, String address, int yearLevel) {
        setUserID(userID);
        this.studentNumber = studentNumber;
        this.name = name;
        this.address = address;
        this.yearLevel = yearLevel;
    }

    // UML: addStudent()
    public boolean addStudent() {
        String sql = "INSERT INTO students (user_id, student_number, name, address, year_level, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (getUserID() > 0) {
                ps.setInt(1, getUserID());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, this.studentNumber);
            ps.setString(3, this.name);
            ps.setString(4, this.address);
            ps.setInt(5, this.yearLevel);
            ps.setString(6, ACTIVE);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Add student error: " + e.getMessage());
            return false;
        }
    }

    // UML: viewStudent() - all students that are not archived
    public static List<Student> viewStudent() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT student_id, user_id, student_number, name, address, year_level FROM students " +
                     "WHERE status <> 'ARCHIVED' ORDER BY student_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(fromRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Fetch students error: " + e.getMessage());
        }
        return list;
    }

    // Search by student number, name or address (archived students are hidden)
    public static List<Student> searchStudents(String keyword) {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT student_id, user_id, student_number, name, address, year_level FROM students " +
                     "WHERE status <> 'ARCHIVED' AND (student_number LIKE ? OR name LIKE ? OR address LIKE ?) " +
                     "ORDER BY student_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(fromRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Search students error: " + e.getMessage());
        }
        return list;
    }

    // UML: updateStudent()
    public boolean updateStudent() {
        String sql = "UPDATE students SET student_number = ?, name = ?, address = ?, year_level = ? WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, this.studentNumber);
            ps.setString(2, this.name);
            ps.setString(3, this.address);
            ps.setInt(4, this.yearLevel);
            ps.setInt(5, this.studentID);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update student error: " + e.getMessage());
            return false;
        }
    }

    // Proposal 7.1: archive instead of delete. The row stays in the database.
    public static boolean archiveStudent(int studentID) {
        String sql = "UPDATE students SET status = 'ARCHIVED' WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Archive student error: " + e.getMessage());
            return false;
        }
    }

    // Builds a Student from one row of the result set
    private static Student fromRow(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("student_id"),
                rs.getInt("user_id"),
                rs.getString("student_number"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getInt("year_level")
        );
    }

    public int getStudentID() { return studentID; }
    public void setStudentID(int studentID) { this.studentID = studentID; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getYearLevel() { return yearLevel; }
    public void setYearLevel(int yearLevel) { this.yearLevel = yearLevel; }
}