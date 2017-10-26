package tatai.model.generator;

import java.util.Random;

/**
 * Class implementing Generator used to generate a number based on input difficulty.
 */
public class NumberGenerator implements Generator {

    private static final int NUM_QUESTIONS = 10;
    private static final Module MODULE = Module.PRACTICE;
    private Difficulty _difficulty;
    private int _value;
    private Random r = new Random();

    public NumberGenerator(Difficulty mode) {
        _difficulty = mode;
    }

    /**
     * Public method used to generate a random number depending on difficulty.
     * @return String representation of number.
     */
    @Override
    public String generate() {
        _value = r.nextInt(_difficulty.val()) + 1;
        return Integer.toString(_value);
    }

    /**
     * Public method to get the integer value of generated number.
     * @return integer representation of generated number.
     */
    @Override
    public int value() {
        return _value;
    }

    /**
     * Public method used to get the number of questions for this generator.
     * @return Total number of questions for the Module.
     */
    @Override
    public int questions() {
        return NUM_QUESTIONS;
    }

    /**
     * Public method used to get the module of the generator, which
     * (for now) will only be TEST.
     * @return TEST Module enum.
     */
    @Override
    public Module module() {
        return MODULE;
    }

    /**
     * Public method used to get the difficulty of the generator.
     * @return Difficulty assigned at construction.
     */
    @Override
    public Difficulty difficulty() {
        return _difficulty;
    }
}
