package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import tatai.model.Difficulty;
import tatai.model.Generator;
import tatai.model.NumberGenerator;
import tatai.ui.Main;

public class PracticeOptionsPage extends Page {

    @FXML
    private Button easyButton;
    @FXML
    private Button hardButton;

    private static final String TITLE = "Practice Your Pronunciation";

    public PracticeOptionsPage() {
        loadFXML(getClass().getResource("PracticeOptions.fxml"));
    }

    public void initialize() {
        easyButton.setOnAction(e -> {
            Generator generator = new NumberGenerator(Difficulty.EASY);
            Main.pushScene(new PronunciationPage("Practice - Easy", generator));
        });
        hardButton.setOnAction(e -> {
            Generator generator = new NumberGenerator(Difficulty.HARD);
            Main.pushScene(new PronunciationPage("Practice - Hard", generator));
        });
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public void onBackButtonPressed() { }

    @Override
    public void onOptionsButtonPressed() { }
}
