package tatai.model.recognition;

/**
 * Class that provides a static method to translate a number into its Maori form.
 */
public class Translator {
    private static final String[] words = {
            "tahi",
            "rua",
            "toru",
            "wh\u0101",
            "rima",
            "ono",
            "whitu",
            "waru",
            "iwa",
            "tekau"
    };

    /**
     * Translate an integer between 1 - 99 (inclusive) into its Maori form.
     * @param n The integer to translate.
     * @return The Maori representation of the number.
     */
    public static String translate(int n) {
        String output;
        if (n <= 10) {
            output = words[n - 1];
        } else if (n / 10 == 1) {
            output = words[9] + " m\u0101 " + words[n % 10 - 1];
        } else if (n % 10 == 0) {
            output = words[n / 10 - 1] + " " + words[9];
        } else {
            output = words[n / 10 - 1] + " " + words[9] + " m\u0101 " + words[n % 10 - 1];
        }
        return output;
    }
}
