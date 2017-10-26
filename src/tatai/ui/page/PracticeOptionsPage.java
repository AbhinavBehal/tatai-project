package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import tatai.model.generator.Difficulty;
import tatai.model.generator.Generator;
import tatai.model.generator.Module;
import tatai.model.generator.NumberGenerator;
import tatai.model.statistics.ScoreListener;
import tatai.model.statistics.StatsManager;
import tatai.ui.Main;

/**
 * PracticeOptions Page controller, handles user actions on PracticeOptions page,
 * namely for choosing difficulty.
 */
public class PracticeOptionsPage extends Page implements ScoreListener {

    @FXML
    private Button easyButton;
    @FXML
    private Button hardButton;
    @FXML
    private Label hardLabel;

    private static final String TITLE = "Practice Your Pronunciation";

    public PracticeOptionsPage() {
        loadFXML(getClass().getResource("PracticeOptions.fxml"));
        StatsManager.manager().addListener(this);
    }

    public void initialize() {
        // Set up click handlers for when the user presses the easy/hard buttons, that create the appropriate generator
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

        // Disables hard mode if it is not unlocked yet
        hardButton.setDisable(!StatsManager.manager().practiceUnlocked());
        hardLabel.setDisable(!StatsManager.manager().practiceUnlocked());
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
}
