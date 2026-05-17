package gui;

import javax.swing.*;
import java.awt.*;
import exceptions.LessonTimeConflictException;
import services.UniversityDatabase;
import services.AuthService;
import models.User;

public class WspGui extends JFrame {
    private UniversityDatabase db;
    private AuthService authService;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel globalHeader;
    private JLabel welcomeLabel;
    private int currentFontSize = 14;
    
    // Modern Color Palette
    private final Color COLOR_BG = new Color(240, 242, 245);
    private final Color COLOR_HEADER = new Color(33, 150, 243); // Blue
    private final Color COLOR_TEXT = new Color(44, 62, 80);
    private final String FONT_NAME = "SansSerif";
    
    public WspGui(UniversityDatabase db) {
        this.db = db;
        this.authService = new AuthService(db);
        this.authService.addObserver(db.getLogger());
        
        setTitle("WSP-wannabe Portal");
        setSize(1000, 750); // Increased default size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        initGlobalHeader();
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        initLoginScreen();
        
        add(globalHeader, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        applyFontSize(this, currentFontSize);
    }

    private void initGlobalHeader() {
        globalHeader = new JPanel(new BorderLayout());
        globalHeader.setBackground(COLOR_HEADER);
        globalHeader.setPreferredSize(new Dimension(1000, 50));

        welcomeLabel = new JLabel("  University Portal");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font(FONT_NAME, Font.BOLD, 18));
        globalHeader.add(welcomeLabel, BorderLayout.WEST);

        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlsPanel.setOpaque(false);

        JButton zoomInBtn = new JButton("Zoom +");
        JButton zoomOutBtn = new JButton("Zoom -");

        for (JButton btn : new JButton[]{zoomInBtn, zoomOutBtn}) {
            btn.setBackground(Color.WHITE);
            btn.setForeground(COLOR_HEADER);
            btn.setFocusPainted(false);
            btn.setFont(new Font(FONT_NAME, Font.BOLD, 12));
        }

        zoomInBtn.addActionListener(e -> {
            currentFontSize += 2;
            applyFontSize(this, currentFontSize);
            this.revalidate();
        });

        zoomOutBtn.addActionListener(e -> {
            if (currentFontSize > 8) {
                currentFontSize -= 2;
                applyFontSize(this, currentFontSize);
                this.revalidate();
            }
        });

        controlsPanel.add(zoomInBtn);
        controlsPanel.add(zoomOutBtn);
        globalHeader.add(controlsPanel, BorderLayout.EAST);
    }
    
    private void applyFontSize(Container container, int size) {
        for (Component c : container.getComponents()) {
            updateComponentFont(c, size);
            if (c instanceof JPanel) {
                c.setBackground(COLOR_BG);
            }
            if (c instanceof JLabel) {
                c.setForeground(COLOR_TEXT);
            }
            if (c instanceof Container) {
                applyFontSize((Container) c, size);
            }
        }
    }

    private void updateComponentFont(Component c, int size) {
        Font oldFont = c.getFont();
        int style = (oldFont != null) ? oldFont.getStyle() : Font.PLAIN;
        c.setFont(new Font(FONT_NAME, style, size));

        if (c instanceof JTable table) {
            table.setRowHeight(size + 15); // Dynamic row height
            table.getTableHeader().setFont(new Font(FONT_NAME, Font.BOLD, size));
            table.setBackground(Color.WHITE);
            table.setSelectionBackground(COLOR_HEADER);
            table.setSelectionForeground(Color.WHITE);
        }
    }
    
    private void initLoginScreen() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(COLOR_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel titleLabel = new JLabel("University Portal Login");
        titleLabel.setFont(new Font(FONT_NAME, Font.BOLD, 28));
        titleLabel.setForeground(COLOR_HEADER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        JLabel emailLabel = new JLabel("Email:");
        loginPanel.add(emailLabel, gbc);
        
        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        loginPanel.add(emailField, gbc);
        
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel passLabel = new JLabel("Password:");
        loginPanel.add(passLabel, gbc);
        
        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);
        
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(COLOR_HEADER);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font(FONT_NAME, Font.BOLD, 14));
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);
        
        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            try {
                User user = authService.login(email, password);
                showDashboard(user);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Login failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        mainPanel.add(loginPanel, "LOGIN");
    }
    
    private void showDashboard(User user) {
        // Update header for logged in user
        welcomeLabel.setText("  Welcome, " + user.getName() + " (" + user.getRole() + ")");
        
        JPanel headerControls = (JPanel) globalHeader.getComponent(1); // Get controlsPanel
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(Color.WHITE);
        logoutBtn.setForeground(COLOR_HEADER);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font(FONT_NAME, Font.BOLD, 12));
        logoutBtn.addActionListener(e -> {
            authService.logout();
            welcomeLabel.setText("  University Portal");
            headerControls.remove(logoutBtn);
            cardLayout.show(mainPanel, "LOGIN");
            globalHeader.revalidate();
            globalHeader.repaint();
        });
        headerControls.add(logoutBtn);
        globalHeader.revalidate();

        JPanel dashboard = new JPanel(new BorderLayout());
        dashboard.setBackground(COLOR_BG);
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font(FONT_NAME, Font.BOLD, 14));
        tabs.setBackground(Color.WHITE);
        
        // News Tab
        tabs.addTab("News", createNewsPanel(user));
        
        if (user.getRole() != enums.UserType.STUDENT) {
            tabs.addTab("Messages", createMessagesPanel(user));
        }

        // Role-specific tabs
        switch (user.getRole()) {
            case STUDENT:
                tabs.addTab("Schedule", createSchedulePanel((models.Student)user));
                tabs.addTab("Courses", createStudentCoursesPanel((models.Student)user));
                tabs.addTab("My Results", createStudentResultsPanel((models.Student)user));
                break;
            case TEACHER:
            case TEACHER_RESEARCHER:
                tabs.addTab("My Schedule", createTeacherSchedulePanel((models.Teacher)user));
                tabs.addTab("Manage Courses", createTeacherCoursesPanel((models.Teacher)user));
                break;
            case MANAGER:
                tabs.addTab("Manage Courses", createManagerCoursesPanel());
                tabs.addTab("Registration Requests", createRegistrationRequestsPanel());
                break;
            case DEAN:
            case RECTOR:
                tabs.addTab("Employee Requests", createEmployeeRequestsPanel());
                break;
            case ADMIN:
                tabs.addTab("Manage Users", createAdminUsersPanel());
                break;
        }
        
        dashboard.add(tabs, BorderLayout.CENTER);
        
        mainPanel.add(dashboard, "DASHBOARD");
        cardLayout.show(mainPanel, "DASHBOARD");
        applyFontSize(mainPanel, currentFontSize); // Ensure new dashboard components have correct font
    }
    
    private JPanel createStudentResultsPanel(models.Student student) {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnNames = {"Course Code", "Course Name", "1st Att", "2nd Att", "Final", "Total", "GPA Contribution"};
        java.util.List<models.Mark> marks = student.getMarks();
        Object[][] data = new Object[marks.size()][7];

        for (int i = 0; i < marks.size(); i++) {
            models.Mark m = marks.get(i);
            models.Course c = m.getCourse();
            data[i][0] = c != null ? c.getCode() : "N/A";
            data[i][1] = c != null ? c.getName() : "N/A";
            data[i][2] = m.getFirstAttestation();
            data[i][3] = m.getSecondAttestation();
            data[i][4] = m.getFinalExam();
            data[i][5] = m.getTotal();
            data[i][6] = String.format("%.2f", m.getTotal() / 25.0); // Simple GPA conversion
        }

        JTable table = new JTable(data, columnNames);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JLabel gpaLabel = new JLabel(" Overall GPA: " + String.format("%.2f", student.calculateGPA()));
        gpaLabel.setFont(new Font(FONT_NAME, Font.BOLD, currentFontSize + 2));
        panel.add(gpaLabel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createTeacherSchedulePanel(models.Teacher teacher) {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnNames = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Object[][] data = new Object[13][7];
        
        for (int i = 0; i < 13; i++) {
            data[i][0] = (i + 8) + ":00";
        }
        
        for (models.Lesson lesson : db.getLessons()) {
            if (lesson.getTeacher().getId().equals(teacher.getId())) {
                int row = lesson.getHour() - 8;
                int col = lesson.getDay().ordinal() + 1;
                if (row >= 0 && row < 13 && col >= 1 && col < 7) {
                    data[row][col] = lesson.getCourse().getCode() + " (" + lesson.getLessonType() + ") [Room " + lesson.getRoomNumber() + "]";
                }
            }
        }
        
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(30);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createEmployeeRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        java.util.List<models.EmployeeRequest> requests = db.getEmployeeRequests();
        
        for (models.EmployeeRequest r : requests) {
            if (r.getStatus() == enums.RequestStatus.PENDING) {
                listModel.addElement(r.getEmployeeId() + ": " + r.getContent());
            }
        }
        JList<String> list = new JList<>(listModel);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton approveBtn = new JButton("Approve");
        JButton rejectBtn = new JButton("Reject");
        btnPanel.add(approveBtn);
        btnPanel.add(rejectBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        approveBtn.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx != -1) {
                int currentPendingIdx = 0;
                for (models.EmployeeRequest r : requests) {
                    if (r.getStatus() == enums.RequestStatus.PENDING) {
                        if (currentPendingIdx == idx) {
                            r.setStatus(enums.RequestStatus.APPROVED);
                            JOptionPane.showMessageDialog(this, "Approved!");
                            listModel.remove(idx);
                            break;
                        }
                        currentPendingIdx++;
                    }
                }
            }
        });

        rejectBtn.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx != -1) {
                int currentPendingIdx = 0;
                for (models.EmployeeRequest r : requests) {
                    if (r.getStatus() == enums.RequestStatus.PENDING) {
                        if (currentPendingIdx == idx) {
                            r.setStatus(enums.RequestStatus.REJECTED);
                            JOptionPane.showMessageDialog(this, "Rejected!");
                            listModel.remove(idx);
                            break;
                        }
                        currentPendingIdx++;
                    }
                }
            }
        });

        return panel;
    }

    private JPanel createNewsPanel(models.User user) {
        JPanel panel = new JPanel(new BorderLayout());
        
        DefaultListModel<models.NewsItem> listModel = new DefaultListModel<>();
        for (models.NewsItem item : db.getNews()) {
            listModel.addElement(item);
        }
        JList<models.NewsItem> list = new JList<>(listModel);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof models.NewsItem item) {
                    value = item.toString();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        // Admins and Managers can add/delete news
        if (user.getRole() == enums.UserType.ADMIN || user.getRole() == enums.UserType.MANAGER) {
            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton addNewsBtn = new JButton("Add News");
            JButton deleteNewsBtn = new JButton("Delete Selected");
            toolbar.add(addNewsBtn);
            toolbar.add(deleteNewsBtn);
            panel.add(toolbar, BorderLayout.NORTH);

            addNewsBtn.addActionListener(e -> {
                showAddNewsDialog();
                refreshNewsList(listModel);
            });

            deleteNewsBtn.addActionListener(e -> {
                models.NewsItem selected = list.getSelectedValue();
                if (selected != null) {
                    db.removeNews(selected);
                    refreshNewsList(listModel);
                    JOptionPane.showMessageDialog(this, "News deleted.");
                } else {
                    JOptionPane.showMessageDialog(this, "Select a news item to delete.");
                }
            });
        }
        
        return panel;
    }

    private void refreshNewsList(DefaultListModel<models.NewsItem> model) {
        model.clear();
        for (models.NewsItem item : db.getNews()) {
            model.addElement(item);
        }
    }
    
    private JPanel createManagerCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addCourseBtn = new JButton("Add Course");
        toolbar.add(addCourseBtn);
        panel.add(toolbar, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (models.Course course : db.getCourses()) {
            listModel.addElement(course.getCode() + " - " + course.getName());
        }
        JList<String> list = new JList<>(listModel);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        addCourseBtn.addActionListener(e -> {
            JTextField codeF = new JTextField();
            JTextField nameF = new JTextField();
            JTextField creditsF = new JTextField();
            JTextField yearF = new JTextField();
            Object[] msg = { "Code:", codeF, "Name:", nameF, "Credits:", creditsF, "Year Required:", yearF };
            int opt = JOptionPane.showConfirmDialog(this, msg, "Add Course", JOptionPane.OK_CANCEL_OPTION);
            if (opt == JOptionPane.OK_OPTION) {
                try {
                    int credits = validateInt(creditsF.getText(), 1, 10, "Credits");
                    int year = validateInt(yearF.getText(), 1, 4, "Year Required");
                    models.Course c = new models.Course(codeF.getText(), nameF.getText(), credits, year);
                    db.addCourse(c);
                    listModel.addElement(c.getCode() + " - " + c.getName());
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });
        return panel;
    }

    private JPanel createRegistrationRequestsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        java.util.List<models.RegistrationRequest> requests = db.getRegistrationRequests();
        
        for (models.RegistrationRequest r : requests) {
            if (r.getStatus() == enums.RequestStatus.PENDING) {
                listModel.addElement(r.getStudent().getName() + " -> " + r.getCourse().getCode());
            }
        }
        JList<String> list = new JList<>(listModel);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton approveBtn = new JButton("Approve Selected");
        panel.add(approveBtn, BorderLayout.SOUTH);

        approveBtn.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx != -1) {
                // Find the actual pending request (this is a bit simplistic as indices might shift)
                // In a real app we'd use a better model
                int currentPendingIdx = 0;
                for (models.RegistrationRequest r : requests) {
                    if (r.getStatus() == enums.RequestStatus.PENDING) {
                        if (currentPendingIdx == idx) {
                            try {
                                r.getStudent().requestToEnroll(r.getCourse());
                                r.setStatus(enums.RequestStatus.APPROVED);
                                JOptionPane.showMessageDialog(this, "Approved!");
                                listModel.remove(idx);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(this, "Failed: " + ex.getMessage());
                                r.setStatus(enums.RequestStatus.REJECTED);
                                listModel.remove(idx);
                            }
                            break;
                        }
                        currentPendingIdx++;
                    }
                }
            }
        });
        return panel;
    }

    private JPanel createTeacherCoursesPanel(models.Teacher teacher) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton gradeBtn = new JButton("Grade Student");
        JButton addLessonBtn = new JButton("Add Lesson");
        toolbar.add(gradeBtn);
        toolbar.add(addLessonBtn);
        panel.add(toolbar, BorderLayout.NORTH);

        DefaultListModel<models.Course> listModel = new DefaultListModel<>();
        for (models.Course course : teacher.getTeachingCourses()) {
            listModel.addElement(course);
        }
        JList<models.Course> list = new JList<>(listModel);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof models.Course c) {
                    value = c.getCode() + " - " + c.getName();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        gradeBtn.addActionListener(e -> {
            models.Course selectedCourse = list.getSelectedValue();
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Select a course first.");
                return;
            }
            showGradingDialog(teacher, selectedCourse);
        });

        addLessonBtn.addActionListener(e -> {
            models.Course selectedCourse = list.getSelectedValue();
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Select a course first.");
                return;
            }
            showAddLessonDialog(teacher, selectedCourse);
        });

        return panel;
    }

    private void showGradingDialog(models.Teacher teacher, models.Course course) {
        java.util.List<models.Student> students = db.getUsers().stream()
            .filter(u -> u instanceof models.Student)
            .map(u -> (models.Student) u)
            .filter(s -> s.getEnrolledCourses().contains(course))
            .toList();
        
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students enrolled in this course.");
            return;
        }

        JComboBox<models.Student> studentCombo = new JComboBox<>(new DefaultComboBoxModel<>(students.toArray(new models.Student[0])));
        JTextField m1F = new JTextField();
        JTextField m2F = new JTextField();
        JTextField exF = new JTextField();

        Object[] msg = { "Student:", studentCombo, "First Attestation (0-30):", m1F, "Second Attestation (0-30):", m2F, "Final Exam (0-40):", exF };
        int opt = JOptionPane.showConfirmDialog(this, msg, "Grade Student", JOptionPane.OK_CANCEL_OPTION);
        
        if (opt == JOptionPane.OK_OPTION) {
            try {
                int m1 = validateInt(m1F.getText(), 0, 30, "First Attestation");
                int m2 = validateInt(m2F.getText(), 0, 30, "Second Attestation");
                int ex = validateInt(exF.getText(), 0, 40, "Final Exam");
                
                teacher.putMark(course, (models.Student)studentCombo.getSelectedItem(), m1, m2, ex);
                JOptionPane.showMessageDialog(this, "Mark recorded!");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                showGradingDialog(teacher, course); // Re-show on error
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private int validateInt(String text, int min, int max, String fieldName) {
        try {
            int val = Integer.parseInt(text.trim());
            if (val < min || val > max) {
                throw new IllegalArgumentException(fieldName + " must be between " + min + " and " + max + ".");
            }
            return val;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid number.");
        }
    }

    private void showAddLessonDialog(models.Teacher teacher, models.Course course) {
        JComboBox<enums.LessonType> typeCombo = new JComboBox<>(enums.LessonType.values());
        JComboBox<enums.WeekDays> dayCombo = new JComboBox<>(enums.WeekDays.values());
        JTextField hourF = new JTextField();
        JTextField roomF = new JTextField();

        Object[] msg = { "Type:", typeCombo, "Day:", dayCombo, "Hour (8-20):", hourF, "Room:", roomF };
        int opt = JOptionPane.showConfirmDialog(this, msg, "Add Lesson to Schedule", JOptionPane.OK_CANCEL_OPTION);

        if (opt == JOptionPane.OK_OPTION) {
            try {
                int hour = validateInt(hourF.getText(), 8, 20, "Hour");
                int room = validateInt(roomF.getText(), 1, 999, "Room");
                models.Lesson lesson = new models.Lesson(course, teacher, (enums.LessonType)typeCombo.getSelectedItem(),
                    (enums.WeekDays)dayCombo.getSelectedItem(), hour, room);
                db.addLesson(lesson);
                JOptionPane.showMessageDialog(this, "Lesson added to schedule!");
            } catch (LessonTimeConflictException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Schedule Conflict", JOptionPane.ERROR_MESSAGE);
                showAddLessonDialog(teacher, course);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                showAddLessonDialog(teacher, course);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private JPanel createAdminUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Toolbar for Admin actions
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addUserBtn = new JButton("Add User");
        JButton removeUserBtn = new JButton("Remove User");
        
        toolbar.add(addUserBtn);
        toolbar.add(removeUserBtn);
        panel.add(toolbar, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Name", "Email", "Role"};
        updateAdminTable(panel, columnNames);

        addUserBtn.addActionListener(e -> showAddUserDialog(panel, columnNames));
        removeUserBtn.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this, "Enter User ID to remove:");
            if (id != null && !id.isEmpty()) {
                models.User u = db.findUserById(id);
                if (u != null) {
                    db.removeUser(u);
                    updateAdminTable(panel, columnNames);
                } else {
                    JOptionPane.showMessageDialog(this, "User not found.");
                }
            }
        });

        return panel;
    }

    private void updateAdminTable(JPanel panel, String[] columnNames) {
        java.util.List<models.User> users = db.getUsers();
        Object[][] data = new Object[users.size()][4];
        for (int i = 0; i < users.size(); i++) {
            models.User u = users.get(i);
            data[i][0] = u.getId();
            data[i][1] = u.getName();
            data[i][2] = u.getEmail();
            data[i][3] = u.getRole();
        }
        JTable table = new JTable(data, columnNames);
        
        // Find existing scrollpane and replace its viewport
        Component[] comps = panel.getComponents();
        for (Component c : comps) {
            if (c instanceof JScrollPane) {
                panel.remove(c);
            }
        }
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    private void showAddUserDialog(JPanel panel, String[] columnNames) {
        JDialog dialog = new JDialog(this, "Add User", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idF = new JTextField(15);
        JTextField nameF = new JTextField(15);
        JTextField emailF = new JTextField(15);
        JTextField passF = new JTextField(15);
        JComboBox<enums.UserType> typeCombo = new JComboBox<>(enums.UserType.values());

        int r = 0;
        dialog.add(new JLabel("Type:"), gbc); gbc.gridx=1; dialog.add(typeCombo, gbc); gbc.gridx=0; gbc.gridy=++r;
        dialog.add(new JLabel("ID:"), gbc); gbc.gridx=1; dialog.add(idF, gbc); gbc.gridx=0; gbc.gridy=++r;
        dialog.add(new JLabel("Name:"), gbc); gbc.gridx=1; dialog.add(nameF, gbc); gbc.gridx=0; gbc.gridy=++r;
        dialog.add(new JLabel("Email:"), gbc); gbc.gridx=1; dialog.add(emailF, gbc); gbc.gridx=0; gbc.gridy=++r;
        dialog.add(new JLabel("Password:"), gbc); gbc.gridx=1; dialog.add(passF, gbc); gbc.gridx=0; gbc.gridy=++r;

        JButton saveBtn = new JButton("Save");
        gbc.gridx = 0; gbc.gridy = ++r; gbc.gridwidth = 2;
        dialog.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            try {
                models.User newUser = utils.UserFactory.createUser((enums.UserType)typeCombo.getSelectedItem(), 
                    idF.getText(), nameF.getText(), emailF.getText(), passF.getText());
                db.addUser(newUser);
                updateAdminTable(panel, columnNames);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAddNewsDialog() {
        JTextField titleF = new JTextField();
        JTextArea bodyF = new JTextArea(5, 20);
        Object[] message = { "Title:", titleF, "Body:", new JScrollPane(bodyF) };
        int option = JOptionPane.showConfirmDialog(this, message, "Add News", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            db.addNews(new models.NewsItem(titleF.getText(), bodyF.getText()));
        }
    }
    
    private JPanel createSchedulePanel(models.Student student) {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columnNames = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Object[][] data = new Object[13][7];
        
        for (int i = 0; i < 13; i++) {
            data[i][0] = (i + 8) + ":00";
        }
        
        for (models.Lesson lesson : db.getLessons()) {
            // Check if student is enrolled in this lesson's course
            if (student.getEnrolledCourses().contains(lesson.getCourse())) {
                int row = lesson.getHour() - 8;
                int col = lesson.getDay().ordinal() + 1;
                if (row >= 0 && row < 13 && col >= 1 && col < 7) {
                    data[row][col] = lesson.getCourse().getCode() + " (" + lesson.getLessonType() + ")";
                }
            }
        }
        
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(30);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMessagesPanel(models.User user) {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        java.util.List<models.Message> messages = db.getMessagesWithUser(user.getId());
        for (models.Message m : messages) {
            listModel.addElement("From: " + m.getFrom() + " | " + m.getContent());
        }
        JList<String> list = new JList<>(listModel);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        JButton sendBtn = new JButton("Send Message");
        panel.add(sendBtn, BorderLayout.SOUTH);

        sendBtn.addActionListener(e -> {
            java.util.List<models.User> recipients = db.getUsers().stream()
                .filter(u -> !(u instanceof models.Student))
                .filter(u -> !u.getId().equals(user.getId()))
                .toList();
            
            JComboBox<models.User> recipientCombo = new JComboBox<>(new DefaultComboBoxModel<>(recipients.toArray(new models.User[0])));
            recipientCombo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    if (value instanceof models.User u) {
                        value = u.getName() + " (" + u.getRole() + ")";
                    }
                    return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                }
            });
            
            JTextArea contentArea = new JTextArea(5, 20);
            Object[] msg = { "Recipient:", recipientCombo, "Message:", new JScrollPane(contentArea) };
            int opt = JOptionPane.showConfirmDialog(this, msg, "Send Message", JOptionPane.OK_CANCEL_OPTION);
            
            if (opt == JOptionPane.OK_OPTION) {
                models.User selected = (models.User) recipientCombo.getSelectedItem();
                if (selected != null) {
                    models.Message m = new models.Message(user.getId(), selected.getId(), contentArea.getText());
                    db.addMessage(m);
                    listModel.addElement("To: " + selected.getName() + " | " + m.getContent());
                }
            }
        });

        return panel;
    }

    private JPanel createStudentCoursesPanel(models.Student student) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton registerBtn = new JButton("Register for Course");
        toolbar.add(registerBtn);
        panel.add(toolbar, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (models.Course course : student.getEnrolledCourses()) {
            listModel.addElement(course.getCode() + " - " + course.getName() + " (" + course.getCredits() + " credits)");
        }
        JList<String> list = new JList<>(listModel);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        registerBtn.addActionListener(e -> {
            java.util.List<models.Course> allCourses = db.getCourses();
            JComboBox<models.Course> courseCombo = new JComboBox<>(new DefaultComboBoxModel<>(allCourses.toArray(new models.Course[0])));
            courseCombo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    if (value instanceof models.Course c) {
                        value = c.getCode() + " - " + c.getName();
                    }
                    return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                }
            });

            int opt = JOptionPane.showConfirmDialog(this, courseCombo, "Select Course to Register", JOptionPane.OK_CANCEL_OPTION);
            if (opt == JOptionPane.OK_OPTION) {
                models.Course selected = (models.Course) courseCombo.getSelectedItem();
                if (selected != null) {
                    models.RegistrationRequest req = new models.RegistrationRequest(student, selected);
                    db.addRegistrationRequest(req);
                    JOptionPane.showMessageDialog(this, "Registration request submitted!");
                }
            }
        });

        return panel;
    }
    
    public static void start(UniversityDatabase db) {
        SwingUtilities.invokeLater(() -> {
            new WspGui(db).setVisible(true);
        });
    }
}
