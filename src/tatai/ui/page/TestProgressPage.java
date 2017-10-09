package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import tatai.model.Recorder;
import tatai.ui.Main;

import java.io.File;

public class TestProgressPage extends Page {

    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button startButton;
    @FXML
    private Button pushButton;

    private Recorder _recorder;

    public TestProgressPage() {
        loadFXML(getClass().getResource("TestProgress.fxml"));
    }


    @FXML
    public void initialize() {
        // Testing the recorder class
        _recorder = new Recorder();
        startButton.setOnAction((e) -> {

            _recorder.start(new File("test.wav"), 3).then((media) -> {
                System.out.println("Media " + media);
            }, (err) -> {
                System.out.println("An error occurred");
                err.printStackTrace();
            });

            progressBar.progressProperty().bind(_recorder.progressProperty());
        });
        pushButton.setOnAction(e -> {
            Main.pushScene(new WelcomePage());
        });
    }

    @Override
    public String getTitle() {
        return "Test Progress";
    }

    @Override
    public void onBackButtonPressed() {
        System.out.println("Back button pressed");
    }

    @Override
    public void onOptionsButtonPressed() {
        System.out.println("Options button pressed");
    }
}