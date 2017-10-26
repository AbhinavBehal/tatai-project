package tatai.model.generator;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A generator that generates questions in a random order from a predefined list.
 */
public class CustomGenerator implements Generator {

    private List<String> _questions;
    private List<Integer> _answers;
    private int _currentQuestion;
    private int _currentAnswer;

    /**
     * Construct a new CustomGenerator.
     * @param questions The list of questions.
     * @param answers The corresponding answers to those questions.
     */
    public CustomGenerator(List<String> questions, List<Integer> answers) {
        _questions = questions;
        _answers = answers;
        randomise();
    }

    /**
     * Inherited public method which returns a string representation of an
     * equation the user loaded.
     * @return String representation of equation.
     */
    @Override
    public String generate() {
        // Reshuffle the questions if all of them have been generated
        if (_currentQuestion == _questions.size()) {
            _currentQuestion = 0;
            randomise();
        }

        _currentAnswer = _answers.get(_currentQuestion);
        return _questions.get(_currentQuestion++) + " = ?";
    }

    /**
     * Public method used to get the value of the answer to the current equation.
     * @return integer value of answer.
     */
    @Override
    public int value() {
        return _currentAnswer;
    }

    /**
     * Public method used to get the number of questions loaded in.
     * @return integer size of the list.
     */
    @Override
    public int questions() {
        return _questions.size();
    }

    // Does not have
    @Override
    public Module module() {
        return null;
    }

    // Does not have
    @Override
    public Difficulty difficulty() {
        return null;
    }

    // Helper function to shuffle the question and answer lists
    private void randomise() {
        Random r = new Random();
        for (int i = 0; i < _questions.size(); ++i) {
            int pos = r.nextInt(_questions.size() - i);
            Collections.swap(_questions, i, pos);
            Collections.swap(_answers, i, pos);
        }
    }
}
