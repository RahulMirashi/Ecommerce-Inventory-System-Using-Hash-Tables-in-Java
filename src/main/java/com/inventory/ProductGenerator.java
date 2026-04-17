package main.java.com.inventory;

import main.java.com.inventory.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utility class that generates realistic synthetic e-commerce product data
 * for use in experiments.  Produces deterministic data sets (seeded RNG)
 * so experiments are reproducible.
 */
public class ProductGenerator {

    private static final String[] CATEGORIES = {
        "Electronics", "Clothing", "Books", "Sports", "Home & Garden",
        "Toys", "Automotive", "Beauty", "Food", "Tools"
    };

    private static final String[] ADJECTIVES = {
        "Pro", "Ultra", "Slim", "Max", "Mini", "Smart", "Elite", "Plus", "Eco", "HD"
    };

    private static final String[] NOUNS = {
        "Widget", "Gadget", "Device", "Tool", "Kit", "Pack", "Set", "Unit", "Module", "Gear"
    };

    private final Random rng;

    public ProductGenerator(long seed) {
        this.rng = new Random(seed);
    }

    /**
     * Generate n products with unique SKUs.
     * SKU format: CAT-XXXXXX  (category prefix + 6-digit zero-padded number)
     */
    public List<Product> generate(int n) {
        List<Product> products = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            String category = CATEGORIES[rng.nextInt(CATEGORIES.length)];
            String prefix   = category.substring(0, 3).toUpperCase();
            String sku      = prefix + "-" + String.format("%06d", i + 1);
            String name     = ADJECTIVES[rng.nextInt(ADJECTIVES.length)]
                            + " " + NOUNS[rng.nextInt(NOUNS.length)];
            double price    = Math.round((5 + rng.nextDouble() * 995) * 100.0) / 100.0;
            int    stock    = rng.nextInt(500) + 1;
            products.add(new Product(sku, name, category, price, stock));
        }
        return products;
    }

    /**
     * Generate n SKU strings that do NOT exist in the product set
     * (for failed-search experiments).
     */
    public List<String> generateMissingSkus(int n) {
        List<String> skus = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            skus.add("MISS-" + String.format("%06d", i + 1));
        }
        return skus;
    }
}
