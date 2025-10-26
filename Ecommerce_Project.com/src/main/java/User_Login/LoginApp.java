package User_Login;

import Home_Page.HomePageGUI;
import Connection_db.Connection_db;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class LoginApp extends JFrame {
    private JTextField emailOrUsernameField, usernameField, emailField, dobField, ageField, phoneField;
    private JPasswordField passwordField;
    private JButton loginButton, createUserButton, loginOptionButton, createOptionButton;
    private Connection_db dbConn = new Connection_db("jdbc:postgresql://localhost:5432/Ecommerce", "postgres", "Achu2006$");
    private JPanel formPanel, optionPanel, centerPanel;

    public LoginApp() {
        setTitle("✨ Gadget Galaxy");
        setSize(550, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(false);

        GradientPanel bgPanel = new GradientPanel();
        bgPanel.setLayout(new BorderLayout());
        add(bgPanel);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(50, 0, 20, 0));
        JLabel titleLabel = new JLabel("Gadget Galaxy");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new CompoundBorder(new EmptyBorder(5, 15, 5, 15), new LineBorder(new Color(255, 255, 255, 100), 2, true)));
        titlePanel.add(titleLabel);
        bgPanel.add(titlePanel, BorderLayout.NORTH);

        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        bgPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel glassCard = new JPanel(new BorderLayout());
        glassCard.setOpaque(false);
        glassCard.setBorder(new CompoundBorder(new LineBorder(new Color(255, 255, 255, 80), 2, true), new EmptyBorder(20, 20, 20, 20)));
        glassCard.setBackground(new Color(255, 255, 255, 30));
        glassCard.setPreferredSize(new Dimension(400, 450));
        glassCard.setLayout(new GridBagLayout());

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.gridx = 0;
        gbcMain.gridy = 0;
        centerPanel.add(glassCard, gbcMain);

        optionPanel = new JPanel();
        optionPanel.setOpaque(false);
        optionPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        loginOptionButton = new JButton("Login");
        loginOptionButton.setBackground(new Color(0, 153, 255));
        loginOptionButton.setForeground(Color.WHITE);
        loginOptionButton.setFocusPainted(false);
        loginOptionButton.setFont(new Font("Segoe UI", Font.BOLD, 16));

        createOptionButton = new JButton("Create Account");
        createOptionButton.setBackground(new Color(0, 200, 83));
        createOptionButton.setForeground(Color.WHITE);
        createOptionButton.setFocusPainted(false);
        createOptionButton.setFont(new Font("Segoe UI", Font.BOLD, 16));

        optionPanel.add(loginOptionButton);
        optionPanel.add(createOptionButton);

        GridBagConstraints gbcOptions = new GridBagConstraints();
        gbcOptions.gridx = 0;
        gbcOptions.gridy = 0;
        gbcOptions.insets = new Insets(10, 10, 10, 10);
        glassCard.add(optionPanel, gbcOptions);

        formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.gridx = 0;
        gbcForm.gridy = 1;
        glassCard.add(formPanel, gbcForm);

        emailOrUsernameField = new JTextField();
        emailOrUsernameField.setText("Enter Username or Email");

        usernameField = new JTextField();
        usernameField.setText("Enter Username");

        emailField = new JTextField();
        emailField.setText("Enter Email");

        dobField = new JTextField();
        dobField.setText("YYYY-MM-DD");

        ageField = new JTextField();
        ageField.setText("Age");

        phoneField = new JTextField();
        phoneField.setText("Phone Number");

        passwordField = new JPasswordField();
        passwordField.setEchoChar('•');

        ageField.setEditable(false);
        ageField.setBackground(new Color(240, 240, 240, 150));

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 153, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));

        createUserButton = new JButton("Create Account");
        createUserButton.setBackground(new Color(0, 200, 83));
        createUserButton.setForeground(Color.WHITE);
        createUserButton.setFocusPainted(false);
        createUserButton.setFont(new Font("Segoe UI", Font.BOLD, 16));


        loginOptionButton.addActionListener(e -> showLoginFields());
        createOptionButton.addActionListener(e -> showNewUserFields());
        loginButton.addActionListener(e -> loginUser());
        createUserButton.addActionListener(e -> createNewUser());

        showLoginFields();
        setVisible(true);
    }

    private void showLoginFields() {
        formPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(emailOrUsernameField, gbc);
        gbc.gridy++;
        formPanel.add(passwordField, gbc);
        gbc.gridy++;
        formPanel.add(loginButton, gbc);
        revalidate();
        repaint();
    }

    private void showNewUserFields() {
        formPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameField, gbc);
        gbc.gridy++;
        formPanel.add(emailField, gbc);
        gbc.gridy++;
        formPanel.add(dobField, gbc);
        dobField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
                try {
                    LocalDate dob = LocalDate.parse(dobField.getText());
                    int age = Period.between(dob, LocalDate.now()).getYears();
                    ageField.setText(String.valueOf(age));
                } catch (Exception ex) {
                    ageField.setText("");
                }
            }
        });
        gbc.gridy++;
        formPanel.add(ageField, gbc);
        gbc.gridy++;
        formPanel.add(phoneField, gbc);
        gbc.gridy++;
        formPanel.add(passwordField, gbc);
        gbc.gridy++;
        formPanel.add(createUserButton, gbc);
        revalidate();
        repaint();
    }

    private void loginUser() {
        String userInput = emailOrUsernameField.getText();
        String password = new String(passwordField.getPassword());
        if (userInput.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fields cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new LoginTask(userInput, password).run();
    }

    private class LoginTask implements Runnable {
        private final String userInput;
        private final String password;

        public LoginTask(String userInput, String password) {
            this.userInput = userInput;
            this.password = password;
        }
        public void run() {
            Connection sharedCon = null;
            try {
                sharedCon = dbConn.getConnection();
                Login_createnewuser helper = new Login_createnewuser(sharedCon);
                boolean exists = helper.searchuser(userInput, "acess");
                if (!exists) {
                    if (sharedCon != null) sharedCon.close();
                    JOptionPane.showMessageDialog(LoginApp.this, "User not found.");
                    return;
                }

                Password_checker checker = new Password_checker(password, sharedCon);
                boolean isCorrect = checker.verifier(userInput, "acess");
                if (isCorrect) {
                    JOptionPane.showMessageDialog(LoginApp.this, "✅ Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    final Connection finalCon = sharedCon;
                    final String loggedInUser = userInput;
                    SwingUtilities.invokeLater(() -> new HomePageGUI(finalCon, loggedInUser).setVisible(true));
                    LoginApp.this.dispose();
                } else {
                    if (sharedCon != null) sharedCon.close();
                    JOptionPane.showMessageDialog(LoginApp.this, "Incorrect password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(LoginApp.this, "Database Error: " + ex.getMessage());
                if (sharedCon != null) {
                    try { sharedCon.close(); } catch (SQLException closeEx) {}
                }
            }
        }
    }

    private void createNewUser() {
        try (Connection con = dbConn.getConnection()) {
            String username = usernameField.getText();
            String email = emailField.getText();
            String dob = dobField.getText();
            LocalDate dobDate = LocalDate.parse(dob);
            int age = Period.between(dobDate, LocalDate.now()).getYears();
            long phone = Long.parseLong(phoneField.getText());
            String newPassword = new String(passwordField.getPassword());

            Login_createnewuser userCreator = new Login_createnewuser(con);
            userCreator.create_newuser(username, email, age, dob, newPassword, phone);

            JOptionPane.showMessageDialog(this, "🎉 Account created successfully!");
            showLoginFields();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    class GradientPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new GradientPaint(0, 0, new Color(20, 25, 45), 0, getHeight(), new Color(60, 120, 220)));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginApp::new);
    }
}
