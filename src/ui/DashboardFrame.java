package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Student;
import model.User;

public class DashboardFrame extends JFrame {

    private final User currentUser;

    private JTextField studentNumberField;
    private JTextField nameField;
    private JTextField addressField;
    private JComboBox<Integer> yearLevelBox;
    private JTextField searchField;

    private JTable studentTable;
    private DefaultTableModel tableModel;
    private int selectedStudentID = -1;

    public DashboardFrame(User user) {
        this.currentUser = user;

        setTitle("Student Enrollment Management System - Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1020, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildMainContent(), BorderLayout.CENTER);

        loadStudents(Student.viewStudent());
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(LoginFrame.NAVY);
        header.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel title = new JLabel("Student Enrollment Management System");
        title.setFont(new Font("SansSerif", Font.BOLD, 19));
        title.setForeground(Color.WHITE);

        JPanel rightBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightBox.setOpaque(false);

        JLabel userLabel = new JLabel("Logged in: " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        userLabel.setForeground(new Color(200, 215, 245));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(210, 55, 55));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoutBtn.setOpaque(true);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> {
            currentUser.logout();
            dispose();
            new LoginFrame().setVisible(true);
        });

        rightBox.add(userLabel);
        rightBox.add(logoutBtn);

        header.add(title, BorderLayout.WEST);
        header.add(rightBox, BorderLayout.EAST);
        return header;
    }

    private JPanel buildMainContent() {
        JPanel container = new JPanel(new BorderLayout(16, 16));
        container.setBackground(LoginFrame.LIGHT);
        container.setBorder(new EmptyBorder(18, 20, 18, 20));

        container.add(buildFormPanel(), BorderLayout.WEST);
        container.add(buildTablePanel(), BorderLayout.CENTER);
        return container;
    }

    private JPanel buildFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(310, 0));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel formTitle = new JLabel("Student Information");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 17));
        formTitle.setForeground(LoginFrame.NAVY);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        studentNumberField = new JTextField();
        nameField = new JTextField();
        addressField = new JTextField();
        yearLevelBox = new JComboBox<>(new Integer[]{1, 2, 3, 4});

        styleInput(studentNumberField);
        styleInput(nameField);
        styleInput(addressField);
        styleInput(yearLevelBox);

        JButton addBtn = makeActionButton("Add Student", LoginFrame.BLUE);
        addBtn.addActionListener(e -> handleAdd());

        JButton updateBtn = makeActionButton("Update Selected", new Color(34, 139, 84));
        updateBtn.addActionListener(e -> handleUpdate());

        JButton archiveBtn = makeActionButton("Archive Selected", new Color(200, 50, 50));
        archiveBtn.addActionListener(e -> handleArchive());

        JButton clearBtn = makeActionButton("Clear Form", new Color(110, 118, 130));
        clearBtn.addActionListener(e -> clearForm());

        formPanel.add(formTitle);
        formPanel.add(Box.createVerticalStrut(16));
        formPanel.add(makeLabel("Student Number"));
        formPanel.add(studentNumberField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(makeLabel("Full Name"));
        formPanel.add(nameField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(makeLabel("Address"));
        formPanel.add(addressField);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(makeLabel("Year Level"));
        formPanel.add(yearLevelBox);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(addBtn);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(updateBtn);
        if (currentUser.isAdmin()) {                 // only the admin sees Archive
            formPanel.add(Box.createVerticalStrut(8));
            formPanel.add(archiveBtn);
        }
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(clearBtn);

        return formPanel;
    }

    private JPanel buildTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(0, 12));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top search bar
        JPanel topBar = new JPanel(new BorderLayout(8, 0));
        topBar.setBackground(Color.WHITE);

        searchField = new JTextField();
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(0, 34));

        JButton searchBtn = new JButton("Search");
        searchBtn.setBackground(LoginFrame.BLUE);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        searchBtn.setOpaque(true);
        searchBtn.setBorderPainted(false);
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                loadStudents(Student.viewStudent());
            } else {
                loadStudents(Student.searchStudents(keyword));
            }
        });

        JButton refreshBtn = new JButton("Reset");
        refreshBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            loadStudents(Student.viewStudent());
        });

        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        btnGroup.setBackground(Color.WHITE);
        btnGroup.add(searchBtn);
        btnGroup.add(refreshBtn);

        topBar.add(new JLabel("Search Student: "), BorderLayout.WEST);
        topBar.add(searchField, BorderLayout.CENTER);
        topBar.add(btnGroup, BorderLayout.EAST);

        // Table
        String[] columns = {"ID", "Student Number", "Name", "Address", "Year"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(28);
        studentTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        studentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        studentTable.getTableHeader().setBackground(LoginFrame.NAVY);
        studentTable.getTableHeader().setForeground(Color.WHITE);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = studentTable.getSelectedRow();
                if (row >= 0) {
                    selectedStudentID = (int) tableModel.getValueAt(row, 0);
                    studentNumberField.setText(tableModel.getValueAt(row, 1).toString());
                    nameField.setText(tableModel.getValueAt(row, 2).toString());
                    addressField.setText(tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
                    yearLevelBox.setSelectedItem(tableModel.getValueAt(row, 4));
                }
            }
        });

        tablePanel.add(topBar, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        return tablePanel;
    }

    private void loadStudents(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                    s.getStudentID(),
                    s.getStudentNumber(),
                    s.getName(),
                    s.getAddress(),
                    s.getYearLevel()
            });
        }
    }

    private void handleAdd() {
        String studNum = studentNumberField.getText().trim();
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        int year = (Integer) yearLevelBox.getSelectedItem();

        if (studNum.isEmpty() || name.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Student Number, Name, and Address.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = new Student(0, studNum, name, address, year);
        if (student.addStudent()) {
            JOptionPane.showMessageDialog(this, "Student enrolled successfully!");
            clearForm();
            loadStudents(Student.viewStudent());
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add student. Check if Student Number already exists.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() {
        if (selectedStudentID == -1) {
            JOptionPane.showMessageDialog(this, "Select a student row from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String studNum = studentNumberField.getText().trim();
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        int year = (Integer) yearLevelBox.getSelectedItem();

        if (studNum.isEmpty() || name.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = new Student(selectedStudentID, 0, studNum, name, address, year);
        if (student.updateStudent()) {
            JOptionPane.showMessageDialog(this, "Student updated successfully!");
            clearForm();
            loadStudents(Student.viewStudent());
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update student.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleArchive() {
        if (selectedStudentID == -1) {
            JOptionPane.showMessageDialog(this, "Select a student row from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!currentUser.isAdmin()) {
            JOptionPane.showMessageDialog(this, "Only the Administrator can archive records.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Archive this student record? It will be hidden from the list but kept in the database.",
                "Confirm Archive", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (Student.archiveStudent(selectedStudentID)) {
                JOptionPane.showMessageDialog(this, "Student archived.");
                clearForm();
                loadStudents(Student.viewStudent());
            } else {
                JOptionPane.showMessageDialog(this, "Failed to archive student.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedStudentID = -1;
        studentNumberField.setText("");
        nameField.setText("");
        addressField.setText("");
        yearLevelBox.setSelectedIndex(0);
        studentTable.clearSelection();
    }

    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleInput(JComponent comp) {
        comp.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        comp.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JButton makeActionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        return btn;
    }
}