package tatai.model.generator;

/**
 * Difficulty enum, used to represent the difficulty of the Module a user can
 * partake in; either easy, or hard.
 */
public enum Difficulty {
    EASY("Easy", 9),
    HARD("Hard", 99);

    private int _value;
    private String _difficulty;

    Difficulty(String difficulty, int value) {
        _difficulty = difficulty;
        _value = value;
    }

    /**
     * Public method used to get the integer representation of the difficulties.
     * @return int value associated with the difficulty.
     */
    public int val() {
        return _value;
    }

    /**
     * Public toString() method overridden to represent Difficulty enum.
     * @return String representation of the enum.
     */
    @Override
    public String toString() {
        return _difficulty;
    }
}
