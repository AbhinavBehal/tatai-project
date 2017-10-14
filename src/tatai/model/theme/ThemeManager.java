package tatai.model.theme;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ThemeManager {

    private static ThemeManager _manager;
    private List<ThemeListener> _listeners;
    private Theme _currentTheme;

    /**
     * Private constructor for singleton ThemeManager instance calls setDefaultTheme().
     */
    private ThemeManager() {
        _listeners = new ArrayList<>();
        setDefaultTheme();
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
    private void setDefaultTheme() {
        try (BufferedReader reader = new BufferedReader(new FileReader(getClass().getResource("/data.txt").getPath()))) {
            String line;

            if ((line = reader.readLine()) != null) {
                for (Theme t : Theme.values()) {
                    if (line.equals(t.simpleName())) {
                        System.out.println(line);
                        _currentTheme = t;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
