package main.java.com.inventory;

import java.util.List;

/**
 * InventoryManager demonstrates the inventory system using Hash Table
 * using all three collision-resolution strategies.
 *
 * This class is the primary entry point for demo use.
 */
public class InventoryManager {

    private final HashTable<String, Product> table;

    public InventoryManager(HashTable<String, Product> table) {
        this.table = table;
    }

    // ---- Business Operations ----

    /** Add or update a product by SKU */
    public void addProduct(Product product) {
        table.insert(product.getSku(), product);
        System.out.println("[ADD]    " + product);
    }

    /** Look up a product by SKU */
    public Product lookupBySku(String sku) {
        Product p = table.search(sku);
        if (p != null) {
            System.out.println("[FOUND]  " + p);
        } else {
            System.out.println("[MISS]   SKU not found: " + sku);
        }
        return p;
    }

    /** Remove a product from inventory */
    public boolean removeProduct(String sku) {
        boolean removed = table.delete(sku);
        System.out.println(removed ? "[REMOVE] SKU removed: " + sku
                                   : "[REMOVE] SKU not found: " + sku);
        return removed;
    }

    /** Restock a product (update quantity) */
    public void restock(String sku, int newQuantity) {
        Product p = table.search(sku);
        if (p != null) {
            p.setStockQuantity(newQuantity);
            table.insert(sku, p); // re-insert to update
            System.out.println("[RESTOCK] " + p);
        } else {
            System.out.println("[RESTOCK] SKU not found: " + sku);
        }
    }

    public void printStats() {
        System.out.printf("%nInventory Stats [%s]%n", table.getName());
        System.out.printf("  Items:      %d%n", table.size());
        System.out.printf("  Load Factor: %.3f%n", table.loadFactor());
        System.out.printf("  Collisions:  %d%n", table.getTotalCollisions());
    }

    // ---- Demo Main ----

    public static void main(String[] args) {
        System.out.println("=== E-Commerce Inventory System — Hash Table Demo ===\n");

        // Demonstrate with each strategy
        demo(new SeparateChainingHT<>(101));
        demo(new LinearProbingHT<>(101));
        demo(new DoubleHashingHT<>(101));
    }

    private static void demo(HashTable<String, Product> ht) {
        System.out.println("\n--- Strategy: " + ht.getName() + " ---");
        InventoryManager mgr = new InventoryManager(ht);

        ProductGenerator gen = new ProductGenerator(99L);
        List<Product> products = gen.generate(20);

        for (Product p : products) mgr.addProduct(p);

        // Lookup examples
        String firstSku = products.get(0).getSku();
        mgr.lookupBySku(firstSku);
        mgr.lookupBySku("ELE-999999"); // miss

        // Restock
        mgr.restock(firstSku, 9999);

        // Remove
        mgr.removeProduct(products.get(1).getSku());
        mgr.lookupBySku(products.get(1).getSku()); // should miss now

        mgr.printStats();
    }
}
