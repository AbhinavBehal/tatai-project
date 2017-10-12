package tatai.model.theme;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {

    private static final Theme DEFAULT_THEME = Theme.SUNSET;
    private static ThemeManager _manager;
    private List<ThemeListener> _listeners;
    private Theme _currentTheme;

    private ThemeManager() {
        _listeners = new ArrayList<>();
        _currentTheme = DEFAULT_THEME;
    }

    public static ThemeManager manager() {
        if (_manager == null) {
            _manager = new ThemeManager();
        }
        return _manager;
    }

    public void addListener(ThemeListener listener) {
        _listeners.add(listener);
    }

    public void updateTheme(Theme newTheme) {
        _listeners.forEach(l -> l.updateTheme(_currentTheme, newTheme));
        _currentTheme = newTheme;
    }

    public Theme getCurrentTheme() { return _currentTheme; }
}
