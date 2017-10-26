package tatai.model.statistics;

import tatai.model.generator.Difficulty;
import tatai.model.generator.Module;

/**
 * Interface used as a listener such that subclasses are notified of score changes.
 */
public interface ScoreListener {

    /**
     * Method called when a new score is obtained.
     * @param module the module the score belongs to, either practice or test.
     * @param difficulty the difficulty of the module, either easy or hard.
     * @param lastScore the last score the user achieved.
     */
    void updateScore(Module module, Difficulty difficulty, int lastScore);
}
