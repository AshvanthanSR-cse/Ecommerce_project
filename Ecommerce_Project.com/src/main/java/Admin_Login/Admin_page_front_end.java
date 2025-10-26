package Admin_Login;

import User_Login.Password_checker;
import User_Login.Login_createnewuser;
import Connection_db.Connection_db;
import Admin_Page.AdminProductInsertionView;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Admin_page_front_end extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTextField loginUserOrMail;
    private JPasswordField loginPassword;
    private JTextField createUsername;
    private JTextField createMail;
    private JPasswordField createPassword;
    private JPasswordField createConfirmPassword;
    private JTextField createAadhar;
    private JTable productTable;
    private String loggedInMail;
    private int loggedInUserId;

    private Connection con;

    public Admin_page_front_end() {
        try {
            String url = "jdbc:postgresql://localhost:5432/Ecommerce";
            String name = "postgres";
            String password = "Achu2006$";
            Connection_db c1 = new Connection_db(url, name, password);
            con = c1.getConnection();
            if (con != null) {
                System.out.println("✅ Connection Created");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Connection Failed: " + e.getMessage());
        }

        setTitle("Gadget Galaxy - Admin");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createCreateUserPanel(), "create");
        mainPanel.add(createDashboardPanel(), "dashboard");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(45, 45, 45));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Admin Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(new Color(0, 191, 255));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel userLabel = new JLabel("Mail ID / Username:");
        userLabel.setForeground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        panel.add(userLabel, gbc);

        loginUserOrMail = new JTextField(20);
        styleTextField(loginUserOrMail);
        gbc.gridx = 1;
        panel.add(loginUserOrMail, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.LIGHT_GRAY);
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(passLabel, gbc);

        loginPassword = new JPasswordField(20);
        styleTextField(loginPassword);
        gbc.gridx = 1;
        panel.add(loginPassword, gbc);

        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        JButton switchToCreateButton = new JButton("Create New Account");
        styleButton(switchToCreateButton);
        gbc.gridy = 4;
        panel.add(switchToCreateButton, gbc);

        loginButton.addActionListener(e -> handleLogin());
        switchToCreateButton.addActionListener(e -> cardLayout.show(mainPanel, "create"));

        return panel;
    }

    private JPanel createCreateUserPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(45, 45, 45));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create Admin Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(new Color(0, 191, 255));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.LIGHT_GRAY);
        gbc.gridx = 0;
        panel.add(usernameLabel, gbc);

        createUsername = new JTextField(20);
        styleTextField(createUsername);
        gbc.gridx = 1;
        panel.add(createUsername, gbc);

        JLabel mailLabel = new JLabel("Mail ID:");
        mailLabel.setForeground(Color.LIGHT_GRAY);
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(mailLabel, gbc);

        createMail = new JTextField(20);
        styleTextField(createMail);
        gbc.gridx = 1;
        panel.add(createMail, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.LIGHT_GRAY);
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(passLabel, gbc);

        createPassword = new JPasswordField(20);
        styleTextField(createPassword);
        gbc.gridx = 1;
        panel.add(createPassword, gbc);

        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setForeground(Color.LIGHT_GRAY);
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(confirmLabel, gbc);

        createConfirmPassword = new JPasswordField(20);
        styleTextField(createConfirmPassword);
        gbc.gridx = 1;
        panel.add(createConfirmPassword, gbc);

        JLabel aadharLabel = new JLabel("Aadhar Number:");
        aadharLabel.setForeground(Color.LIGHT_GRAY);
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(aadharLabel, gbc);

        createAadhar = new JTextField(20);
        styleTextField(createAadhar);
        gbc.gridx = 1;
        panel.add(createAadhar, gbc);

        JButton createButton = new JButton("Create Admin");
        styleButton(createButton);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(createButton, gbc);

        JButton switchToLoginButton = new JButton("Back to Login");
        styleButton(switchToLoginButton);
        gbc.gridy = 7;
        panel.add(switchToLoginButton, gbc);

        createButton.addActionListener(e -> handleCreateUser());
        switchToLoginButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(45, 45, 45));

        JLabel title = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(0, 191, 255));
        panel.add(title, BorderLayout.NORTH);

        productTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(45, 45, 45));
        JButton insertBtn = new JButton("Insert Product");
        JButton viewBtn = new JButton("Refresh Products");
        JButton logoutBtn = new JButton("Logout");

        styleButton(insertBtn);
        styleButton(viewBtn);
        styleButton(logoutBtn);

        bottomPanel.add(insertBtn);
        bottomPanel.add(viewBtn);
        bottomPanel.add(logoutBtn);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        insertBtn.addActionListener(e -> new AdminProductInsertionView(con, loggedInMail, loggedInUserId));
        viewBtn.addActionListener(e -> loadProducts());
        logoutBtn.addActionListener(e -> {
            loggedInMail = null;
            loggedInUserId = -1;
            cardLayout.show(mainPanel, "login");
        });

        return panel;
    }

    private void handleLogin() {
        String userOrMail = loginUserOrMail.getText().trim();
        String password = new String(loginPassword.getPassword());

        if (userOrMail.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try {
            Login_createnewuser l1 = new Login_createnewuser(con);
            boolean userExists = l1.searchuser(userOrMail, "admin_page");

            if (userExists) {
                Password_checker p1 = new Password_checker(password, con);
                boolean passwordOk = p1.verifier(userOrMail, "admin_page");
                if (passwordOk) {
                    loggedInMail = userOrMail;
                    c_user c1 = new c_user(con);
                    loggedInUserId = c1.user_id_retrival(userOrMail);
                    JOptionPane.showMessageDialog(this, "✅ Login Successful!");
                    loadProducts();
                    cardLayout.show(mainPanel, "dashboard");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Incorrect Password!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "❌ No User Found. Create a new account.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    private void handleCreateUser() {
        String username = createUsername.getText().trim();
        String mail = createMail.getText().trim();
        String password = new String(createPassword.getPassword());
        String confirmPassword = new String(createConfirmPassword.getPassword());
        String aadhar = createAadhar.getText().trim();

        if (username.isEmpty() || mail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || aadhar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!");
            return;
        }

        long aadharNo;
        try {
            aadharNo = Long.parseLong(aadhar);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Aadhar Number!");
            return;
        }

        try {
            c_user c2 = new c_user(con);
            c2.create_newuser(username, mail, password, aadharNo);
            JOptionPane.showMessageDialog(this, "✅ New Admin Created Successfully!");
            cardLayout.show(mainPanel, "login");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }
    }

    private void loadProducts() {
        try {
            Admin_prod_view view = new Admin_prod_view(con, loggedInMail);
            String query = "SELECT product_id, product_name, product_desc, product_price, product_count FROM product_cat WHERE a_user_key = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, loggedInUserId);
            ResultSet rs = ps.executeQuery();

            Vector<String> columnNames = new Vector<>();
            columnNames.add("ID");
            columnNames.add("Name");
            columnNames.add("Description");
            columnNames.add("Price");
            columnNames.add("Stock");

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("product_id"));
                row.add(rs.getString("product_name"));
                row.add(rs.getString("product_desc"));
                row.add(rs.getDouble("product_price"));
                row.add(rs.getInt("product_count"));
                data.add(row);
            }

            productTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
            rs.close();
            ps.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "❌ Failed to load products: " + e.getMessage());
        }
    }

    private void styleTextField(JTextField field) {
        field.setBackground(new Color(60, 60, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Admin_page_front_end().setVisible(true));
    }
}
