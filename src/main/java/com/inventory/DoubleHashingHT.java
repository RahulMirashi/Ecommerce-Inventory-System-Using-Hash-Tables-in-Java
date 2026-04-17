package main.java.com.inventory;

public class DoubleHashingHT<K, V> implements HashTable<K, V> {

    private static final Object DELETED = new Object();

    // ---- Fields ----
    private final int      capacity;   // prime number
    private final int      q;          // prime < capacity for h2
    private final Object[] keys;
    private final Object[] values;
    private int    size;
    private int    collisions;

    // ---- Constructor ----
    public DoubleHashingHT(int capacity) {
        this.capacity   = capacity;
        this.q          = largestPrimeSmallerThan(capacity);
        this.keys       = new Object[capacity];
        this.values     = new Object[capacity];
        this.size       = 0;
        this.collisions = 0;
    }

    // ---- Hash functions ----

    private int h1(K key) {
        int h = key.hashCode();
        h ^= (h >>> 16);
        return Math.abs(h % capacity);
    }

    private int h2(K key) {
        int h = key.hashCode();
        h ^= (h >>> 16);
        // Ensures step size >= 1 and != 0
        return q - (Math.abs(h) % q);
    }

    // ---- Helper: largest prime < n ----
    private static int largestPrimeSmallerThan(int n) {
        for (int candidate = n - 1; candidate >= 2; candidate--) {
            if (isPrime(candidate)) return candidate;
        }
        return 2;
    }

    private static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // ---- Interface implementation ----

    @Override
    public void insert(K key, V value) {
        int h1  = h1(key);
        int h2  = h2(key);
        int idx = h1;

        for (int i = 0; i < capacity; i++) {
            if (keys[idx] == null || keys[idx] == DELETED) {
                keys[idx]   = key;
                values[idx] = value;
                size++;
                return;
            }
            if (keys[idx].equals(key)) {  // update
                values[idx] = value;
                return;
            }
            collisions++;
            idx = (h1 + (i + 1) * h2) % capacity;
        }
        throw new RuntimeException("Hash table is full");
    }

    @Override
    @SuppressWarnings("unchecked")
    public V search(K key) {
        int h1  = h1(key);
        int h2  = h2(key);
        int idx = h1;

        for (int i = 0; i < capacity; i++) {
            if (keys[idx] == null) break;
            if (keys[idx] != DELETED && keys[idx].equals(key)) {
                return (V) values[idx];
            }
            idx = (h1 + (i + 1) * h2) % capacity;
        }
        return null;
    }

    @Override
    public boolean delete(K key) {
        int h1  = h1(key);
        int h2  = h2(key);
        int idx = h1;

        for (int i = 0; i < capacity; i++) {
            if (keys[idx] == null) break;
            if (keys[idx] != DELETED && keys[idx].equals(key)) {
                keys[idx]   = DELETED;
                values[idx] = null;
                size--;
                return true;
            }
            idx = (h1 + (i + 1) * h2) % capacity;
        }
        return false;
    }

    @Override public int    size()                  { return size; }
    @Override public double loadFactor()             { return (double) size / capacity; }
    @Override public int    getTotalCollisions()     { return collisions; }
    @Override public void   resetCollisionCounter()  { collisions = 0; }
    @Override public String getName()                { return "Double Hashing"; }
}
