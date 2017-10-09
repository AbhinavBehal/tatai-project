package tatai.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EquationGenerator {

    /**
     * Current equation maker, takes answer as input to generate an equation from
     * @param answer answer of equation
     * @param operators number of operators
     * @return final equation
     */
    public static String generate(int answer, int operators) {
        Random r = new Random();

        double val = r.nextDouble();
        int a = Math.abs(r.nextInt(99) - answer);
        int result = answer;

        String newEquation = "(";

        if (val < 0.25) {
            result -= Math.abs(a);
            newEquation += result + "+" + a + ")";
        } else if (val < 0.5) {
            result += a;
            newEquation += result + "-" + a + ")";
        } else if (val < 0.75) {
            a = factorOf(answer);
            result = answer / a;

            if (r.nextBoolean()) {
                newEquation += result + "*" + a + ")";
            } else {
                newEquation += a + "*" + result + ")";
            }
        } else {
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
        for (int i = 1; i < number; i++) {
            if (number % i == 0) {
                factors.add(i);
            }
        }
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
     * @return answer of equation?
     */
    public static int solve(String equation) {
        return -1;
    }
}
