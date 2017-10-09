package tatai.model;

import java.util.Random;

public class NumberGenerator implements Generator {

    private boolean _easyMode;
    private int _value;

    public NumberGenerator(boolean easy) {
        _easyMode = easy;
    }

    @Override
    public String generate() {
        Random r = new Random();
        _value = r.nextInt(_easyMode ? Difficulty.EASY.val() : Difficulty.HARD.val()) + 1;
        return Integer.toString(_value);
    }

    @Override
    public int value() {
        return _value;
    }
}
