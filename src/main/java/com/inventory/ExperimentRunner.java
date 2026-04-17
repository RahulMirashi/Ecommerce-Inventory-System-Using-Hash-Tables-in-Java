
package main.java.com.inventory;

import java.util.List;

public class ExperimentRunner {

    // ---- Capacity values (chosen as primes for fair double-hashing) ----
    private static final int[] CAPACITIES = {127, 509, 1021, 4093, 8191};
    // Each test fills table to approximately this fraction of capacity
    private static final double TARGET_LOAD = 0.70;

    // ---- Reproducible seed ----
    private static final long SEED = 42L;

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println(" COMP47500 — Hash Table Experiments: E-Commerce Inventory");
        System.out.println("=============================================================\n");

        runExperiment1();
    }

    // =========================================================================
    // Experiment 1: Collision Rate Across All Three Techniques
    // =========================================================================
    private static void runExperiment1() {
        System.out.println("-------------------------------------------------------------");
        System.out.println("Collision Rate vs. Input Size (load ≈ 0.70)");
        System.out.println("-------------------------------------------------------------");
        System.out.printf("%-10s | %-22s | %-22s | %-22s%n",
                "N (items)", "Separate Chaining", "Linear Probing", "Double Hashing");
        System.out.println("-".repeat(82));

        ProductGenerator gen = new ProductGenerator(SEED);

        for (int cap : CAPACITIES) {
            int n = (int) (cap * TARGET_LOAD);
            List<Product> products = gen.generate(n);

            HashTable<String, Product> sc  = new SeparateChainingHT<>(cap);
            HashTable<String, Product> lp  = new LinearProbingHT<>(cap);
            HashTable<String, Product> dh  = new DoubleHashingHT<>(cap);

            for (Product p : products) {
                sc.insert(p.getSku(), p);
                lp.insert(p.getSku(), p);
                dh.insert(p.getSku(), p);
            }

            System.out.printf("%-10d | %-22s | %-22s | %-22s%n",
                    n,
                    sc.getTotalCollisions() + " collisions (lf=" + String.format("%.2f", sc.loadFactor()) + ")",
                    lp.getTotalCollisions() + " collisions (lf=" + String.format("%.2f", lp.loadFactor()) + ")",
                    dh.getTotalCollisions() + " collisions (lf=" + String.format("%.2f", dh.loadFactor()) + ")");
        }
        System.out.println();
    }
}
