package model;

import dao.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    // Encapsulation: all attributes are private
    private int userID;
    private String username;
    private String password;
    private String role;

    public User() {
    }

    public User(int userID, String username, String password, String role) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Checks the users table. Returns true and fills this object if correct.
    public boolean login(String username, String password) {
        String sql = "SELECT user_id, username, password, role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    this.userID = rs.getInt("user_id");
                    this.username = rs.getString("username");
                    this.password = rs.getString("password");
                    this.role = rs.getString("role");
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return false;
    }

    public void logout() {
        this.userID = 0;
        this.username = null;
        this.password = null;
        this.role = null;
    }

    // True if this user is the administrator (accepts either role spelling)
    public boolean isAdmin() {
        return "Administrator".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role);
    }

    // Getters and setters
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}