package tatai.model.theme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ThemeManager {

    private static final Theme DEFAULT_THEME = Theme.BEACHSIDE;
    private static ThemeManager _manager;
    private List<ThemeListener> _listeners;
    private Theme _currentTheme;

    /**
     * Private constructor for singleton ThemeManager instance calls setInitialTheme().
     */
    private ThemeManager() {
        _listeners = new ArrayList<>();
        setInitialTheme();
    }

    /**
     * Public method used to get the singleton instance.
     * @return singleton ThemeManager.
     */
    public static ThemeManager manager() {
        if (_manager == null) {
            _manager = new ThemeManager();
        }
        return _manager;
    }

    /**
     * Public method used to add listeners to StatsManager when scores change.
     * @param listener An object of class which implements ThemeListener.
     */
    public void addListener(ThemeListener listener) {
        _listeners.add(listener);
    }

    /**
     * Public method used to update theme managed by ThemeManager singleton, calls
     * the inherited updateTheme on all ThemeListeners and sets current theme to
     * the new passed in theme.
     * @param newTheme Theme to set current theme to.
     */
    public void updateTheme(Theme newTheme) {
        _listeners.forEach(l -> l.updateTheme(_currentTheme, newTheme));
        _currentTheme = newTheme;
    }

    /**
     * Public method used to get the current theme stored in ThemeManager.
     * @return Theme stored as current theme.
     */
    public Theme getCurrentTheme() {
        return _currentTheme;
    }

    /**
     * Private helper method used at construction to read in, and set, the current
     * theme to the last theme the user had active before exiting the application.
     * This is stored in the data file.
     */
    private void setInitialTheme() {
        if (!new File("data.txt").exists()) {
            _currentTheme = DEFAULT_THEME;
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(new File("data.txt")))) {
                String line;

                if ((line = reader.readLine()) != null) {
                    for (Theme t : Theme.values()) {
                        if (line.contains(t.simpleName())) {
                            System.out.println(line);
                            _currentTheme = t;
                        }
                    }
                } else {
                    _currentTheme = DEFAULT_THEME;
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(_currentTheme);
    }
}
