package tatai.ui.page;

import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import tatai.model.generator.Difficulty;
import tatai.model.generator.Module;
import tatai.model.statistics.StatsManager;

import java.util.List;

import static tatai.model.generator.Difficulty.EASY;
import static tatai.model.generator.Difficulty.HARD;

public class DetailedStatsPage extends Page {

    private final String TITLE;
    private Module _module;

    @FXML
    private ColumnConstraints lineChartCol;
    @FXML
    private ColumnConstraints easyListCol;
    @FXML
    private ColumnConstraints hardListCol;
    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private ListView<String> easyList;
    @FXML
    private ListView<String> hardList;
    @FXML
    private ToggleGroup recentN;
    @FXML
    private RadioButton tenButton;
    @FXML
    private TextField customField;
    @FXML
    private VBox easyBox;
    @FXML
    private VBox hardBox;
    @FXML
    private Button expandButton;

    public DetailedStatsPage(Module module) {
        _module = module;
        TITLE = "Detailed Statistics - " + _module;
        loadFXML(getClass().getResource("DetailedStats.fxml"));
    }

    public void initialize() {
        lineChart.setAnimated(false);
        populateList();

        lineChartCol.setPercentWidth(80);
        easyListCol.setPercentWidth(10);
        hardListCol.setPercentWidth(10);

        recentN.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton btn = (RadioButton) newValue;
            if (btn.getText().equals("All")) {
                customField.setDisable(true);
                int easySize = easyList.getItems().size();
                int hardSize = hardList.getItems().size();
                setScores(easySize > hardSize ? easySize : hardSize);
            } else if (btn.getText().isEmpty()) {
                customField.setDisable(false);
                if (!customField.getText().isEmpty()) {
                    setScores(Integer.parseInt(customField.getText()));
                }
            } else {
                customField.setDisable(true);
                int n = Integer.parseInt(btn.getText());
                setScores(n);
            }
        });
        tenButton.setSelected(true);

        customField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                customField.setText(oldValue);
            } else if (!newValue.isEmpty()) {
                setScores(Integer.parseInt(customField.getText()));
            }
        });

        expandButton.setOnAction(e -> transit(easyBox.isVisible() || hardBox.isVisible()));
    }

    /**
     * Private helper function used to set the data for the LineChart, as well
     * as setting the lower/upper bounds and increment size depending on size
     * of data. Calls getScores(Module, Difficulty, n).
     * @param n Number of items to show (up to size of data)
     */
    private void setScores(int n) {
        XYChart.Series<Number, Number> easySeries = getScores(_module, EASY,n);
        XYChart.Series<Number, Number> hardSeries = getScores(_module, HARD,n);
        easySeries.setName("Easy");
        hardSeries.setName("Hard");
        xAxis.setLowerBound(0);

        // Sets upper bound to largest series and set to increment ten times.
        if (easySeries.getData().size() < hardSeries.getData().size()) {
            xAxis.setUpperBound(hardSeries.getData().size());
            xAxis.setTickUnit(Math.ceil(xAxis.getUpperBound() / 10));
        } else {
            xAxis.setUpperBound(easySeries.getData().size());
            xAxis.setTickUnit(Math.ceil(xAxis.getUpperBound() / 10));
        }

        // If LineChart already has data, remove it before adding new data.
        if (lineChart.getData().size() > 0) {
            System.out.println(lineChart.getData());
            lineChart.getData().remove(0, lineChart.getData().size());
        }
        lineChart.getData().add(easySeries);
        lineChart.getData().add(hardSeries);
    }

    /**
     * Private helper function used to get data from StatsManager and change it
     * to XYChart.Series for line chart to use.
     * @param module Module the scores were obtained in.
     * @param difficulty Difficulty the scores were obtained in.
     * @param n Number of items to get, if there are that many.
     * @return XYChart.Series of the requested n scores.
     */
    private XYChart.Series<Number, Number> getScores(Module module, Difficulty difficulty, int n) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        List<Integer> scores = StatsManager.manager().getScoreList(module, difficulty);

        int size = scores.size();
        // If requesting more than or equal size of available data.
        if (n >= size) {
            // Get data from end of list to start so that newest data is on the right.
            for (int i = 1; i <= n && i <= size; i++) {
                series.getData().add(new XYChart.Data<>(i, scores.get(size - i)));
            }
        } else {
            // Otherwise start getting data once within the most recent n entries.
            int j = 1;
            for (int i = size; i > 0; i--) {
                if (i <= n) {
                    series.getData().add(new XYChart.Data<>(j++, scores.get(size - i)));
                }
            }
        }
        return series;
    }

    /**
     * Private helper function used to populate the ListViews.
     */
    private void populateList() {
        easyList.setItems(FXCollections.observableArrayList());
        hardList.setItems(FXCollections.observableArrayList());

        for (Difficulty difficulty : Difficulty.values()) {
            List<Integer> scoreList = StatsManager.manager().getScoreList(_module, difficulty);
            for (Integer score : scoreList) {
                if (difficulty == HARD) {
                    hardList.getItems().add(0,"" + score);
                } else if (difficulty == EASY) {
                    easyList.getItems().add(0,"" + score);
                }
            }
        }
    }

    private void transit(boolean expand) {
        if (expand) {
            easyBox.setVisible(false);
            hardBox.setVisible(false);
            easyListCol.setPercentWidth(0);
            hardListCol.setPercentWidth(0);

            Timeline tl = new Timeline(new KeyFrame(Duration.millis(300), new KeyValue(lineChartCol.percentWidthProperty(), 100)));
            tl.play();
        } else {
            Timeline tl = new Timeline(new KeyFrame(Duration.millis(300), new KeyValue(lineChartCol.percentWidthProperty(), 80)));
            tl.play();

            easyBox.setVisible(true);
            hardBox.setVisible(true);
            easyListCol.setPercentWidth(10);
            hardListCol.setPercentWidth(10);
        }
    }

    @Override
    public String getTitle() {
        return TITLE;
    }
}
