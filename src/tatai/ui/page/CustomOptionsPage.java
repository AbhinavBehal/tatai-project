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
                long leftBrackets = line.chars().filter(c -> c == '(').count();
                long rightBrackets = line.chars().filter(c -> c == ')').count();
                long numEquals = line.chars().filter(c -> c == '=').count();
                if (leftBrackets != rightBrackets || numEquals != 1) continue;

                boolean valid = true;
                for (char c : line.toCharArray()) {
                    valid = (Character.isDigit(c) ||
                                        c == '(' ||
                                        c == ')' ) ||
                                        !Character.isLetter(c);
                    if (!valid) break;
                }
                if (!valid) continue;

                String withoutSpaces = line.replaceAll("\\s+", "");
                int equalsIndex= withoutSpaces.lastIndexOf("=");
                if (equalsIndex == 0 || equalsIndex > withoutSpaces.length() - 2) continue;

                String answer = withoutSpaces.substring(equalsIndex + 1);
                long invalidCount = answer.chars().filter(c -> !Character.isDigit(c)).count();
                if (invalidCount > 0) continue;

                // 1 + 2 + = 5 is treated as valid
                // check for bracket or number after every non number and non bracket (operation) character
                line = withoutSpaces.substring(0, equalsIndex);
                _questions.add(line);
                _answers.add(Integer.parseInt(answer));
            }
            questionsView.setItems(FXCollections.observableArrayList(_questions));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
