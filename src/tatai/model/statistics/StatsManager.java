package tatai.model.statistics;

import javafx.util.Pair;
import tatai.model.generator.Difficulty;
import tatai.model.generator.Module;
import tatai.util.Triple;

import java.util.*;

public class StatsManager {

    private static StatsManager _manager;
    private List<ScoreListener> _listeners;
    private Map<Pair<Module, Difficulty>, List<Integer>> _scores;
    private Map<Triple<Module, Difficulty, Statistic>, Double> _statistics;

    private StatsManager() {
        _listeners = new ArrayList<>();
        _scores = new HashMap<>();
        _statistics = new HashMap<>();

    }

    public static StatsManager manager() {
        if (_manager == null) {
            _manager = new StatsManager();
        }
        return _manager;
    }

    public void addListener(ScoreListener listener) {
        _listeners.add(listener);
    }

    public void populateScores(Pair<Module, Difficulty> list, List<Integer> scores) {
        if (!_scores.containsKey(list)) {
            _scores.put(list, scores);
        }
        _scores.get(list).addAll(scores);
    }

    /**
     *
     * @param list
     * @param score
     */
    public void updateScore(Pair<Module, Difficulty> list, int score) {
        _scores.get(list).add(score);

        for (ScoreListener l : _listeners) {
            l.updateScore(list, score);
        }
    }

    public void setStatistic(Module module, Difficulty difficulty, Statistic statistic, double value) {
        Triple<Module, Difficulty, Statistic> triple = new Triple<>(module, difficulty, statistic);
        _statistics.put(triple, value);
    }

    public double getStatistic(Module module, Difficulty difficulty, Statistic statistic) {
        return _statistics.get(new Triple<>(module, difficulty, statistic));
    }

}
