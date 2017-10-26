package tatai.util;

/**
 * Class used as a tuple, namely a Triple. Stores three objects inside the class.
 * @param <F> First Object
 * @param <S> Second Object
 * @param <T> Third Object
 */
public class Triple<F extends Comparable<? super F>,
        S extends Comparable<? super S>,
        T extends Comparable<? super T>> implements Comparable<Triple<F, S, T>> {

    private F _first;
    private S _second;
    private T _third;

    /**
     * Constructor for a new Triple, 3-tuple, class.
     * @param first Object of first type.
     * @param second Object of second type.
     * @param third Object of third type.
     */
    public Triple(F first, S second, T third) {
        _first = first;
        _second = second;
        _third = third;
    }

    /**
     * Public method used to get the object stored as the first.
     * @return Object stored as first.
     */
    public F first() { return _first; }

    /**
     * Public method used to get the object stored as the second.
     * @return Object stored as second.
     */
    public S second() { return _second; }

    /**
     * Public method used to get the object stored as the value.
     * @return Object stored as third.
     */
    public T third() { return _third; }

    /**
     * Public toString() method overridden to represent Triple class.
     * @return String representation of the Triple object.
     */
    @Override
    public String toString() {
        return "[" + _first + ", " + _second + ", " + _third + "]";
    }

    /**
     * Public hashCode() method overridden to calculate hashCode of the Triple
     * object.
     * @return int hash.
     */
    @Override
    public int hashCode() {
        return _first.hashCode() * 31 + _second.hashCode() * 11 + _third.hashCode() * 13;
    }

    /**
     * Public equals() method overridden to check if an object is equal to an
     * instance of Triple.
     * @param o Object to compare the 3-tuple instance with.
     * @return boolean whether the objects are deemed equal or not.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Triple)) return false;
        Triple triple = (Triple) o;
        return _first == null ? _first == triple._first : _first.equals(triple._first) &&
               _second == null ? _second == triple._second : _second.equals(triple._second) &&
               _third == null ? _third == triple._third : _third.equals(triple._third);
    }

    /**
     * Public compareTo() method overridden to compare two Triple instances
     * @param triple The other Triple instance to compare with.
     * @return int depending on which instance is deemed greater, equal, or
     * lesser. Compares first, then second, and lastly value.
     */
    @Override
    public int compareTo(Triple<F, S, T> triple) {
        int f = this._first.compareTo(triple.first());
        int s = this._second.compareTo(triple.second());

        if (f == 0) {
            if (s == 0) {
                return this._third.compareTo(triple.third());
            } else {
                return s;
            }
        } else {
            return f;
        }
    }
}
