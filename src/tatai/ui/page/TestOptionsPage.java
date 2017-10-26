package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import tatai.model.generator.*;
import tatai.model.statistics.ScoreListener;
import tatai.model.statistics.StatsManager;
import tatai.ui.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TestOptions Page controller, handles user actions on TestOptions page,
 * namely for choosing difficulty and operators to include in the equations.
 */
public class TestOptionsPage extends Page implements ScoreListener {

    @FXML
    private Button easyButton;
    @FXML
    private Button hardButton;
    @FXML
    private Label hardLabel;
    @FXML
    private Button customButton;
    @FXML
    private HBox operationsView;

    private static final String TITLE = "Test Yourself";
    private Map<Operator, CheckBox> _operatorMap;
    private int _numOperations;

    public TestOptionsPage() {
        _operatorMap = new HashMap<>();
        loadFXML(getClass().getResource("TestOptions.fxml"));
        StatsManager.manager().addListener(this);
    }

    public void initialize() {
        for (Operator op : Operator.values()) {
            // Fill the operator Map with the checkboxes for each operator
            String label = op.name().substring(0, 1).toUpperCase() + op.name().substring(1).toLowerCase();
            CheckBox checkBox = new CheckBox(label);
            checkBox.getStyleClass().add("h5");

            checkBox.selectedProperty().addListener((obs, oldValue, newValue) -> onOperationSelected(newValue));

            _operatorMap.put(op, checkBox);
            operationsView.getChildren().add(checkBox);
        }
        _operatorMap.get(Operator.ADDITION).setSelected(true);

        easyButton.setOnMouseClicked(e -> {
            Generator generator = new EquationGenerator(Difficulty.EASY, getSelectedOperations());
            Main.pushPage(new PronunciationPage("Test - Easy", generator));
        });

        hardButton.setOnMouseClicked(e ->{
            Generator generator = new EquationGenerator(Difficulty.HARD, getSelectedOperations());
            Main.pushPage(new PronunciationPage("Test - Hard", generator));
        });

        // Disables hard mode if it is not unlocked yet
        hardButton.setDisable(!StatsManager.manager().testUnlocked());
        hardLabel.setDisable(!StatsManager.manager().testUnlocked());

        customButton.setOnMouseClicked(e -> Main.pushPage(new CustomOptionsPage()));
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    // Listener method used to see if hard becomes unlocked by scoring 8 or above
    @Override
    public void updateScore(Module module, Difficulty difficulty, int lastScore) {
        if (lastScore > 7) {
            hardButton.setDisable(false);
            hardLabel.setDisable(false);
        }
    }

    private void onOperationSelected(boolean selected) {
        _numOperations = selected ? _numOperations + 1 : _numOperations - 1;
    }

    private List<Operator> getSelectedOperations() {
        List<Operator> operations = new ArrayList<>();
        for (Map.Entry<Operator, CheckBox> entry : _operatorMap.entrySet()) {
            if (entry.getValue().isSelected() || _numOperations == 0) {
                operations.add(entry.getKey());
            }
        }
        return operations;
    }
}
