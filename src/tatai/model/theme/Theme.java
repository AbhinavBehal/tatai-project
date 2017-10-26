package tatai.model.theme;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Theme enum, used to represent the themes available to the user for the UI.
 * Standard themes include: Sunset, Laboratory, and Sublime (subject to change);
 * the user may define their own theme via placeholder-theme (subject to change).
 */
public enum Theme {
    BEACHSIDE("/themes/beachside-theme.css"),
    CULTURAL("/themes/cultural-theme.css"),
    SUBLIME("/themes/sublime-theme.css"),
    KORU("/themes/koru-theme.css");

    private String _url;
    private String _bg;
    private String _fg;

    /**
     * Enum constructor, reads in the colour codes for theme from css, namely the
     * background and foreground colours of the theme.
     * @param url Location of theme including its name.
     */
    Theme(String url) {
        _url = url;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(url)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("-background")) {
                    _bg = line.substring(line.lastIndexOf("#"), line.lastIndexOf(";"));
                }
                if (line.contains("-foreground")) {
                    _fg = line.substring(line.lastIndexOf("#"), line.lastIndexOf(";"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Public toString() method overridden to represent Theme enum.
     * @return String representation of the theme's location.
     */
    @Override
    public String toString() {
        return _url;
    }

    /**
     * Public method used to get the simple name of the theme.
     * @return String representation of the theme's simple name.
     */
    public String simpleName() {
        return _url.substring(_url.lastIndexOf("/") + 1, _url.lastIndexOf("-theme.css"));
    }

    /**
     * Public method used to get the background colour of the theme in hexadecimal.
     * @return String representation of the hexadecimal colour value.
     */
    public String hexBackground() {
        return _bg;
    }

    /**
     * Public method used to get the foreground colour of the theme in hexadecimal.
     * @return String representation of the hexadecimal colour value.
     */
    public String hexForeground() {
        return _fg;
    }

}
