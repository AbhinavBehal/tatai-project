package tatai.model;

public enum Difficulty {
    EASY(8),
    HARD(98);

    private int _value;

    Difficulty(int value) { _value = value; }

    public int val() { return _value; }
}
