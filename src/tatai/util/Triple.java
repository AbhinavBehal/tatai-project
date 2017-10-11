package tatai.util;

public class Triple<K, I, V> {

    private K _key;
    public K Key() { return _key; }

    private I _item;
    public I Item() { return _item; }

    private V _value;
    public V Val() { return _value; }

    public Triple(K key, I item, V value) {
        _key = key;
        _item = item;
        _value = value;
    }

    @Override
    public String toString() {
        return "[" + _key + ", " + _item + ", " + _value + "]";
    }

    @Override
    public int hashCode() {
        return _key.hashCode() * 31 + _item.hashCode() * 11 + _value.hashCode() * 13;
    }

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
}
