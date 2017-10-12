package tatai.model.recognition;

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
