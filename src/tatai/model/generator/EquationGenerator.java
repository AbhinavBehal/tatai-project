package tatai.model.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static tatai.model.generator.Operator.*;

/**
 * Class implementing Generator used to generate strings of equations based on
 * input difficulty and operator parameters.
 */
public class EquationGenerator implements Generator {

    private static final int MIN_OPERATORS = 1;
    private static final int MAX_OPERATORS = 3;
    private static final int NUM_QUESTIONS = 10;
    private static final Module MODULE = Module.TEST;
    private Difficulty _difficulty;
    private List<Operator> _operators;
    private Random r = new Random();
    private int _answer;

    /**
     * Constructor used to define specifications of the equations.
     * @param difficulty Difficulty of the Module.
     * @param operators Types of Operators to use.
     */
    public EquationGenerator(Difficulty difficulty, List<Operator> operators) {
        _difficulty = difficulty;
        _operators = operators;
    }

    /**
     * Public method used to generate an equation with a random answer between 1 - 99
     * depending difficulty.
     * @return final equation
     */
    @Override
    public String generate() {
        _answer = r.nextInt(_difficulty.val()) + 1;

        String newEquation;

        if (_difficulty == Difficulty.HARD) {
            newEquation = append(r.nextInt(MAX_OPERATORS) + 1, _answer);
        } else {
            newEquation = append(MIN_OPERATORS, _answer);
        }

        // Returns equation string without outside brackets
        return newEquation.substring(1, newEquation.length() - 1) + " = ?";
    }

    /**
     * Private helper method used to generate a random factor of a given number.
     * @param number number to get factor of
     * @return a random factor of number
     */
    private int factorOf(int number) {
        if (number == 0) {
            return r.nextInt(_difficulty.val()) + 1;
        } else if (Math.abs(number) == 1) {
            return 1;
        }

        List<Integer> factors = new ArrayList<>();
        for (int i = 1; i <= Math.abs(number / 2); i++) {
            if (number % i == 0) {
                factors.add(i);
            }
        }
        factors.add(number);
        return factors.get(new Random().nextInt(factors.size()));
    }

    /**
     * Private helper method used to generate a random multiple of a number less than 500.
     * @param number number to get multiple of
     * @return a random multiple less than 500 of number
     */
    private int multipleOf(int number) {
        int bound = 500;
        if (number == 0) {
            return 0;
        } else if (number > bound) {
            return number;
        }

        List<Integer> multiples = new ArrayList<>();
        int i = 0;
        while (Math.abs(++i * number) <= bound) {
            multiples.add(i * number);
        }
        return multiples.get(new Random().nextInt(multiples.size()));
    }

    /**
     * Private recursive method used to append n number of operators into equations.
     * @param n number of operators
     * @param previousAnswer to calculate to
     * @return appended equation
     */
    private String append(int n, int previousAnswer) {
        if (n == 0) {
            return Integer.toString(previousAnswer);
        }

        int a = Math.abs(r.nextInt(_difficulty.val()) - previousAnswer);
        int currentAnswer = previousAnswer;

        // Get a random operator
        Operator op;
        if (_operators.size() == 1) {
            op = _operators.get(0);
        } else {
            op = _operators.get(r.nextInt(_operators.size()));
        }

        // Don't allow division by 0
        if (op == DIVISION && currentAnswer == 0) {
            op = ADDITION;
        }

        // Calculate newest answer depending on operator
        if (op.equals(ADDITION)) {
            currentAnswer -= a;
        } else if (op.equals(SUBTRACTION)) {
            currentAnswer += a;
        } else if (op.equals(MULTIPLICATION)) {
            a = factorOf(currentAnswer);
            currentAnswer /= a;
        } else if (op.equals(DIVISION)) {
            a = multipleOf(currentAnswer);
            currentAnswer = a / currentAnswer;
        } else {
            return "";
        }

        // Append next operator
        String newEquation = append(--n, currentAnswer);

        // Construct string from previously generated equation string and append
        // current equation to it. If addition or multiplication, append current
        // equation either in front or behind.
        if (op.equals(ADDITION)) {
            if (r.nextBoolean()) {
                newEquation = "(" + newEquation + " " + op + " " + a + ")";
            } else {
                newEquation = "(" + a + " " + op + " " + newEquation + ")";
            }
        } else if (op.equals(SUBTRACTION)) {
            newEquation = "(" + newEquation + " " + op + " " + a + ")";
        } else if (op.equals(MULTIPLICATION)) {
            if (r.nextBoolean()) {
                newEquation = "(" + newEquation + " " + op + " " + a + ")";
            } else {
                newEquation = "(" + a + " " + op + " " + newEquation + ")";
            }
        } else if (op.equals(DIVISION)) {
            newEquation = "(" + a + " " + op + " " + newEquation + ")";
        }

        // Return string with adding negative replaced with just subtracting
        return newEquation.replace("+ -", SUBTRACTION + " ");
    }

    /**
     * Public method used to get the current randomly generated answer.
     * @return answer to equation generated
     */
    @Override
    public int value() {
        return _answer;
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
