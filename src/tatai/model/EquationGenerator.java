package tatai.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.List;

public class EquationGenerator implements Generator {

    private Difficulty _difficulty;
    private List<Operator> _operations;
    private int _value;

    public EquationGenerator(Difficulty mode, List operations) {
        _difficulty = mode;
        _operations = operations;
    }

    /**
     * Current equation maker, takes _value as input to generate an equation from
     * @return final equation
     */
    @Override
    public String generate() {
        Random r = new Random();

        //answer
        _value = r.nextInt(_difficulty.val() - 1 ) + 1;

        //
        int operation = r.nextInt(_operations.size());
        int a = Math.abs(r.nextInt(_difficulty.val()) - _value);
        int result = _value;

        String newEquation = "(";

        if (_operations.get(operation) == Operator.SUM) {
            result -= Math.abs(a);
            newEquation += result + "+" + a + ")";
        } else if (_operations.get(operation) == Operator.SUBTRACT) {
            result += a;
            newEquation += result + "-" + a + ")";
        } else if (_operations.get(operation) == Operator.MULTIPLY) {
            a = factorOf(_value);
            result = _value / a;

            if (r.nextBoolean()) {
                newEquation += result + "*" + a + ")";
            } else {
                newEquation += a + "*" + result + ")";
            }
        } else if (_operations.get(operation) == Operator.DIVIDE) {
            result *= a;
            newEquation += result + "/" + a + ")";
        }
        return newEquation;
    }

    /**
     * private helper function used to generate a random factor of a number
     * @param number number to get factor of
     * @return a random factor of number
     */
    private static int factorOf(int number) {
        List<Integer> factors = new ArrayList<>();
        for (int i = 1; i < number/2; i++) {
            if (number % i == 0) {
                factors.add(i);
            }
        }
        factors.add(number);
        return factors.get(new Random().nextInt(factors.size()));
    }

    /**
     * Old equation maker
     * TODO: change to append for multiple operators
     * @param equation to append to
     * @return appended equation
     */
    private static String append(String equation) {
        Random r = new Random();
        double val = r.nextDouble();
        int a = r.nextInt(98) + 1;

        String newEquation = "(";
        String op;

        if (val < 0.25) {
            op = "+";
        } else if (val < 0.5) {
            op = "-";
        } else if (val < 0.75) {
            op = "*";
        } else {
            op = "/";
        }

        if (equation.isEmpty()) {
            int b = r.nextInt(99) - a;
            if (b < 0 && op.equals("+")) {
                b = Math.abs(b);
                op = "-";
            }
            newEquation += a + op + b + ")";
        } else if (r.nextBoolean()) {
            newEquation += equation + op + a + ")";
        } else {
            newEquation += a + op + equation + ")";
        }

        System.out.println(newEquation);

        return append(newEquation);
    }

    /**
     * TODO: string parser?
     * @param equation equation to parse
     * @return _value of equation?
     */
    public static int solve(String equation) {
        return -1;
    }

    @Override
    public int value() {
        return _value;
    }
}
