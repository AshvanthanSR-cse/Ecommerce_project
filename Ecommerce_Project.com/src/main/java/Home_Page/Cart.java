package Home_Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Connection_db.Connection_db;

public class Cart {

    private Connection con;

    public Cart() throws SQLException {
        this.con = Connection_db.getConnection();
    }

    private int getProductId(String productName) throws SQLException {
        String query = "SELECT product_id FROM product_cat WHERE product_name = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, productName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("product_id");
                else throw new SQLException("Product not found: " + productName);
            }
        }
    }
    
    private int getCartId(String mailId) throws SQLException { 
        String query = "SELECT un_key FROM acess WHERE mail_id = ?"; 
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, mailId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("un_key");
                else throw new SQLException("Cart ID (un_key) not found for mail_id: " + mailId);
            }
        }
    }

    private int getProductPrice(String productName) throws SQLException {
        String query = "SELECT product_price FROM product_cat WHERE product_name = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, productName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("product_price");
                else throw new SQLException("Product price not found: " + productName);
            }
        }
    }

    public void addToCart(String mailId, String productName, int quantity) {
        try {
            int produ_id = getProductId(productName);
            int cart_id = getCartId(mailId); 
            int productPrice = getProductPrice(productName);
            
            String query = "INSERT INTO cart (produ_id, cart_id, produ_name, produ_price, produ_quantity) VALUES (?, ?, ?, ?, ?) ON CONFLICT (produ_id, cart_id) DO UPDATE SET produ_quantity = cart.produ_quantity + EXCLUDED.produ_quantity;";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, produ_id);
                ps.setInt(2, cart_id);
                ps.setString(3, productName);
                ps.setInt(4, productPrice);
                ps.setInt(5, quantity);
                
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("SUCCESS");
                } else {
                    System.out.println("Insert failed for ");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL ERROR" + e.getMessage());
        }
    }
    
    public void removeFromCart(String mailId, int productId) {
        try {
            int cart_id = getCartId(mailId);
            String query = "DELETE FROM cart WHERE cart_id = ? AND produ_id = ?";
            
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, cart_id);
                ps.setInt(2, productId);
                
                int rowsAffected = ps.executeUpdate();
                
                if (rowsAffected > 0) {
                    System.out.println("Product Inserted"); 
                } else {
                    System.out.println("Product Not Inserted");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL ERROR" + e.getMessage());
        }
    }

    public void updateQuantity(String mailId, int productId, int quantity) {
        try {
            int cart_id = getCartId(mailId);
            
            String query = "UPDATE cart SET produ_quantity = ? WHERE cart_id = ? AND produ_id = ?";
            
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, quantity);
                ps.setInt(2, cart_id);
                ps.setInt(3, productId);
                
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("SQL ERROR in updateQuantity: " + e.getMessage());
        }
    }
    public List<Home_Page.CartGUI.CartItemModel> getCartItems(String mailId) throws SQLException {
        List<Home_Page.CartGUI.CartItemModel> items = new ArrayList<>();
        int cart_id = getCartId(mailId);

        String query = "SELECT c.produ_id, c.produ_name, c.produ_quantity, p.product_price FROM cart c JOIN product_cat p ON c.produ_id = p.product_id WHERE c.cart_id = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, cart_id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new Home_Page.CartGUI.CartItemModel(
                        rs.getInt("produ_id"),
                        rs.getString("produ_name"),
                        rs.getInt("product_price"),
                        rs.getInt("produ_quantity")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL ERROR " + e.getMessage());
            throw e;
        }
        return items;
    }
}