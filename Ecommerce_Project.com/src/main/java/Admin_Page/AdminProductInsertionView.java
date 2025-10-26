/*package Admin_Page;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import Admin_Login.c_user;
import Connection_db.Connection_db;

public class AdminProductInsertionView extends JFrame {

    private JTextField tfMail, tfUsername, tfPName, tfPDesc, tfPImg, tfPPrice, tfPDiscount, tfPStock;
    private JComboBox<String> cbCategory;
    private JButton btnInsert, btnClear;
    private Connection con;

    public AdminProductInsertionView(Connection con) {
        this.con = con;

        setTitle("Gadget Galaxy");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);

        JLabel title = new JLabel("Gadget Galaxy");
        title.setForeground(new Color(135, 206, 235));
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBounds(120, 20, 300, 40);
        add(title);

        int y = 80;
        int gap = 45;

        tfMail = addField("User Mail ID:", y); y += gap;
        tfUsername = addField("User Name:", y); y += gap;

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setForeground(Color.WHITE);
        lblCategory.setBounds(50, y, 150, 25);
        add(lblCategory);

        cbCategory = new JComboBox<>(new String[]{"Laptop", "Mobile", "Accessories"});
        cbCategory.setBounds(200, y, 200, 25);
        add(cbCategory);
        y += gap;

        tfPName = addField("Product Name:", y); y += gap;
        tfPDesc = addField("Product Desc:", y); y += gap;
        tfPImg = addField("Product Image URL:", y); y += gap;
        tfPPrice = addField("Product Price:", y); y += gap;
        tfPDiscount = addField("Discount %:", y); y += gap;
        tfPStock = addField("Stock Quantity:", y); y += gap;

        btnInsert = new JButton("Insert Product");
        btnInsert.setBounds(80, y + 10, 150, 35);
        add(btnInsert);

        btnClear = new JButton("Clear Fields");
        btnClear.setBounds(260, y + 10, 150, 35);
        add(btnClear);

        btnInsert.addActionListener(e -> handleInsert());
        btnClear.addActionListener(e -> clearFields());

        setVisible(true);
    }

    private JTextField addField(String label, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setBounds(50, y, 150, 25);
        add(lbl);
        JTextField tf = new JTextField();
        tf.setBounds(200, y, 200, 25);
        add(tf);
        return tf;
    }

    private void clearFields() {
        tfMail.setText("");
        tfUsername.setText("");
        cbCategory.setSelectedIndex(0);
        tfPName.setText("");
        tfPDesc.setText("");
        tfPImg.setText("");
        tfPPrice.setText("");
        tfPDiscount.setText("");
        tfPStock.setText("");
    }

    private void handleInsert() {
        try {
            String mail = tfMail.getText().trim();
            String username = tfUsername.getText().trim();
            String category = (String) cbCategory.getSelectedItem();
            String pname = tfPName.getText().trim();
            String pdesc = tfPDesc.getText().trim();
            String pimg = tfPImg.getText().trim();
            int price = Integer.parseInt(tfPPrice.getText().trim());
            int discount = Integer.parseInt(tfPDiscount.getText().trim());
            int stock = Integer.parseInt(tfPStock.getText().trim());
            int id = 0;
            if (category.equals("Laptop")) id = 12;
            else if (category.equals("Mobile")) id = 13;
            else if (category.equals("Accessories")) id = 14;
            c_user c2 = new c_user(con);
            int u_id = c2.user_id_retrival(mail);
            if (u_id == 0) {
                JOptionPane.showMessageDialog(this, "❌ User not found for the given Mail ID");
                return;
            }
            Insert_user_cat backend = new Insert_user_cat(con, mail, username, u_id);
            backend.insert_product(id, pname, pdesc, pimg, price, discount, stock);

            JOptionPane.showMessageDialog(this, "✅ Product inserted successfully!");
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Enter valid numbers for Price, Discount, Stock.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Database Error: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Connection con = new Connection_db(
                    "jdbc:postgresql://localhost:5432/Ecommerce",
                    "postgres",
                    "Achu2006$"
            ).getConnection();

            SwingUtilities.invokeLater(() -> new AdminProductInsertionView(con));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "DB Connection Failed: " + e.getMessage());
        }
    }
}*/

package Admin_Page;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import Admin_Login.c_user;
import Connection_db.Connection_db;

public class AdminProductInsertionView extends JFrame {

    private JTextField tfPName, tfPDesc, tfPImg, tfPPrice, tfPDiscount, tfPStock;
    private JComboBox<String> cbCategory;
    private JButton btnInsert, btnClear;
    private Connection con;

    private String loggedInMail;
    private int loggedInUserId;

    public AdminProductInsertionView(Connection con, String mail, int userId) {
        this.con = con;
        this.loggedInMail = mail;
        this.loggedInUserId = userId;

        setTitle("Gadget Galaxy - Insert Product");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK);
        setLayout(null);

        JLabel title = new JLabel("Insert New Product");
        title.setForeground(new Color(135, 206, 235));
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBounds(100, 20, 300, 40);
        add(title);

        JLabel lblInsertingAs = new JLabel("Inserting as: " + loggedInMail + " (ID: " + loggedInUserId + ")");
        lblInsertingAs.setForeground(Color.YELLOW);
        lblInsertingAs.setBounds(50, 65, 350, 25);
        add(lblInsertingAs);

        int y = 100;
        int gap = 45;

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setForeground(Color.WHITE);
        lblCategory.setBounds(50, y, 150, 25);
        add(lblCategory);

        cbCategory = new JComboBox<>(new String[]{"Laptop", "Mobile", "Accessories"});
        cbCategory.setBounds(200, y, 200, 25);
        add(cbCategory);
        y += gap;

        tfPName = addField("Product Name:", y); y += gap;
        tfPDesc = addField("Product Desc:", y); y += gap;
        tfPImg = addField("Product Image URL:", y); y += gap;
        tfPPrice = addField("Product Price:", y); y += gap;
        tfPDiscount = addField("Discount %:", y); y += gap;
        tfPStock = addField("Stock Quantity:", y); y += gap;

        btnInsert = new JButton("Insert Product");
        btnInsert.setBounds(80, y + 10, 150, 35);
        add(btnInsert);

        btnClear = new JButton("Clear Fields");
        btnClear.setBounds(260, y + 10, 150, 35);
        add(btnClear);

        btnInsert.addActionListener(e -> handleInsert());
        btnClear.addActionListener(e -> clearFields());

        setVisible(true);
    }

    private JTextField addField(String label, int y) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Color.WHITE);
        lbl.setBounds(50, y, 150, 25);
        add(lbl);

        JTextField tf = new JTextField();
        tf.setBounds(200, y, 200, 25);
        tf.setForeground(Color.WHITE);
        tf.setBackground(new Color(60, 60, 60));
        tf.setCaretColor(Color.WHITE);
        add(tf);

        return tf;
    }

    private void clearFields() {
        cbCategory.setSelectedIndex(0);
        tfPName.setText("");
        tfPDesc.setText("");
        tfPImg.setText("");
        tfPPrice.setText("");
        tfPDiscount.setText("");
        tfPStock.setText("");
    }

    private void handleInsert() {
        try {
            String category = (String) cbCategory.getSelectedItem();
            String pname = tfPName.getText().trim();
            String pdesc = tfPDesc.getText().trim();
            String pimg = tfPImg.getText().trim();
            int price = Integer.parseInt(tfPPrice.getText().trim());
            int discount = Integer.parseInt(tfPDiscount.getText().trim());
            int stock = Integer.parseInt(tfPStock.getText().trim());
            int id = 0;
            if (category.equals("Laptop")) id = 12;
            else if (category.equals("Mobile")) id = 13;
            else if (category.equals("Accessories")) id = 14;

            int u_id = this.loggedInUserId;
            String mail = this.loggedInMail;
            String username = "";

            if (u_id <= 0) {
                JOptionPane.showMessageDialog(this, "❌ User ID is invalid. Please log in again.");
                return;
            }

            Insert_user_cat backend = new Insert_user_cat(con, mail, username, u_id);
            backend.insert_product(id, pname, pdesc, pimg, price, discount, stock);

            JOptionPane.showMessageDialog(this, "✅ Product inserted successfully! Click Refresh Products on the dashboard.");
            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Enter valid numbers for Price, Discount, Stock.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Database Error: " + ex.getMessage());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Connection con = new Connection_db(
                    "jdbc:postgresql://localhost:5432/Ecommerce",
                    "postgres",
                    "Achu2006$"
            ).getConnection();

            SwingUtilities.invokeLater(() -> new AdminProductInsertionView(con, "test@mail.com", 1));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "DB Connection Failed: " + e.getMessage());
        }
    }
}
