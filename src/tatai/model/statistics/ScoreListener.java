package tatai.model.statistics;

import javafx.util.Pair;
import tatai.model.generator.Difficulty;
import tatai.model.generator.Module;

public interface ScoreListener {

    /**
     * Function called when a new score is obtained.
     * @param mode the mode the score belongs to, where module is either
     *             practice or test, and difficulty is either easy or hard.
     * @param lastScore the last score the user achieved.
     */
    void updateScore(Pair<Module, Difficulty> mode, int lastScore);
}
