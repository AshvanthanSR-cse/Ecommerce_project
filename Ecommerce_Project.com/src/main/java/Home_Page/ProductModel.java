package Home_Page;

import java.math.BigDecimal;

public class ProductModel {
    private int productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private String imageUrl;

    public ProductModel(int productId, String productName, String description, BigDecimal price, String imageUrl) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String toString() {
        return "Product{" +
               "id=" + productId +
               ", name='" + productName + '\'' +
               ", price=" + price +
               '}';
    }
}
