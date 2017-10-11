package tatai.model;

public enum Operator {
    ADDITION("+"),
    SUBTRACTION("-"),
    MULTIPLICATION("x"),
    DIVISION("/");

    private String _operation;

    Operator(String operation) {
        _operation = operation;
    }

    @Override
    public String toString() {
        return _operation;
    }
}
