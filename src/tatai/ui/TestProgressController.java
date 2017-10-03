package tatai.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import tatai.model.Recorder;

import java.io.File;
import java.io.IOException;

public class TestProgressController {

    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button startButton;
    @FXML
    private Button pushButton;

    private Recorder _recorder;

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
            try {
                Main.pushScene(new Scene(FXMLLoader.load(getClass().getResource("WelcomeScene.fxml"))));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

}
