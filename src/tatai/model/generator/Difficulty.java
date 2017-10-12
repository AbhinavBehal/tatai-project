package tatai.model.generator;

public enum Difficulty {
    EASY("Easy", 9),
    HARD("Hard", 99);

    private int _value;
    private String _difficulty;

    Difficulty(String difficulty, int value) {
        _difficulty = difficulty;
        _value = value;
    }

    public int val() {
        return _value;
    }

    @Override
    public String toString() {
        return _difficulty;
    }
}
