package main.java.com.inventory;

/**
 * Common interface for all Hash Table implementations.
 * Defines the contract for insert, search, delete, and diagnostic operations.
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public interface HashTable<K, V> {
    void insert(K key, V value);

    V search(K key);

    boolean delete(K key);

    int size();

    double loadFactor();

    int getTotalCollisions();

    void resetCollisionCounter();

    String getName();
}
