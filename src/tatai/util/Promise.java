package tatai.util;

/**
 * Class representing the future output of an operation
 * @param <T> The type of the result of the operation
 */
public class Promise<T> {

    private Callback<T> _resolve;
    private Callback<Throwable> _reject;

    public Promise() {}

    /**
     * Function to set the callback functions that are executed when the callback is available
     * @param resolve The callback function called when the operation completes successfully
     * @param reject The callback function called when the operation is unsuccessful
     */
    public void then(Callback<T> resolve, Callback<Throwable> reject) {
        _resolve = resolve;
        _reject = reject;
    }

    /**
     * Resolve the promise when the operation successfully completes
     * @param result the result of the operation
     */
    public void resolve(T result) {
        if (_resolve == null) return;
        _resolve.call(result);
    }

    /**
     * Reject the promise when the operation is unsuccessful
     * @param err The exception that occurred during the operation (may be null if no exception occurred)
     */
    public void reject(Throwable err) {
        if (_reject == null) return;
        _reject.call(err);
    }

}
