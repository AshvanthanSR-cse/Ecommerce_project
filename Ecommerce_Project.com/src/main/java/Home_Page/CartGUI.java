package Home_Page;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class CartGUI extends JFrame {

    private Cart cartManager;
    private String loggedInUserMailId;
    private JPanel cartItemsPanel;
    private JLabel totalLabel;
    
    public static class CartItemModel {
        private final int productId;
        private final String name;
        private final int price;
        private int quantity;

        public CartItemModel(int productId, String name, int price, int quantity) {
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public int getProductId() { return productId; }
        public String getName() { return name; }
        public int getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public CartGUI(String loggedInUserMailId) {
        this.loggedInUserMailId = loggedInUserMailId;
        
        try {
            this.cartManager = new Cart(); 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error initializing cart: Database connection failed.",
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return; 
        }

        setTitle("Gadget Galaxy - Your Cart: " + loggedInUserMailId); 
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(750, 600);
        setMinimumSize(new Dimension(650, 500));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        initializeHeader();
        
        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        initializeFooter();

        loadCartItems();
    }

    private void initializeHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(20, 25, 45));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel title = new JLabel("Shopping Cart", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        headerPanel.add(title, BorderLayout.WEST);
        
        add(headerPanel, BorderLayout.NORTH);
    }

    private void initializeFooter() {
        JPanel footerPanel = new JPanel(new BorderLayout(20, 0));
        footerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        footerPanel.setBackground(new Color(230, 230, 230));

        totalLabel = new JLabel("Total: ₹ 0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalLabel.setForeground(new Color(0, 128, 0));
        
        JButton checkoutButton = new JButton("Proceed to Checkout");
        checkoutButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        checkoutButton.setBackground(new Color(255, 165, 0));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFocusPainted(false);
        checkoutButton.addActionListener(e -> proceedToCheckout());

        footerPanel.add(totalLabel, BorderLayout.WEST);
        footerPanel.add(checkoutButton, BorderLayout.EAST);
        
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private void loadCartItems() {
        cartItemsPanel.removeAll();
        double grandTotal = 0;
        
        List<CartItemModel> items = fetchCartItemsFromDatabase();

        if (items.isEmpty()) {
            JLabel emptyLabel = new JLabel("Your cart is empty!", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            cartItemsPanel.add(Box.createVerticalStrut(150));
            cartItemsPanel.add(emptyLabel);
        } else {
            for (CartItemModel item : items) {
                JPanel itemCard = createCartItemCard(item);
                itemCard.setAlignmentX(Component.CENTER_ALIGNMENT); 
                cartItemsPanel.add(itemCard);
                cartItemsPanel.add(Box.createVerticalStrut(10));
                grandTotal += (double)item.getPrice() * item.getQuantity();
            }
        }
        
        totalLabel.setText(String.format("Total: ₹ %,.2f", grandTotal));
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }
    
    private List<CartItemModel> fetchCartItemsFromDatabase() {
        try {
           
            return cartManager.getCartItems(loggedInUserMailId);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching cart items: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    private JPanel createCartItemCard(CartItemModel item) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(650, 80)); 
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Color.WHITE);
        JLabel nameLabel = new JLabel(item.getName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel priceLabel = new JLabel(String.format("Price: ₹ %,d", item.getPrice()));
        priceLabel.setForeground(new Color(34, 139, 34)); 
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        card.add(infoPanel, BorderLayout.WEST);
        
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        quantityPanel.setBackground(Color.WHITE);
        
        SpinnerModel spinnerModel = new SpinnerNumberModel(item.getQuantity(), 1, 99, 1);
        JSpinner quantitySpinner = new JSpinner(spinnerModel);
        quantitySpinner.setPreferredSize(new Dimension(60, 30));
        
        quantitySpinner.addChangeListener(e -> {
            int newQuantity = (Integer) quantitySpinner.getValue();
            item.setQuantity(newQuantity);
            updateItemQuantityInDB(item.getProductId(), newQuantity);
            loadCartItems();
        });
        
        JLabel qtyText = new JLabel("Qty:");
        qtyText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        quantityPanel.add(qtyText);
        quantityPanel.add(quantitySpinner);
        card.add(quantityPanel, BorderLayout.CENTER);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        actionPanel.setBackground(Color.WHITE);
        
        JLabel subtotalLabel = new JLabel(String.format("Subtotal: ₹ %,d", item.getPrice() * item.getQuantity()));
        subtotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subtotalLabel.setPreferredSize(new Dimension(150, 20));

        JButton removeButton = new JButton("Remove");
        removeButton.setBackground(new Color(220, 20, 60)); 
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        
        removeButton.addActionListener(e -> {
            removeItemFromCart(item.getProductId());
        });

        actionPanel.add(subtotalLabel);
        actionPanel.add(removeButton);
        card.add(actionPanel, BorderLayout.EAST);

        return card;
    }

    private void updateItemQuantityInDB(int productId, int quantity) {
        try {
            cartManager.updateQuantity(loggedInUserMailId, productId, quantity); 
            
            System.out.println("ACTION: Attempting to update product " + productId + " quantity to " + quantity + " for user " + loggedInUserMailId);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating quantity: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removeItemFromCart(int productId) {
        try {
            cartManager.removeFromCart(loggedInUserMailId, productId);
            
            loadCartItems(); 
            JOptionPane.showMessageDialog(this, "Item removed from cart.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error removing item: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void proceedToCheckout() {
        List<CartItemModel> items = fetchCartItemsFromDatabase();
        double grandTotal = 0;

        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty. Please add items before checking out.", "Checkout Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (CartItemModel item : items) {
            grandTotal += (double)item.getPrice() * item.getQuantity();
        }

        try {
            O_SummaryGUI summaryWindow = new O_SummaryGUI(loggedInUserMailId, items, grandTotal);
            summaryWindow.setVisible(true);
            this.dispose(); 
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error preparing checkout: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}