package tatai.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import tatai.model.Recorder;

import java.io.File;

public class TestProgressController {

    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button startButton;

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
    }

}
