package tatai.model.generator;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CustomGenerator implements Generator {

    private List<String> _questions;
    private List<Integer> _answers;
    private int _currentQuestion;
    private int _currentAnswer;

    public CustomGenerator(List<String> questions, List<Integer> answers) {
        _questions = questions;
        _answers = answers;
        randomise();
    }

    @Override
    public String generate() {
        if (_currentQuestion == _questions.size()) return "";

        _currentAnswer = _answers.get(_currentQuestion);
        return _questions.get(_currentQuestion++) + " = ?";
    }

    @Override
    public int value() {
        return _currentAnswer;
    }

    @Override
    public int questions() {
        return _questions.size();
    }

    @Override
    public Module module() {
        return null;
    }

    @Override
    public Difficulty difficulty() {
        return null;
    }

    private void randomise() {
        Random r = new Random();
        for (int i = 0; i < _questions.size(); ++i) {
            int pos = r.nextInt(_questions.size() - i);
            Collections.swap(_questions, i, pos);
            Collections.swap(_answers, i, pos);
        }
    }
}
