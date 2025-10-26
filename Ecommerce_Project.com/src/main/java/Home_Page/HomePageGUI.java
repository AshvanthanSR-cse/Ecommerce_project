package Home_Page;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class HomePageGUI extends JFrame {
    private final int PAGE_SIZE = 10;
    private int currentPage = 0;
    private String currentSearchKeyword = "";
    private ProductCatalogManager catalogManager;
    private JTextField searchField;
    private JButton searchButton, prevButton, nextButton;
    private JPanel productPanel;
    private JLabel pageLabel;	
    private Cart cart;	
    private JButton cartButton; 
    
    private String loggedInUser;

    public HomePageGUI(Connection con, String loggedInUser) {
        this.catalogManager = new ProductCatalogManager(con);
        this.loggedInUser = loggedInUser;
        try {
            this.cart = new Cart();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,	
                "Error initializing cart system. Database connection failed: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        setTitle("Gadget Galaxy - Home");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(new Color(240, 245, 250));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBorder(new EmptyBorder(20, 30, 10, 30));
        topPanel.setBackground(new Color(20, 25, 45));
        JLabel title = new JLabel("Gadget Galaxy", SwingConstants.LEFT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        searchField.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(new Color(100, 150, 255));
        searchButton.setFocusPainted(false);
        cartButton = new JButton("View Cart");
        cartButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cartButton.setForeground(Color.WHITE);
        cartButton.setBackground(new Color(255, 165, 0));
        cartButton.setFocusPainted(false);
        cartButton.setPreferredSize(new Dimension(150, 40)); 

        JPanel searchBarPanel = new JPanel(new BorderLayout());
        searchBarPanel.setBackground(new Color(20, 25, 45));
        searchBarPanel.add(searchField, BorderLayout.CENTER);
        searchBarPanel.add(searchButton, BorderLayout.EAST);
        JPanel centerHeaderPanel = new JPanel(new BorderLayout(20, 0));
        centerHeaderPanel.setBackground(new Color(20, 25, 45));
        centerHeaderPanel.add(searchBarPanel, BorderLayout.CENTER);
        centerHeaderPanel.add(cartButton, BorderLayout.EAST);


        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(centerHeaderPanel, BorderLayout.CENTER); 
        add(topPanel, BorderLayout.NORTH);

        searchButton.addActionListener(e -> {
            currentSearchKeyword = searchField.getText().trim();
            currentPage = 0;
            loadProducts();
        });
        searchField.addActionListener(e -> {
            currentSearchKeyword = searchField.getText().trim();
            currentPage = 0;
            loadProducts();
        });
        cartButton.addActionListener(e -> {
            if (cart != null) {
                CartGUI cartWindow = new CartGUI(loggedInUser);
                cartWindow.setVisible(true);
            } else {
                 JOptionPane.showMessageDialog(this, "Cannot open cart. Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(0, 2, 20, 20));
        productPanel.setBackground(new Color(240, 245, 250));
        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.setBorder(new EmptyBorder(20, 30, 20, 30));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        bottomPanel.setBackground(new Color(240, 245, 250));
        prevButton = new JButton("Previous");
        prevButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        prevButton.setBackground(new Color(220, 220, 220));
        prevButton.setFocusPainted(false);
        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nextButton.setBackground(new Color(220, 220, 220));
        nextButton.setFocusPainted(false);
        pageLabel = new JLabel("Page 1");
        pageLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bottomPanel.add(prevButton);
        bottomPanel.add(pageLabel);
        bottomPanel.add(nextButton);
        add(bottomPanel, BorderLayout.SOUTH);

        prevButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                loadProducts();
            }
        });
        nextButton.addActionListener(e -> {
            currentPage++;
            loadProducts();
        });

        loadProducts();
    }

    private void loadProducts() {
        try {
            int offset = currentPage * PAGE_SIZE;
            List<ProductModel> products = catalogManager.getProducts(currentSearchKeyword, offset, PAGE_SIZE);
            int total = catalogManager.countProducts(currentSearchKeyword);
            productPanel.removeAll();

            if (products.isEmpty()) {
                JLabel noData = new JLabel("No products found.", SwingConstants.CENTER);
                noData.setFont(new Font("Segoe UI", Font.BOLD, 20));
                noData.setForeground(Color.GRAY);
                productPanel.add(noData);
            } else {
                for (ProductModel product : products)
                    productPanel.add(createProductCard(product));
            }

            productPanel.revalidate();
            productPanel.repaint();
            prevButton.setEnabled(currentPage > 0);
            nextButton.setEnabled((offset + PAGE_SIZE) < total);
            pageLabel.setText("Page " + (currentPage + 1));

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage());
        }
    }

    private JPanel createProductCard(ProductModel product) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(10, 10, 10, 10))
        );

        JLabel imageLabel;
        try {
            ImageIcon icon = new ImageIcon(product.getImageUrl());
            Image img = icon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel = new JLabel("No Image", SwingConstants.CENTER);
        }
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel(product.getProductName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(new Color(30, 30, 30));
        JLabel descLabel = new JLabel("<html><p style='width:300px;'>" + product.getDescription() + "</p></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(80, 80, 80));
        JLabel priceLabel = new JLabel("₹ " + product.getPrice());
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(new Color(0, 128, 0));

        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setBackground(new Color(100, 200, 100));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setFocusPainted(false);
        addToCartButton.addActionListener(e -> {
            if (cart != null) {
                cart.addToCart(loggedInUser, product.getProductName(), 1);
                JOptionPane.showMessageDialog(this, product.getProductName() + " added to cart!");
            } else {
                JOptionPane.showMessageDialog(this, "Cart system is unavailable due to a database error.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(descLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(priceLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(addToCartButton);

        card.add(imageLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }
}
