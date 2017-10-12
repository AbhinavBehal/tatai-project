package tatai.model.statistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
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
        scores = _manager._scoreLists.get(TH);
        scores.add(8);
        scores.add(4);
        scores.add(5);
        scores.add(3);
        scores.add(6);
        scores.add(9);
        _manager.getScoreList(Module.TEST, Difficulty.HARD);
    }

    private static StatsManager _manager;
    private List<ScoreListener> _listeners;
    private List<Triple<Module, Difficulty, Integer>> _scores;
    private Map<Pair<Module, Difficulty>, List<Integer>> _scoreLists;
    private Map<Triple<Module, Difficulty, Statistic>, Double> _statistics;

    private StatsManager() {
        _listeners = new ArrayList<>();
        _scores = new ArrayList<>();
        _scoreLists = new HashMap<>();
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
//        System.out.println(list);
//        System.out.println(_scoreLists.keySet());
        if (!_scoreLists.containsKey(list)) {
            _scoreLists.put(list, scores);
        }
        _scoreLists.get(list).addAll(scores);
    }

    /**
     *
     * @param list
     * @param score
     */
    public void updateScore(Pair<Module, Difficulty> list, int score) {
        _scores.add(new Triple<>(list.getKey(), list.getValue(), score));
        _scoreLists.get(list).add(score);
        _listeners.forEach(l -> l.updateScore(list, score));
//        System.out.println(_scoreLists.entrySet());
    }

    /*
    public void setStatistic(Module module, Difficulty difficulty, Statistic statistic, double value) {
        Triple<Module, Difficulty, Statistic> triple = new Triple<>(module, difficulty, statistic);
        _statistics.put(triple, value);
    }*/

    public XYChart.Series<String, Number> getAllScores(int n) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        int i = n < _scores.size() ? _scores.size() - n : 0;
        while (i < _scores.size()) {
            String module = _scores.get(i).Key() + " - " + _scores.get(i).Item();
//            System.out.println(module + " " + _scores.get(i).Val());
            series.getData().add(new XYChart.Data<>(module, _scores.get(i++).Val()));
        }

//        System.out.println(series.getData());
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

}
