package Model;

import java.util.Map;

/**
 * @class Entry
 * @author Hubert Furmanek <254315@stud.umk.pl>
 * @brief Class represents Tuple or Pair element in Java.
 * It implement Templates for key and value attributes.
 */
final class Entry<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    /**
     * @fn Entry
     * @brief Contructor of class Entry.
     * @param key - key element in Entry.
     * @param value - value assigned to key in Entry.
     */
    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @fn getKey
     * @brief Get key element from Entry.
     * @return key
     */
    @Override
    public K getKey() {
        return key;
    }

    /**
     * @fn getValue
     * @brief Get value element from Entry.
     * @return value
     */
    @Override
    public V getValue() {
        return value;
    }

    /**
     * @fn setValue
     * @brief Set value element in Entry.
     * @param value - new value which will be assigned to Entry.
     */
    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }
}