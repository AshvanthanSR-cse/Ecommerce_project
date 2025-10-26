package Home_Page;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import Connection_db.Connection_db;

public class O_SummaryGUI extends JFrame {

    private final String loggedInUserMailId;
    private final List<CartGUI.CartItemModel> cartItems;
    private final double grandTotal;
    private final O_summary orderManager;
    private JTextArea addressArea;
    private JButton confirmButton;
    private JButton paymentButton;

    public O_SummaryGUI(String loggedInUserMailId, List<CartGUI.CartItemModel> cartItems, double grandTotal) throws SQLException {
        this.loggedInUserMailId = loggedInUserMailId;
        this.cartItems = cartItems;
        this.grandTotal = grandTotal;
        this.orderManager = new O_summary(); 

        setTitle("Order Summary");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(550, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        initializeHeader();
        initializeItemsPanel();
        initializeInputAndFooter();
        
        paymentButton.setVisible(false); 
    }

    private void initializeHeader() {
        JLabel title = new JLabel("Confirm Order", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(20, 25, 45));
        add(title, BorderLayout.NORTH);
    }

    private void initializeItemsPanel() {
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(Color.WHITE);
        itemsPanel.setBorder(BorderFactory.createTitledBorder("Items Summary"));

        for (CartGUI.CartItemModel item : cartItems) {
            JLabel itemLabel = new JLabel(
                String.format("%s (x%d) - ₹ %,d", 
                    item.getName(), 
                    item.getQuantity(), 
                    item.getPrice() * item.getQuantity()
                )
            );
            itemLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
            itemsPanel.add(itemLabel);
        }

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initializeInputAndFooter() {
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        bottomPanel.setBackground(new Color(245, 245, 245));

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Delivery Address (Max 500 chars)"));
        addressArea = new JTextArea(4, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScrollPane = new JScrollPane(addressArea);
        inputPanel.add(addressScrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel totalLabel = new JLabel(String.format("Grand Total: ₹ %,.2f", grandTotal));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        totalLabel.setForeground(new Color(0, 128, 0));
        
        confirmButton = new JButton("Place Order");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        confirmButton.setBackground(new Color(20, 25, 45));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(e -> placeOrder());
        
        paymentButton = new JButton("Proceed to Payment");
        paymentButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        paymentButton.setBackground(new Color(0, 150, 0)); 
        paymentButton.setForeground(Color.WHITE);
        paymentButton.setFocusPainted(false);
        paymentButton.addActionListener(e -> redirectToPayment());

        actionPanel.add(totalLabel);
        actionPanel.add(Box.createHorizontalStrut(20));
        actionPanel.add(confirmButton);
        actionPanel.add(paymentButton);

        bottomPanel.add(inputPanel, BorderLayout.NORTH);
        bottomPanel.add(actionPanel, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void placeOrder() {
        String address = addressArea.getText().trim();
        if (address.isEmpty() || address.length() > 500) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid delivery address (up to 500 characters).", 
                "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            String.format("Confirm order details and proceed to storage? (Total: ₹ %,.2f)", grandTotal),
            "Confirm Order", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                orderManager.processOrder(loggedInUserMailId, address, cartItems); 
                
                JOptionPane.showMessageDialog(this, "Order details successfully saved. Proceeding to payment confirmation.", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                confirmButton.setEnabled(false);
                
                Connection dbCon = Connection_db.getConnection(); 
                int userCartId = orderManager.getCartId(loggedInUserMailId);
                
                PaymentGatewayGUI paymentWindow = new PaymentGatewayGUI(dbCon, userCartId);
                paymentWindow.setVisible(true);
                
                this.dispose(); 
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Database Error while processing order: " + e.getMessage() + ". Order rollbacked.", 
                    "Order Failed", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private void redirectToPayment() {
        JOptionPane.showMessageDialog(this, 
            "Redirecting to secure payment gateway. Payment process successfully initiated!", 
            "Payment Gateway", JOptionPane.INFORMATION_MESSAGE);
            
        this.dispose();
    }
}