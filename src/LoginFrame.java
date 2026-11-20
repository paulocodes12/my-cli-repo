import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// The first window of the system: the Login screen.
public class LoginFrame extends JFrame {

    // Colors used across the screens
    static final Color NAVY = new Color(25, 42, 86);
    static final Color BLUE = new Color(52, 120, 246);
    static final Color LIGHT = new Color(245, 247, 251);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginFrame() {
        setTitle("Student Enrollment Management System - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(780, 460);
        setLocationRelativeTo(null);   // center on screen
        setResizable(false);
        setLayout(new GridLayout(1, 2));

        add(buildLeftPanel());
        add(buildRightPanel());
    }

    // Left side: name of the system
    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(NAVY);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(110, 30, 30, 30));

        JLabel title = new JLabel("<html><div style='text-align:center'>Student Enrollment<br>Management System</div></html>");
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Proposal 01");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setForeground(new Color(180, 195, 230));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel school = new JLabel("UM Tagum College");
        school.setFont(new Font("SansSerif", Font.PLAIN, 13));
        school.setForeground(new Color(180, 195, 230));
        school.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(15));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(5));
        panel.add(school);
        return panel;
    }

    // Right side: the login form
    private JPanel buildRightPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(80, 50, 30, 50));

        JLabel heading = new JLabel("Welcome back");
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel hint = new JLabel("Please log in to continue");
        hint.setForeground(Color.GRAY);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        styleField(usernameField);
        styleField(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(BLUE);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginButton.addActionListener(e -> doLogin());
        getRootPane().setDefaultButton(loginButton);   // pressing Enter clicks Login

        messageLabel = new JLabel(" ");
        messageLabel.setForeground(new Color(200, 40, 40));
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(heading);
        panel.add(Box.createVerticalStrut(4));
        panel.add(hint);
        panel.add(Box.createVerticalStrut(25));
        panel.add(makeLabel("Username"));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(12));
        panel.add(makeLabel("Password"));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(loginButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(messageLabel);
        return panel;
    }

    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    // Runs when the Login button is clicked (or Enter is pressed)
    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please type your username and password.");
            return;
        }

        User user = new User();            // uses the User class from User.java
        if (user.login(username, password)) {
            JOptionPane.showMessageDialog(this,
                    "Login successful!\nWelcome, " + user.getUsername() + " (" + user.getRole() + ")",
                    "Login", JOptionPane.INFORMATION_MESSAGE);
            passwordField.setText("");
        } else {
            messageLabel.setText("Wrong username or password.");
            passwordField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
