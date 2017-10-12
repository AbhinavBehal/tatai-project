package tatai.model.statistics;

import javafx.scene.chart.XYChart;
import javafx.util.Pair;
import tatai.model.generator.Difficulty;
import tatai.model.generator.Module;
import tatai.util.Triple;

import java.util.*;

import static tatai.model.generator.Difficulty.EASY;
import static tatai.model.generator.Difficulty.HARD;
import static tatai.model.generator.Module.PRACTICE;

public class StatsManager {

    public static void main(String[] args) {
        System.out.println("TBD");
    }

    private static StatsManager _manager;
    private List<ScoreListener> _listeners;
    private Map<Pair<Module, Difficulty>, List<Integer>> _scoreLists;
    private Map<Triple<Module, Difficulty, Statistic>, Double> _statistics;

    private StatsManager() {
        _listeners = new ArrayList<>();
        _scoreLists = new HashMap<>();
        _statistics = new HashMap<>();

        // for testing
        testPopulation();
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
        if (!_scoreLists.containsKey(list)) {
            _scoreLists.put(list, scores);
        }
        _scoreLists.get(list).addAll(scores);
    }

    public void updateScore(Pair<Module, Difficulty> list, int score) {
        _scoreLists.get(list).add(score);
        _listeners.forEach(l -> l.updateScore(list, score));
    }

    // TODO to be changed with new bar chart
    public XYChart.Series<String, Number> getAllScores(int n) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        return series;
    }

    public XYChart.Series<Number, Number> getScoreList(Module module, Difficulty difficulty) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        int i = 1;
        for (int score : _scoreLists.get(new Pair<>(module, difficulty))) {
            series.getData().add(new XYChart.Data<>(i++, score));
        }

        return series;
    }

    public double getStatistic(Module module, Difficulty difficulty, Statistic statistic) {
        return _statistics.get(new Triple<>(module, difficulty, statistic));
    }

    // private function to simulate data
    private void testPopulation() {
        Pair<Module, Difficulty> pe = new Pair<>(PRACTICE, EASY);
        Pair<Module, Difficulty> ph = new Pair<>(PRACTICE, HARD);
        Pair<Module, Difficulty> te = new Pair<>(PRACTICE, EASY);
        Pair<Module, Difficulty> th = new Pair<>(PRACTICE, HARD);
        StatsManager m = StatsManager.manager();
        m.populateScores(pe, new ArrayList<>());
        m.updateScore(pe, 8);
        m.updateScore(pe, 5);
        m.updateScore(pe, 3);
        m.updateScore(pe, 6);
        m.updateScore(pe, 5);
        m.updateScore(pe, 9);
        m.updateScore(pe, 9);

        m.populateScores(ph, new ArrayList<>());
        m.updateScore(ph, 5);
        m.updateScore(ph, 3);
        m.updateScore(ph, 6);
        m.updateScore(ph, 4);
        m.updateScore(ph, 5);
        m.updateScore(ph, 3);
        m.updateScore(ph, 6);

        m.populateScores(te, new ArrayList<>());
        m.updateScore(ph, 5);
        m.updateScore(ph, 3);
        m.updateScore(ph, 5);
        m.updateScore(ph, 3);
        m.updateScore(ph, 6);
        m.updateScore(ph, 6);
        m.updateScore(ph, 4);

        m.populateScores(th, new ArrayList<>());
        m.updateScore(pe, 3);
        m.updateScore(pe, 6);
        m.updateScore(pe, 4);
        m.updateScore(pe, 4);
        m.updateScore(pe, 3);
        m.updateScore(pe, 6);
        m.updateScore(pe, 4);
        m.updateScore(pe, 5);
        m.updateScore(pe, 3);
        m.updateScore(pe, 6);
    }
}
