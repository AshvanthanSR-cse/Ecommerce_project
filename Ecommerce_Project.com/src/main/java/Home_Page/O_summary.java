package Home_Page;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Home_Page.CartGUI.CartItemModel;
import Connection_db.Connection_db;

public class O_summary {

    private Connection con;

    public O_summary() throws SQLException {
        this.con = Connection_db.getConnection();
    }

    public int getCartId(String mailId) throws SQLException { 
        String query = "SELECT un_key FROM acess WHERE mail_id = ?"; 
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, mailId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("un_key");
                else throw new SQLException("Cart ID (un_key) not found for mail_id: " + mailId);
            }
        }
    }
    
    public void processOrder(String mailId, String address, List<CartItemModel> items) throws SQLException {
        Connection connection = this.con;
        
        int cart_id = getCartId(mailId);
        
        boolean autoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);

        try {
            String maxOidQuery = "SELECT COALESCE(MAX(o_id), 0) FROM order_sum";
            int nextOid;
            try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(maxOidQuery)) {
                rs.next();
                nextOid = rs.getInt(1) + 1;
            }

            String insertOrderQuery = "INSERT INTO order_sum (o_id, o_cart_id, price, p_name, address, user_mail_id) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psOrder = connection.prepareStatement(insertOrderQuery)) {
                for (CartItemModel item : items) {
                    psOrder.setInt(1, nextOid++); 
                    psOrder.setInt(2, cart_id);
                    psOrder.setInt(3, item.getPrice() * item.getQuantity()); 
                    psOrder.setString(4, item.getName() + " (x" + item.getQuantity() + ")"); 
                    psOrder.setString(5, address);
                    psOrder.setString(6, mailId); 
                    psOrder.addBatch();
                }
                psOrder.executeBatch();
            }

            connection.commit();
            System.out.println("SUCCESS: Order details saved.");

        } catch (SQLException e) {
            connection.rollback();
            System.err.println("TRANSACTION FAILED. Rolled back changes.");
            throw e;
        } finally {
            connection.setAutoCommit(autoCommit);
        }
    }

    public void clearUserCart(int cart_id) throws SQLException {
        String clearCartQuery = "DELETE FROM cart WHERE cart_id = ?";
        try (PreparedStatement psClearCart = con.prepareStatement(clearCartQuery)) {
            psClearCart.setInt(1, cart_id);
            psClearCart.executeUpdate();
            System.out.println("SUCCESS: Cart cleared for user " + cart_id);
        } catch (SQLException e) {
            System.err.println("Error clearing cart after payment: " + e.getMessage());
            throw e;
        }
    }
    public double calculateOrderTotal(int o_cart_id) throws SQLException {
        String query = "SELECT SUM(price) AS total FROM order_sum WHERE o_cart_id = ?";
        
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, o_cart_id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        }
        return 0.0;
    }
}