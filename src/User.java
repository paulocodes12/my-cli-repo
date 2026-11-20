// Admin and Staff accounts are Users.
public class User {

    // Encapsulation: private attributes
    private String username;
    private String role;

    // Registered accounts: { username, password, role }
    private static final String[][] ACCOUNTS = {
        { "admin", "admin123", "ADMIN" },
        { "staff", "staff123", "STAFF" }
    };

    public User() {
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }

    // Checks the username and password against the registered accounts.
    public boolean login(String username, String password) {
        for (String[] account : ACCOUNTS) {
            if (account[0].equals(username) && account[1].equals(password)) {
                this.username = account[0];
                this.role = account[2];
                return true;
            }
        }
        return false;
    }

    public void logout() {
        username = null;
        role = null;
    }
}
