package tatai.model.statistics;

import tatai.model.generator.Difficulty;
import tatai.model.generator.Module;

import java.time.LocalDate;

/**
 * A class that wraps metadata about a users score, such as the date the score was achieved,
 * the module and difficulty it was obtained in, and the score itself.
 */
public class Score {
    private LocalDate date;
    private Module module;
    private Difficulty difficulty;
    private int score;

    public Score(LocalDate date, Module module, Difficulty difficulty, int score) {
        this.date = date;
        this.module = module;
        this.difficulty = difficulty;
        this.score = score;
    }

    public LocalDate date() { return date; }
    public Module module() { return module; }
    public Difficulty difficulty() { return difficulty; }
    public int score() { return score; }
}
