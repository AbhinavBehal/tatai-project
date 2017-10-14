package tatai.model.generator;

import java.util.Random;

public class NumberGenerator implements Generator {

    private static final int NUM_QUESTIONS = 10;
    private static final Module MODULE = Module.PRACTICE;
    private Difficulty _difficulty;
    private int _value;

    public NumberGenerator(Difficulty mode) {
        _difficulty = mode;
    }

    @Override
    public String generate() {
        Random r = new Random();
        _value = r.nextInt(_difficulty.val()) + 1;
        return Integer.toString(_value);
    }

    @Override
    public int value() {
        return _value;
    }

    @Override
    public int questions() {
        return NUM_QUESTIONS;
    }

    @Override
    public Module module() {
        return MODULE;
    }

    @Override
    public Difficulty difficulty() {
        return _difficulty;
    }
}
