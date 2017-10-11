package tatai.model.theme;

import tatai.model.statistics.ScoreListener;
import tatai.model.statistics.StatsManager;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {

    private static ThemeManager _manager;
    private List<ThemeListener> _listeners;
    private Theme _currentTheme;

    private ThemeManager() {
        _listeners = new ArrayList<>();
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

    public void setTheme(Theme theme) {
        _currentTheme = theme;
    }

    public void updateTheme(Theme newTheme) {
        for (ThemeListener l : _listeners) {
            l.updateTheme(_currentTheme, newTheme);
        }
        _currentTheme = newTheme;
    }
}
