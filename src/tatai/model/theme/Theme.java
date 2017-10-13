package tatai.model.theme;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

public enum Theme {
    SUNSET("/themes/sunset-theme.css"),
    LABORATORY("/themes/laboratory-theme.css"),
    SUBLIME("/themes/sublime-theme.css"),
    PLACEHOLDER("/themes/placeholder-theme.css");


    private String _url;
    private String _bg;
    private String _fg;

    Theme(String url) {
        _url = url;

        try (BufferedReader reader = new BufferedReader(new FileReader(getClass().getResource(url).getPath()))) {
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
