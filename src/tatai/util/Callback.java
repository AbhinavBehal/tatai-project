package tatai.util;

/**
 * Interface representing a callback function
 * @param <T> The type of the result obtained from an operation
 */
public interface Callback<T> {
    /**
     * The callback function, called when an operation completes
     * @param result The result of the operation
     */
    void call(T result);
}
