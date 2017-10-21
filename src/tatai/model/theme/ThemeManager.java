package tatai.model.theme;

import tatai.model.data.DataManager;

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
     * Static method to get the default theme
     * @return the default theme
     */
    public static Theme defaultTheme() { return DEFAULT_THEME; }

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
        DataManager.manager().updateTheme(newTheme);
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
        _currentTheme = DataManager.manager().getTheme();
    }
}
