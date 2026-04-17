package main.java.com.inventory;

import java.util.LinkedList;
public class SeparateChainingHT<K, V> implements HashTable<K, V> {

    // ---- Inner node class ----
    private static class Entry<K, V> {
        K key;
        V value;
        Entry(K k, V v) { key = k; value = v; }
    }

    // ---- Fields ----
    private final int capacity;
    private final LinkedList<Entry<K, V>>[] buckets;
    private int size;
    private int collisions;

    // ---- Constructor ----
    @SuppressWarnings("unchecked")
    public SeparateChainingHT(int capacity) {
        this.capacity   = capacity;
        this.buckets    = new LinkedList[capacity];
        this.size       = 0;
        this.collisions = 0;
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    // ---- Hash function ----
    private int hash(K key) {
        int h = key.hashCode();
        h ^= (h >>> 16);
        return Math.abs(h % capacity);
    }

    // ---- Interface implementation ----

    @Override
    public void insert(K key, V value) {
        int idx = hash(key);
        LinkedList<Entry<K, V>> bucket = buckets[idx];

        // If bucket already has entries, a collision is occurring
        if (!bucket.isEmpty()) collisions++;

        // Check for existing key (update in place)
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        bucket.add(new Entry<>(key, value));
        size++;
    }

    @Override
    public V search(K key) {
        int idx = hash(key);
        for (Entry<K, V> entry : buckets[idx]) {
            if (entry.key.equals(key)) return entry.value;
        }
        return null;
    }

    @Override
    public boolean delete(K key) {
        int idx = hash(key);
        LinkedList<Entry<K, V>> bucket = buckets[idx];
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                bucket.remove(entry);
                size--;
                return true;
            }
        }
        return false;
    }

    @Override public int    size()                  { return size; }
    @Override public double loadFactor()             { return (double) size / capacity; }
    @Override public int    getTotalCollisions()     { return collisions; }
    @Override public void   resetCollisionCounter()  { collisions = 0; }
    @Override public String getName()                { return "Separate Chaining"; }
}
