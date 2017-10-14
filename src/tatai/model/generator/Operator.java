package tatai.model.generator;

/**
 * Operator enum, used to represent the operators used in the different Generators.
 * These include addition, subtraction, multiplication, and division.
 */
public enum Operator {
    ADDITION("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("x"),
    DIVISION("/");

    private String _operation;

    Operator(String operation) {
        _operation = operation;
    }

    /**
     * Public toString() method overridden to represent Operator enum.
     * @return String representation of operator.
     */
    @Override
    public String toString() {
        return _operation;
    }
}
