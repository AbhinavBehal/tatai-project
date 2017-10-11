package tatai.model.generator;

public enum Difficulty {
    EASY(9),
    HARD(99);

    private int _value;

    Difficulty(int value) { _value = value; }

    public int val() { return _value; }
}
