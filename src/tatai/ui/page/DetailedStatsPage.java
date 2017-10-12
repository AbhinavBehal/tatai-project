package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;
import tatai.model.generator.Difficulty;
import tatai.model.generator.Module;
import tatai.model.statistics.StatsManager;

import java.util.ArrayList;

import static tatai.model.generator.Difficulty.*;

public class DetailedStatsPage extends Page {

    private static final String TITLE = "Detailed Statistics";
    private Module _module;

    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private NumberAxis xAxis;

    public DetailedStatsPage(Module module) {
        _module = module;
        loadFXML(getClass().getResource("DetailedStats.fxml"));
    }

    public void initialize() {
        testLinePop();
        XYChart.Series<Number, Number> easySeries = StatsManager.manager().getScoreList(_module, EASY);
        XYChart.Series<Number, Number> hardSeries = StatsManager.manager().getScoreList(_module, HARD);
        easySeries.setName("Easy");
        hardSeries.setName("Hard");
        xAxis.setLowerBound(1);
        if (easySeries.getData().size() < hardSeries.getData().size()) {
            xAxis.setUpperBound(hardSeries.getData().size());
        } else {
            xAxis.setUpperBound(easySeries.getData().size());
        }
        lineChart.getData().addAll(easySeries, hardSeries);
    }

    private void testLinePop() {
        Pair<Module, Difficulty> me = new Pair<>(_module, EASY);
        Pair<Module, Difficulty> mh = new Pair<>(_module, HARD);
        StatsManager m = StatsManager.manager();
        m.populateScores(me, new ArrayList<>());
        m.updateScore(me, 8);
        m.updateScore(me, 4);
        m.updateScore(me, 5);
        m.updateScore(me, 3);
        m.updateScore(me, 6);
        m.updateScore(me, 4);
        m.updateScore(me, 5);
        m.updateScore(me, 3);
        m.updateScore(me, 6);
        m.updateScore(me, 9);
        m.updateScore(me, 9);

        m.populateScores(mh, new ArrayList<>());
        m.updateScore(mh, 5);
        m.updateScore(mh, 3);
        m.updateScore(mh, 6);
        m.updateScore(mh, 4);
        m.updateScore(mh, 5);
        m.updateScore(mh, 3);
        m.updateScore(mh, 6);
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public void onOptionsButtonPressed() {

    }
}
