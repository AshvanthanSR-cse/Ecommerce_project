package Home_Page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import Connection_db.Connection_db;

public class PaymentGatewayGUI extends JFrame {

    private final Connection con; 
    private final int userId; 
    private double finalPrice = 0.0;
    private O_summary orderManager;

    private JLabel priceLabel;
    private ButtonGroup paymentGroup;
    private JRadioButton cardRadio, gpayRadio, codRadio;
    private JButton payButton;

    public PaymentGatewayGUI(Connection con, int userId) {
        this.con = con;
        this.userId = userId;
        
        try {
            this.orderManager = new O_summary();
            
            finalPrice = orderManager.calculateOrderTotal(userId);
            
            if (finalPrice <= 0.0) {
                 JOptionPane.showMessageDialog(this, "Order total is zero. Cannot proceed.", 
                                          "Error", JOptionPane.ERROR_MESSAGE);
                 this.dispose();
                 return;
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching order total: " + e.getMessage(), 
                                          "Database Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
            return;
        }

        setupGUI();
        setVisible(true);
    }

    private void setupGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(25, 25, 35));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setOpaque(false);
        
        JLabel title = new JLabel("Secure Checkout", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(173, 216, 230));
        headerPanel.add(title);
        
        priceLabel = new JLabel(String.format("User ID #%d | Final Total: ₹ %.2f", userId, finalPrice), SwingConstants.CENTER);
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        priceLabel.setForeground(new Color(144, 238, 144)); 
        headerPanel.add(priceLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 150)), 
            "Select Payment Method", 
            TitledBorder.LEFT, TitledBorder.TOP, 
            new Font("Arial", Font.BOLD, 14), new Color(200, 200, 200)));
        optionsPanel.setBackground(new Color(40, 40, 50));
        
        paymentGroup = new ButtonGroup();
        
        cardRadio = createRadioButton("Credit/Debit Card", "CARD");
        gpayRadio = createRadioButton("Google Pay (GPay)", "GPAY");
        codRadio = createRadioButton("Cash On Delivery (COD)", "COD");
        
        optionsPanel.add(cardRadio);
        optionsPanel.add(gpayRadio);
        optionsPanel.add(codRadio);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);

        payButton = new JButton("Pay Now");
        payButton.setFont(new Font("Arial", Font.BOLD, 18));
        payButton.setBackground(new Color(50, 205, 50)); 
        payButton.setForeground(Color.WHITE);
        payButton.setFocusPainted(false);
        payButton.addActionListener(e -> handlePayment());
        
        mainPanel.add(payButton, BorderLayout.SOUTH);

        add(mainPanel);
    }
    
    private JRadioButton createRadioButton(String text, String command) {
        JRadioButton button = new JRadioButton(text);
        button.setActionCommand(command);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(40, 40, 50));
        paymentGroup.add(button);
        
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(5, 10, 5, 10));
        wrapper.add(button);
        return button;
    }

    private void handlePayment() {
        if (paymentGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Please select a payment method.", 
                                          "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String method = paymentGroup.getSelection().getActionCommand();
        String message;

        try {
            orderManager.clearUserCart(userId);
            if (method.equals("COD")) {
                message = String.format("Order placed! Payment of ₹ %.2f will be collected upon delivery. Your cart is now empty.", 
                                         finalPrice);
            } else {
                message = String.format("Payment of ₹ %.2f successful via %s! Thank you for your purchase. Your cart is now empty.", 
                                         finalPrice, method);
            }
            
            JOptionPane.showMessageDialog(this, message, "Payment Complete", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Payment confirmed, but failed to clear your cart. Please contact support. Error: " + e.getMessage(), 
                                          "System Warning", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }
}