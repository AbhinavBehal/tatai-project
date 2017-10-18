package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import tatai.model.generator.Difficulty;
import tatai.model.generator.Generator;
import tatai.model.generator.NumberGenerator;
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
        easyButton.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                Generator generator = new NumberGenerator(Difficulty.EASY);
                Main.pushPage(new PronunciationPage("Practice - Easy", generator));
            }
        });
        hardButton.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                Generator generator = new NumberGenerator(Difficulty.HARD);
                Main.pushPage(new PronunciationPage("Practice - Hard", generator));
            }
        });
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

}
