package tatai.model.theme;

public enum Theme {
    SUNSET("/themes/sunset-theme.css", "#374258", "#fa584d"),
    LABORATORY("/themes/laboratory-theme.css", "#dddddd", "#403f63"),
    SUBLIME("/themes/sublime-theme.css", "#444444", "#ffe047"),
    PLACEHOLDER("/themes/placeholder-theme.css", "#222222", "#dddddd");


    private String _url;
    private String _bg;
    private String _fg;

    Theme(String url, String hexBackground, String hexForeground) {
        _url = url;
        _bg = hexBackground;
        _fg = hexForeground;
    }

    @Override
    public String toString() {
        return _url;
    }

    public String hexBackground() {
        return _bg;
    }

    public String hexForeground() {
        return _fg;
    }
}
