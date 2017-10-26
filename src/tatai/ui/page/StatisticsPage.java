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
import javafx.util.Duration;
import javafx.util.Pair;
import tatai.model.statistics.StatsManager;
import tatai.ui.Main;
import tatai.ui.control.IconButton;

import java.util.List;

import static tatai.model.generator.Module.PRACTICE;
import static tatai.model.generator.Module.TEST;

/**
 * Statistics Page controller, handles user actions on Statistics page, namely
 * showing the PieChart and choosing which DetailedStatistics page to show.
 */
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

        pieChartButton.setText("Scores Overview");

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

        barChart.getData().addAll(getTopScores());
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public void onBackButtonPressed() {
        // If PieChart is open, close that instead of going back from the page.
        if (overlay.isVisible()) {
            showChart(false);
        } else {
            Main.popPage();
        }
    }

    // Private helper function used for the transition of showing the PieChart
    private void showChart(boolean show) {
        FadeTransition overlayFade = new FadeTransition(Duration.millis(150), overlay);
        FadeTransition pieChartFade = new FadeTransition(Duration.millis(150), pieChart);
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

    // Private helper function used to get score data from StatsManager and convert
    // to PieChart.Data for PieChart to use.
    private ObservableList<PieChart.Data> getTotalScores() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        List<Pair<String, Double>> dataList = StatsManager.manager().getTotalCorrect();

        dataList.forEach(d -> pieChartData.add(new PieChart.Data(d.getKey(), d.getValue())));

        return pieChartData;
    }

    // Private helper function used to get score data from StatsManager and convert
    // to XYChart.Series for BarChart to use.
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
}
