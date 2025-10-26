package Home_Page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ProductCatalogManager {
    private Connection con;

    public ProductCatalogManager(Connection con) {
        this.con = con;
    }

    public Connection getConnection() {
        return this.con;
    }

    public List<ProductModel> getProducts(String searchKeyword, int offset, int limit) throws SQLException {
        List<ProductModel> products = new ArrayList<>();
        String likeKeyword = "%" + searchKeyword.toLowerCase() + "%";

        String sql = "SELECT product_id, product_name, product_desc, product_price, product_img " +
                     "FROM product_cat " +
                     "WHERE LOWER(product_name) LIKE ? OR LOWER(product_desc) LIKE ? " +
                     "ORDER BY product_id " +
                     "LIMIT ? OFFSET ?";

        if (con == null || con.isClosed()) {
            System.err.println("Database connection is not available.");
            return products;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, likeKeyword);
            ps.setString(2, likeKeyword);
            ps.setInt(3, limit);
            ps.setInt(4, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("product_id");
                    String name = rs.getString("product_name");
                    String description = rs.getString("product_desc");
                    BigDecimal price = new BigDecimal(rs.getInt("product_price"));
                    String imageUrl = rs.getString("product_img");

                    products.add(new ProductModel(id, name, description, price, imageUrl));
                }
            }
        }
        return products;
    }

    public int countProducts(String searchKeyword) throws SQLException {
        String likeKeyword = "%" + searchKeyword.toLowerCase() + "%";
        String sql = "SELECT COUNT(*) FROM product_cat " +
                     "WHERE LOWER(product_name) LIKE ? OR LOWER(product_desc) LIKE ?";

        if (con == null || con.isClosed()) {
            System.err.println("Database connection is not available.");
            return 0;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, likeKeyword);
            ps.setString(2, likeKeyword);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}

