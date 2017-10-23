package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import tatai.model.generator.Difficulty;
import tatai.model.generator.EquationGenerator;
import tatai.model.generator.Generator;
import tatai.model.generator.Operator;
import tatai.ui.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestOptionsPage extends Page {

    @FXML
    private Button easyButton;
    @FXML
    private Button hardButton;
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

        customButton.setOnMouseClicked(e -> Main.pushPage(new CustomOptionsPage()));
    }

    @Override
    public String getTitle() {
        return TITLE;
    }


    private void onOperationSelected(boolean selected) {
        _numOperations = selected ? _numOperations + 1 : _numOperations - 1;
        if (_numOperations == 0) {
            // If no operations are selected, select the addition operation by default
            _operatorMap.get(Operator.ADDITION).setSelected(true);
        }
    }

    private List<Operator> getSelectedOperations() {
        List<Operator> operations = new ArrayList<>();
        for (Map.Entry<Operator, CheckBox> entry : _operatorMap.entrySet()) {
            if (entry.getValue().isSelected()) {
                operations.add(entry.getKey());
            }
        }
        return operations;
    }
}
