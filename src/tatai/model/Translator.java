package tatai.model;

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
        String output = "";
        if (n <= 10) {
            output = words[n - 1];
        } else if (n / 10 == 1) {
            output = String.format("%s m\u0101 %s", words[9], words[n % 10 - 1]);
        } else if (n % 10 == 0) {
            output = String.format("%s %s", words[n / 10 - 1], words[9]);
        } else {
            output = String.format("%s %s m\u0101 %s", words[n / 10 - 1], words[9], words[n % 10 - 1]);
        }
        return output;
    }
}
