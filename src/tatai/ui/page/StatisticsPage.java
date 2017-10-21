package tatai.ui.page;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.Pair;
import tatai.model.generator.Difficulty;
import tatai.model.generator.Module;
import tatai.model.statistics.Statistic;
import tatai.model.statistics.StatsManager;
import tatai.ui.Main;
import tatai.ui.control.IconButton;
import tatai.util.Triple;

import java.util.List;

import static tatai.model.generator.Module.PRACTICE;
import static tatai.model.generator.Module.TEST;
import static tatai.model.statistics.Statistic.MAX;

public class StatisticsPage extends Page {

    private static final String TITLE = "Statistics";

    @FXML
    private StackPane parentPane;
    @FXML
    private Pane overlay;
    @FXML
    private IconButton pieChartButton;
    @FXML
    private PieChart pieChart;
    @FXML
    private IconButton lineChartButton;
    @FXML
    private VBox moduleBox;
    @FXML
    private VBox containerBox;
    @FXML
    private Button practice;
    @FXML
    private Button test;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    public StatisticsPage() {
        loadFXML(getClass().getResource("Statistics.fxml"));
    }

    public void initialize() {

        pieChartButton.setText("\nScores\nOverview");
        lineChartButton.setText("\nDetailed\nStatistics");

        parentPane.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            if (e.getPickResult().getIntersectedNode() == null || e.getButton() != MouseButton.PRIMARY) return;

            if (e.getPickResult().getIntersectedNode() != pieChart && pieChart.isVisible()) {
                // Don't do anything if during transition
                if (pieChart.getOpacity() != 1) return;
                showChart(false);
            }
        });

        pieChart.setData(getTotalScores());
        pieChartButton.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)){
                showChart(true);
            }
        });

        lineChartButton.setOnMouseEntered(e -> {
            if (lineChartButton.isVisible()){
                showModes(true);
            }
        });

        containerBox.setOnMouseExited(e -> {
            if (moduleBox.isVisible()) {
                // Don't do anything if during transition
                // NOTE: this will cause state to be stuck in container box even if
                // the mouse is outside, this is due to user moving over the region
                // too quickly.
                if (!lineChartButton.isVisible()) {
                    showModes(false);
                }
            }
        });

        practice.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                Main.pushPage(new DetailedStatsPage(PRACTICE));
            }
        });

        test.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                Main.pushPage(new DetailedStatsPage(TEST));
            }
        });

        // possibly change to a set number of points, 10/20/50 or let user choose
        barChart.getData().addAll(getTopScores());
    }

    private void showChart(boolean show) {
        FadeTransition overlayFade = new FadeTransition(Duration.millis(100), overlay);
        FadeTransition pieChartFade = new FadeTransition(Duration.millis(100), pieChart);
        SequentialTransition st;

        if (show) {
            overlay.setVisible(true);
            overlayFade.setFromValue(0);
            overlayFade.setToValue(0.8);

            pieChart.setVisible(true);
            pieChartFade.setFromValue(0);
            pieChartFade.setToValue(1);

            st = new SequentialTransition(overlayFade, pieChartFade);
        } else {
            pieChartFade.setFromValue(1);
            pieChartFade.setToValue(0);

            overlayFade.setFromValue(0.8);
            overlayFade.setToValue(0);

            st = new SequentialTransition(pieChartFade, overlayFade);
        }

        st.play();
        st.setOnFinished(e -> {
            if (!show) {
                pieChart.setVisible(false);
                overlay.setVisible(false);
            }
        });
    }

    private ObservableList<PieChart.Data> getTotalScores() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        List<Pair<String, Double>> dataList = StatsManager.manager().getTotalCorrect();
        dataList.forEach(d -> pieChartData.add(new PieChart.Data(d.getKey(), d.getValue())));
        return pieChartData;
    }

    private ObservableList<XYChart.Series<String, Number>> getTopScores() {
        ObservableList<XYChart.Series<String, Number>> series = FXCollections.observableArrayList();
        List<Pair<String, Double>> dataList = StatsManager.manager().getTopScores();
        dataList.forEach(d -> {
            XYChart.Series<String, Number> totalSeries = new XYChart.Series<>();
            totalSeries.setName(d.getKey());
            totalSeries.getData().add(new XYChart.Data<>("", d.getValue()));
            series.add(totalSeries);
        });

        return series;
    }

    private void showModes(boolean show) {
        FadeTransition iconFade = new FadeTransition(Duration.millis(100), lineChartButton);
        FadeTransition modeFade = new FadeTransition(Duration.millis(100), moduleBox);
        SequentialTransition st;

        if (show) {
            iconFade.setFromValue(1);
            iconFade.setToValue(0);

            moduleBox.setVisible(true);
            modeFade.setFromValue(0);
            modeFade.setToValue(1);

            st = new SequentialTransition(iconFade, modeFade);
        } else {
            lineChartButton.setVisible(true);
            iconFade.setFromValue(0);
            iconFade.setToValue(1);

            modeFade.setFromValue(1);
            modeFade.setToValue(0);

            st = new SequentialTransition(modeFade, iconFade);
        }

        st.play();
        st.setOnFinished(e -> {
            if (show) {
                lineChartButton.setVisible(false);
            } else {
                moduleBox.setVisible(false);
            }
        });
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

}
