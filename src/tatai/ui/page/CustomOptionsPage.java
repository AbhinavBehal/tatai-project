package tatai.ui.page;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import tatai.model.generator.CustomGenerator;
import tatai.model.generator.Generator;
import tatai.ui.Main;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CustomOptionsPage extends Page {

    @FXML
    private ListView<String> questionsView;
    @FXML
    private Button loadButton;
    @FXML
    private Button startButton;

    private static final String TITLE = "Load your questions";
    private FileChooser _fileChooser;
    private List<String> _questions;
    private List<Integer> _answers;

    public CustomOptionsPage() {
        _questions = new ArrayList<>();
        _answers = new ArrayList<>();

        _fileChooser = new FileChooser();
        _fileChooser.setTitle("Select your custom question file");
        _fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        _fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text files", "*.txt")
        );
        loadFXML(getClass().getResource("CustomOptions.fxml"));
    }

    public void initialize() {
        startButton.setDisable(true);

        loadButton.setOnAction(e -> {
            File questionFile = _fileChooser.showOpenDialog(questionsView.getScene().getWindow());
            processFile(questionFile);
            startButton.setDisable(questionsView.getItems().isEmpty());
        });

        startButton.setOnAction(e -> {
            Generator generator = new CustomGenerator(_questions, _answers);
            Main.pushScene(new PronunciationPage("Custom Questions", generator));
        });
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public void onOptionsButtonPressed() { }


    private void processFile(File questionFile) {
        if (questionFile == null) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(questionFile))) {
            _questions.clear();
            _answers.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                // Check that the number of left and right brackets are equal
                boolean valid = true;
                line = line.replaceAll("\\s+", "");
                Stack<Character> bracketStack = new Stack<>();
                char[] chars = line.toCharArray();
                for (char c : chars) {
                    if (c == '(') {
                        bracketStack.push(c);
                    } else if (c == ')') {
                        if (bracketStack.size() > 0) {
                            bracketStack.pop();
                        } else {
                            valid = false;
                            break;
                        }
                    }
                }
                valid = valid && bracketStack.size() == 0;
                if (!valid) continue;

                // Check that there is exactly one equals sign in the equation
                long numEquals = line.chars().filter(c -> c == '=').count();
                if (numEquals != 1) continue;

                // Check that each character in the equation is a bracket, number or operator
                // and check that every operator is followed by either a number or bracket
                boolean wasOperator = false;
                for (char c : chars) {
                    boolean numberOrBracket = Character.isDigit(c) || c == '(' || c == ')';
                    boolean isOperator = !numberOrBracket && !Character.isLetter(c);

                    valid = numberOrBracket || (!wasOperator && isOperator);
                    wasOperator = isOperator;

                    if (!valid) break;
                }

                if (!valid) continue;

                // Check that there is something after the equals sign
                int equalsIndex = line.lastIndexOf("=");
                if (equalsIndex > line.length() - 2) continue;

                char first = line.charAt(0);
                valid = Character.isDigit(first) || first == '-';
                if (!valid) continue;

                // Check that the characters after the equals sign are all numbers
                String answer = line.substring(equalsIndex + 1);
                long invalidCount = answer.chars().filter(c -> !Character.isDigit(c)).count();
                if (invalidCount > 0) continue;

                // Add the equation without the answer to the list of questions that will be displayed
                line = line.substring(0, equalsIndex);
                _questions.add(line);
                _answers.add(Integer.parseInt(answer));
            }
            questionsView.setItems(FXCollections.observableArrayList(_questions));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
