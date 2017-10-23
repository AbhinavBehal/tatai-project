package tatai.model.statistics;

import javafx.util.Pair;
import tatai.model.data.DataManager;
import tatai.model.generator.Difficulty;
import tatai.model.generator.Module;
import tatai.util.Triple;

import java.time.LocalDate;
import java.util.*;

import static tatai.model.statistics.Statistic.*;

public class StatsManager {

    private static final int MAX_SCORE = 10;
    private static final int UNLOCK_TEST_THRESHOLD = 8;
    private static final int UNLOCK_PRACTICE_THRESHOLD = 8;

    private static StatsManager _manager;
    private List<ScoreListener> _listeners;
    private Map<Triple<LocalDate, Module, Difficulty>, List<Integer>> _scoreLists;
    private Map<Triple<Module, Difficulty, Statistic>, Double> _statistics;
    private LocalDate _date;
    private boolean _practiceUnlocked;
    private boolean _testUnlocked;

    /**
     * Private constructor for singleton StatsManager instance, sets current date and
     * reads from data file for previous user data.
     */
    private StatsManager() {
        _listeners = new ArrayList<>();
        _scoreLists = new TreeMap<>();
        _statistics = new HashMap<>();
        _date = LocalDate.now();
        initializeStats();
        DataManager.manager().getScores().forEach(s -> addScore(s.date(), s.module(), s.difficulty(), s.score()));
    }

    /**
     * Public method used to get the singleton instance.
     * @return singleton StatsManager.
     */
    public static StatsManager manager() {
        if (_manager == null) {
            _manager = new StatsManager();
        }
        return _manager;
    }

    /**
     * Public method used to add listeners to StatsManager when scores change.
     * TODO: Discuss necessity
     * @param listener An object of class which implements ScoreListener.
     */
    public void addListener(ScoreListener listener) {
        _listeners.add(listener);
    }

    /**
     * Public method used to update scores managed by StatsManager singleton, calls
     * the inherited updateScore method on all ScoreListeners.
     * @param date Date to add score to.
     * @param module Module the score was obtained in.
     * @param difficulty Difficulty the score was obtained in.
     * @param score The final score obtained.
     */
    public void updateScore(LocalDate date, Module module, Difficulty difficulty, int score) {
        if (date == null || module == null || difficulty == null) return;

        addScore(date, module, difficulty, score);
        DataManager.manager().updateScore(new Score(date, module, difficulty, score));

        _listeners.forEach(l -> l.updateScore(module, difficulty, score));
    }

    /**
     * Public method used to get the lifetime maximum scores obtained in each mode,
     * currently returns data only usable for an XYChart.
     * @return List of each mode as a string and the maximum score.
     */
    public List<Pair<String, Double>> getTopScores() {
        List<Pair<String, Double>> topScores = new ArrayList<>();
        for (Module module : Module.values()) {
            for (Difficulty difficulty : Difficulty.values()) {
                String name = (module.toString() + "-" + difficulty.toString());
                Triple<Module, Difficulty, Statistic> maxStat = new Triple<>(module, difficulty, MAX);
                Pair<String, Double> mdMaxScore = new Pair<>(name, _statistics.get(maxStat));
                topScores.add(mdMaxScore);
            }
        }

        return topScores;
    }

    /**
     * Public method used to get the lifetime correct scores obtained in each mode,
     * currently returns data only usable for a PieChart.
     * @return List of total scores for each mode as a string and total answers
     * correct (and incorrect? - discuss).
     */
    public List<Pair<String,Double>> getTotalCorrect() {
        List<Pair<String, Double>> totalScores = new ArrayList<>();
        double totalCorrect = 0;
        double total = 0;
        for (Module module : Module.values()) {
            for (Difficulty difficulty : Difficulty.values()) {
                double correct = _statistics.get(new Triple<>(module, difficulty, CORRECT));
                totalScores.add(new Pair<>(module + " " + difficulty, correct));
                totalCorrect += correct;
                total += _statistics.get(new Triple<>(module, difficulty, TOTAL));
            }
        }

        totalScores.add(new Pair<>("Attempted", total - totalCorrect));

        return totalScores;
    }

    /**
     * Public method used to get the list scores obtained in a given mode.
     * @param module Module of the list of scores.
     * @param difficulty Difficulty of the list of scores.
     * @return List of the scores requested.
     */
    public List<Integer> getScoreList(Module module, Difficulty difficulty) {
        List<Integer> scoreList = new ArrayList<>();
        _scoreLists.forEach((k, v) -> {
            if (k.second() == module && k.third() == difficulty) {
                scoreList.addAll(v);
            }
        });

        return scoreList;
    }

    /**
     * Public method used to check if PRACTICE Module's HARD Difficulty has been
     * unlocked.
     * @return boolean of whether or not HARD is unlocked.
     */
    public boolean practiceUnlocked() {
        return _practiceUnlocked;
    }

    /**
     * Public method used to check if TEST Module's HARD Difficulty has been
     * unlocked.
     * @return boolean of whether or not HARD is unlocked.
     */
    public boolean testUnlocked() {
        return _testUnlocked;
    }

    /**
     * Private method used to add newest score to appropriate list and update
     * statistics accordingly.
     * @param date Date to add score to.
     * @param module Module the score was obtained in.
     * @param difficulty Difficulty the score was obtained in.
     * @param score The final score obtained.
     */
    private void addScore(LocalDate date, Module module, Difficulty difficulty, int score) {
        if (date == null || module == null || difficulty == null) return;

        Triple<LocalDate, Module, Difficulty> key = new Triple<>(date, module, difficulty);

        if (!_scoreLists.containsKey(key)) {
            _scoreLists.put(key, new ArrayList<>());
        }
        _scoreLists.get(key).add(score);

        int games = _scoreLists.get(key).size();

        double average = _statistics.get(new Triple<>(module, difficulty, AVERAGE));
        average = (average * games + score) / (games + 1);

        double max = _statistics.get(new Triple<>(module, difficulty, MAX));
        max = score > max ? score : max;

        if (module == Module.PRACTICE) {
            _practiceUnlocked = max > UNLOCK_PRACTICE_THRESHOLD;
        } else if (module == Module.TEST) {
            _testUnlocked = max > UNLOCK_TEST_THRESHOLD;
        }

        double correct = _statistics.get(new Triple<>(module, difficulty, CORRECT)) + score;

        _statistics.put(new Triple<>(module, difficulty, AVERAGE), average);
        _statistics.put(new Triple<>(module, difficulty, LAST), (double) score);
        _statistics.put(new Triple<>(module, difficulty, MAX), max);
        _statistics.put(new Triple<>(module, difficulty, CORRECT), correct);
        _statistics.put(new Triple<>(module, difficulty, TOTAL), (double) games * MAX_SCORE);
    }

    /**
     * Private helper function used to initialise statistics for each list.
     */
    private void initializeStats() {
        for (Module m : Module.values()) {
            for (Difficulty d : Difficulty.values()) {
                for (Statistic s : Statistic.values()) {
                    _statistics.put(new Triple<>(m, d, s), 0.0);
                }
            }
        }
    }

    // Private method to check stats, used for testing
    private void checkem() {
        for (Module m : Module.values()) {
            for (Difficulty d : Difficulty.values()) {
                for (Statistic s : Statistic.values()) {
                    System.out.println(m + ":" + d + ":" + s + "=" + _statistics.get(new Triple<>(m, d, s)));
                }
            }
        }
    }
}
