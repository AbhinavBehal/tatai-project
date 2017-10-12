package tatai.ui.page;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.Pair;
import tatai.model.generator.Difficulty;
import tatai.model.generator.Module;
import tatai.model.statistics.StatsManager;
import tatai.ui.control.IconButton;

import java.util.ArrayList;

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
    private VBox modeBox;
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

        pieChart.setData(testPiePop());
        pieChartButton.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)){
                showChart(true);
            }
        });
        parentPane.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            if (e.getPickResult().getIntersectedNode() == null || e.getButton() != MouseButton.PRIMARY) return;

            if (e.getPickResult().getIntersectedNode() != pieChart && pieChart.isVisible()) {
                // Don't do anything if during transition
                if (pieChart.getOpacity() != 1) return;
                showChart(false);
            }
        });

        lineChartButton.setOnMouseEntered(e -> {
            if (lineChartButton.isVisible()){
                showModes(true);
            }
        });

        modeBox.setOnMouseExited(e -> {
            if (modeBox.isVisible()) {
                showModes(false);
            }
        });

        testBarPop();

        // possibly change to a set number of points, 10/20/50 or let user choose
        barChart.getData().add(StatsManager.manager().getScores(10));
        barChart.getData().get(0).getData().forEach(System.out::println);
    }

    private ObservableList<PieChart.Data> testPiePop() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("January", 100),
                new PieChart.Data("February", 200),
                new PieChart.Data("March", 50),
                new PieChart.Data("April", 75),
                new PieChart.Data("May", 110),
                new PieChart.Data("June", 300),
                new PieChart.Data("July", 111),
                new PieChart.Data("August", 30),
                new PieChart.Data("September", 75),
                new PieChart.Data("October", 55),
                new PieChart.Data("November", 225),
                new PieChart.Data("December", 99));

        pieChart.setTitle("Monthly Record");
        return pieChartData;
    }

    private void testBarPop() {
        Pair<Module, Difficulty> TH = new Pair<>(Module.TEST, Difficulty.HARD);
        StatsManager m = StatsManager.manager();
        m.populateScores(TH, new ArrayList<>());
        m.updateScore(TH, 8);
        m.updateScore(TH, 4);
        m.updateScore(TH, 5);
        m.updateScore(TH, 3);
        m.updateScore(TH, 6);
        m.updateScore(TH, 9);
    }

    private void showChart(boolean show) {
        FadeTransition overlayFade = new FadeTransition(Duration.millis(100), overlay);
        FadeTransition pieChartFade = new FadeTransition(Duration.millis(100), pieChart);
        SequentialTransition st;

        if (show) {
            overlay.setVisible(true);
            overlayFade.setFromValue(0);
            overlayFade.setToValue(0.4);

            pieChart.setVisible(true);
            pieChartFade.setFromValue(0);
            pieChartFade.setToValue(1);

            st = new SequentialTransition(overlayFade, pieChartFade);
        } else {
            pieChartFade.setFromValue(1);
            pieChartFade.setToValue(0);

            overlayFade.setFromValue(0.4);
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

    private void showModes(boolean show) {
        FadeTransition iconFade = new FadeTransition(Duration.millis(100), lineChartButton);
        FadeTransition modeFade = new FadeTransition(Duration.millis(100), modeBox);
        SequentialTransition st;

        if (show) {
            iconFade.setFromValue(1);
            iconFade.setToValue(0);

            modeBox.setVisible(true);
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
                modeBox.setVisible(false);
            }
        });
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public void onOptionsButtonPressed() {

    }
}
