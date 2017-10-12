package tatai.model.statistics;

import javafx.scene.chart.XYChart;
import javafx.util.Pair;
import tatai.model.generator.Difficulty;
import tatai.model.generator.Module;
import tatai.util.Triple;

import java.util.*;

public class StatsManager {

    public static void main(String[] args) {
        List<Integer> scores;
        Pair<Module, Difficulty> TH = new Pair<>(Module.TEST, Difficulty.HARD);
        manager().populateScores(TH, new ArrayList<>());
        scores = _manager._scores.get(TH);
        scores.add(8);
        scores.add(4);
        scores.add(5);
        scores.add(3);
        scores.add(6);
        scores.add(9);
        _manager.getScores(Module.TEST, Difficulty.HARD);
    }

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
        _listeners.forEach(l -> l.updateScore(list, score));
    }

    public void setStatistic(Module module, Difficulty difficulty, Statistic statistic, double value) {
        Triple<Module, Difficulty, Statistic> triple = new Triple<>(module, difficulty, statistic);
        _statistics.put(triple, value);
    }

    public XYChart.Series<Number, Number> getScores(Module module, Difficulty difficulty) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        int i = 1;
        for (int score : _scores.get(new Pair<>(module, difficulty))) {
//            System.out.println(i + ", " + score);
            series.getData().add(new XYChart.Data<>(i, score));
            i++;
        }

        return series;
    }

    public double getStatistic(Module module, Difficulty difficulty, Statistic statistic) {
        return _statistics.get(new Triple<>(module, difficulty, statistic));
    }

}
