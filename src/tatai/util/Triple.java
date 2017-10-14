package tatai.util;

public class Triple<K extends Comparable<? super K>,
        I extends Comparable<? super I>,
        V extends Comparable<? super V>> implements Comparable<Triple<K, I, V>> {

    private K _key;
    private I _item;
    private V _value;

    /**
     * Constructor for a new Triple, 3-tuple, class.
     * @param key Object of first type.
     * @param item Object of second type.
     * @param value Object of third type.
     */
    public Triple(K key, I item, V value) {
        _key = key;
        _item = item;
        _value = value;
    }

    /**
     * Public method used to get the object stored as the key.
     * @return Object stored as key.
     */
    public K key() { return _key; }

    /**
     * Public method used to get the object stored as the item.
     * @return Object stored as item.
     */
    public I item() { return _item; }

    /**
     * Public method used to get the object stored as the value.
     * @return Object stored as value.
     */
    public V val() { return _value; }

    /**
     * Public toString() method overridden to represent Triple class.
     * @return String representation of the Triple object.
     */
    @Override
    public String toString() {
        return "[" + _key + ", " + _item + ", " + _value + "]";
    }

    /**
     * Public hashCode() method overridden to calculate hashCode of the Triple
     * object.
     * @return int hash.
     */
    @Override
    public int hashCode() {
        return _key.hashCode() * 31 + _item.hashCode() * 11 + _value.hashCode() * 13;
    }

    /**
     * Public equals() method overridden to check if an object is equal to an
     * instance of Triple.
     * @param o Object to compare the 3-tuple instance with.
     * @return boolean whether the objects are deemed equal or not.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof Triple) {
            Triple triple = (Triple) o;
            if (this._key == null || triple._key == null || !this._key.equals(triple._key)) {
                return false;
            }

            if (this._item == null || triple._item == null || !this._item.equals(triple._item)) {
                return false;
            }

            if (this._value == null || triple._value == null || !this._value.equals(triple._value)) {
                return false;
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Public compareTo() method overridden to compare two Triple instances
     * @param triple The other Triple instance to compare with.
     * @return int depending on which instance is deemed greater, equal, or
     * lesser. Compares key, then item, and lastly value.
     */
    @Override
    public int compareTo(Triple<K, I, V> triple) {
        int k = this._key.compareTo(triple.key());
        int i = this._item.compareTo(triple.item());

        if (k == 0) {
            if (i == 0) {
                return this._value.compareTo(triple.val());
            } else {
                return i;
            }
        } else {
            return k;
        }
    }
}
