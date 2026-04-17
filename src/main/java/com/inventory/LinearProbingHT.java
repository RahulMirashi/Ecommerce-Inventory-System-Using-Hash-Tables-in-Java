package main.java.com.inventory;
public class LinearProbingHT<K, V> implements HashTable<K, V> {

    // ---- Tombstone sentinel ----
    private static final Object DELETED = new Object();

    // ---- Fields ----
    private final int    capacity;
    private final Object[] keys;
    private final Object[] values;
    private int    size;
    private int    collisions;

    // ---- Constructor ----
    public LinearProbingHT(int capacity) {
        this.capacity   = capacity;
        this.keys       = new Object[capacity];
        this.values     = new Object[capacity];
        this.size       = 0;
        this.collisions = 0;
    }

    // ---- Hash function (same spread as chaining for fair comparison) ----
    private int hash(K key) {
        int h = key.hashCode();
        h ^= (h >>> 16);
        return Math.abs(h % capacity);
    }

    // ---- Interface implementation ----

    @Override
    public void insert(K key, V value) {
        int idx   = hash(key);
        int start = idx;

        while (keys[idx] != null && keys[idx] != DELETED) {
            if (keys[idx].equals(key)) {        // update existing
                values[idx] = value;
                return;
            }
            collisions++;
            idx = (idx + 1) % capacity;
            if (idx == start) throw new RuntimeException("Hash table is full");
        }
        keys[idx]   = key;
        values[idx] = value;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V search(K key) {
        int idx   = hash(key);
        int start = idx;

        while (keys[idx] != null) {
            if (keys[idx] != DELETED && keys[idx].equals(key)) {
                return (V) values[idx];
            }
            idx = (idx + 1) % capacity;
            if (idx == start) break;
        }
        return null;
    }

    @Override
    public boolean delete(K key) {
        int idx   = hash(key);
        int start = idx;

        while (keys[idx] != null) {
            if (keys[idx] != DELETED && keys[idx].equals(key)) {
                keys[idx]   = DELETED;
                values[idx] = null;
                size--;
                return true;
            }
            idx = (idx + 1) % capacity;
            if (idx == start) break;
        }
        return false;
    }

    @Override public int    size()                  { return size; }
    @Override public double loadFactor()             { return (double) size / capacity; }
    @Override public int    getTotalCollisions()     { return collisions; }
    @Override public void   resetCollisionCounter()  { collisions = 0; }
    @Override public String getName()                { return "Linear Probing"; }
}
