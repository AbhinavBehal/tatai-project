package tatai.model;

import java.util.Random;

public class NumberGenerator implements Generator {

    private Difficulty _difficulty;
    private int _value;

    public NumberGenerator(Difficulty mode) {
        _difficulty = mode;
    }

    @Override
    public String generate() {
        Random r = new Random();
        _value = r.nextInt(_difficulty.val() -1 ) + 1;
        return Integer.toString(_value);
    }

    @Override
    public int value() {
        return _value;
    }
}
