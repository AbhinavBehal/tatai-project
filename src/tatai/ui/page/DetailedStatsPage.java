package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import tatai.model.generator.Module;
import tatai.model.statistics.StatsManager;


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
        XYChart.Series<Number, Number> easySeries = StatsManager.manager().getScoreList(_module, EASY);
        XYChart.Series<Number, Number> hardSeries = StatsManager.manager().getScoreList(_module, HARD);
        easySeries.setName("Easy");
        hardSeries.setName("Hard");
        xAxis.setLowerBound(1);
        if (easySeries.getData().size() < hardSeries.getData().size()) {
            xAxis.setUpperBound(hardSeries.getData().size());
            xAxis.setTickUnit((int) xAxis.getUpperBound() / 10);
        } else {
            xAxis.setUpperBound(easySeries.getData().size());
            xAxis.setTickUnit((int) xAxis.getUpperBound() / 10);
        }
        lineChart.getData().add(easySeries);
        lineChart.getData().add(hardSeries);
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public void onOptionsButtonPressed() {

    }
}
