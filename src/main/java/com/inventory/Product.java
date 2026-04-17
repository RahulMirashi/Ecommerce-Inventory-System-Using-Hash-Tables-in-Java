package main.java.com.inventory;

/**
 * Represents an e-commerce product with SKU, name, category, price, and stock.
 * The SKU (Stock Keeping Unit) is used as the hash key across all implementations.
 */
public class Product {

    private String sku;        // unique identifier (key)
    private String name;
    private String category;
    private double price;
    private int stockQuantity;

    public Product(String sku, String name, String category, double price, int stockQuantity) {
        this.sku = sku;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    // --- Getters ---

    public String getSku()          { return sku; }
    public String getName()         { return name; }
    public String getCategory()     { return category; }
    public double getPrice()        { return price; }
    public int    getStockQuantity(){ return stockQuantity; }

    // --- Setters ---

    public void setPrice(double price)             { this.price = price; }
    public void setStockQuantity(int stockQuantity){ this.stockQuantity = stockQuantity; }

    @Override
    public String toString() {
        return String.format("Product{sku='%s', name='%s', category='%s', price=%.2f, stock=%d}",
                sku, name, category, price, stockQuantity);
    }
}
