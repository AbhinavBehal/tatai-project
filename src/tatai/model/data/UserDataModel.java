package tatai.model.data;

import tatai.model.statistics.Score;
import tatai.model.theme.Theme;

import java.util.List;

class UserDataModel {
    private Theme theme;
    private List<Score> scores;

    UserDataModel(Theme theme, List<Score> scores) {
        this.theme = theme;
        this.scores = scores;
    }

    Theme getTheme() { return theme; }
    void setTheme(Theme theme) { this.theme = theme; }

    List<Score> getScores() { return scores; }
}
