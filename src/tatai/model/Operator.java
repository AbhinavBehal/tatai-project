package tatai.model;

public enum Operator {
    SUM("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    private String _operation;

    Operator(String operation) {
        _operation = operation;
    }

    @Override
    public String toString() {
        return _operation;
    }
}
