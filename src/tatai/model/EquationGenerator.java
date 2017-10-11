package tatai.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EquationGenerator implements Generator {

    // extra entry, used for testing equation generator
    // remove once completed testing
    public static void main(String[] args) {
        ArrayList<Operator> OP = new ArrayList<>();
        OP.add(Operator.ADDITION);
        OP.add(Operator.SUBTRACTION);
        OP.add(Operator.MULTIPLICATION);
        OP.add(Operator.DIVISION);
        EquationGenerator eg = new EquationGenerator(Difficulty.HARD, OP);
        System.out.println(eg.generate());
        System.out.println("=" + eg.value());
    }

    private static final int MIN_OPERATORS = 1;
    private static final int MAX_OPERATORS = 3;
    private Difficulty _difficulty;
    private List<Operator> _operations;
    private int _answer;
    Random r = new Random();

    public EquationGenerator(Difficulty mode, List operations) {
        _difficulty = mode;
        _operations = operations;
    }

    /**
     * Current equation maker, generates an equation for a random number depending
     * on input difficulty at construction.
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
        return newEquation.substring(1, newEquation.length() - 1) + " = ?";
    }

    /**
     * private helper function used to generate a random factor of a number
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
     * private helper function used to generate a random multiple of a number less than 500
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
     * recursive func used to append n number of operators into equations
     * @param n number of operators
     * @param previousAnswer to calculate to
     * @return appended equation
     */
    private String append(int n, int previousAnswer) {

        // check if working correctly
        //System.out.println(n + " " + previousAnswer);
        if (n == 0) {
            return Integer.toString(previousAnswer);
        }

        Random r = new Random();
        int a = Math.abs(r.nextInt(_difficulty.val()) - previousAnswer);
        int currentAnswer = previousAnswer;

        Operator op;
        if (_operations.size() == 1) {
            op = _operations.get(0);
        } else {
            op = _operations.get(r.nextInt(_operations.size()));
        }

        if (op == Operator.DIVISION && currentAnswer == 0) {
            op = Operator.ADDITION;
        }

        if (op.equals(Operator.ADDITION)) {
            currentAnswer -= a;
        } else if (op.equals(Operator.SUBTRACTION)) {
            currentAnswer += a;
        } else if (op.equals(Operator.MULTIPLICATION)) {
            a = factorOf(currentAnswer);
            currentAnswer /= a;
        } else if (op.equals(Operator.DIVISION)) {
            a = multipleOf(currentAnswer);
            currentAnswer = a / currentAnswer;
        } else {
            return "";
        }

        // check current equation output
        //System.out.println(previousAnswer + "=" + currentAnswer + op + a);

        String newEquation = append(--n, currentAnswer);

        if (op.equals(Operator.ADDITION)) {
            if (r.nextBoolean()) {
                newEquation = "(" + newEquation + " " + op + " " + a + ")";
            } else {
                newEquation = "(" + a + " " + op + " " + newEquation + ")";
            }
        } else if (op.equals(Operator.SUBTRACTION)) {
            newEquation = "(" + newEquation + " " + op + " " + a + ")";
        } else if (op.equals(Operator.MULTIPLICATION)) {
            if (r.nextBoolean()) {
                newEquation = "(" + newEquation + " " + op + " " + a + ")";
            } else {
                newEquation = "(" + a + " " + op + " " + newEquation + ")";
            }
        } else if (op.equals(Operator.DIVISION)) {
            newEquation = "(" + a + " " + op + " " + newEquation + ")";
        }

        return newEquation.replace("+-", Operator.SUBTRACTION.toString());
    }

    /**
     * TODO: string parser? - may move to util if still needed?
     * @param equation equation to parse
     * @return _value of equation?
     */
    public int solve(String equation) {
        return -1;
    }

    /**
     * Public function used to get the current randomly generated answer
     * @return answer to equation generated
     */
    @Override
    public int value() {
        return _answer;
    }
}
