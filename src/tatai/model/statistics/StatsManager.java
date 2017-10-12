package tatai.model.statistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;
import tatai.model.generator.Difficulty;
import tatai.model.generator.Module;
import tatai.util.Triple;

import java.util.*;

import static tatai.model.generator.Difficulty.*;
import static tatai.model.generator.Module.*;
import static tatai.model.statistics.Statistic.*;

public class StatsManager {

    private static final double MAX_SCORE = 10;

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

        Double average;
        Double max;
        Double correct;

        if ((average = _statistics.get(new Triple<>(list.getKey(), list.getValue(), AVERAGE))) == null ) {
            average = (double) score;
        } else {
            average = (average * _scoreLists.get(list).size() + score ) / (_scoreLists.get(list).size() + 1);
        }

        if ((max = _statistics.get(new Triple<>(list.getKey(), list.getValue(), MAX))) == null ) {
            max = (double) score;
        } else {
            max = max < score ? score : max;
        }

        if ((correct = _statistics.get(new Triple<>(list.getKey(), list.getValue(), CORRECT))) == null ) {
            correct = (double) score;
        }

        _statistics.put(new Triple<>(list.getKey(), list.getValue(), AVERAGE), average);
        _statistics.put(new Triple<>(list.getKey(), list.getValue(), LAST), (double) score);
        _statistics.put(new Triple<>(list.getKey(), list.getValue(), MAX), max);
        _statistics.put(new Triple<>(list.getKey(), list.getValue(), CORRECT), correct);
        _statistics.put(new Triple<>(list.getKey(), list.getValue(), TOTAL), _scoreLists.get(list).size() * MAX_SCORE);
    }

    public ObservableList<XYChart.Series<String, Number>> getTopScores() {
        ObservableList<XYChart.Series<String, Number>> series = FXCollections.observableArrayList();

        for (Module m : Module.values()) {
            for (Difficulty d : Difficulty.values()) {
                XYChart.Series<String, Number> mdMax = new XYChart.Series<>();
                Triple<Module, Difficulty, Statistic> mdMaxStat = new Triple<>(m, d, MAX);
                XYChart.Data<String, Number> mdMaxScore =
                        new XYChart.Data<>(mdMaxStat.Key() + "-" + mdMaxStat.Item(), _statistics.get(mdMaxStat));
                mdMax.getData().add(mdMaxScore);
                series.add(mdMax);
            }
        }

        series.forEach(s -> System.out.println(s.getData()));

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
        Pair<Module, Difficulty> te = new Pair<>(TEST, EASY);
        Pair<Module, Difficulty> th = new Pair<>(TEST, HARD);
        populateScores(pe, new ArrayList<>());
        updateScore(pe, 8);
        updateScore(pe, 5);
        updateScore(pe, 3);
        updateScore(pe, 6);
        updateScore(pe, 5);
        updateScore(pe, 9);
        updateScore(pe, 9);

        populateScores(ph, new ArrayList<>());
        updateScore(ph, 5);
        updateScore(ph, 3);
        updateScore(ph, 6);
        updateScore(ph, 4);
        updateScore(ph, 5);
        updateScore(ph, 3);
        updateScore(ph, 6);

        populateScores(te, new ArrayList<>());
        updateScore(te, 5);
        updateScore(te, 3);
        updateScore(te, 5);
        updateScore(te, 3);
        updateScore(te, 6);
        updateScore(te, 6);
        updateScore(te, 4);

        populateScores(th, new ArrayList<>());
        updateScore(th, 3);
        updateScore(th, 4);
        updateScore(th, 4);
        updateScore(th, 3);
        updateScore(th, 6);
        updateScore(th, 4);
        updateScore(th, 5);
        updateScore(th, 3);
        updateScore(th, 6);
        updateScore(th, 6);
    }

    // private function to check stats
    private void checkem() {
        for (Module m : Module.values()) {
            for (Difficulty d : Difficulty.values()) {
                for (Statistic s : Statistic.values()) {
                    System.out.println(_statistics.get(new Triple<>(m, d, s)));
                }
            }
        }
    }
}
