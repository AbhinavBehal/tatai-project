package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;
import tatai.model.Generator;
import tatai.model.Recorder;
import tatai.ui.control.IconButton;

import java.io.File;

public class PronunciationPage extends Page {

    @FXML
    private Label question;
    @FXML
    private IconButton recordButton;
    @FXML
    private IconButton playButton;
    @FXML
    private Button checkButton;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button submitButton;
    @FXML
    private HBox progressView;


    private final String FILENAME = "out.wav";
    private final int DURATION = 3;
    private String _title;
    private Generator _generator;
    private MediaPlayer _player;

    public PronunciationPage(String title, Generator generator) {
        loadFXML(getClass().getResource("Pronunciation.fxml"));
        _generator = generator;
        _title = title;
    }

    public void initialize() {
        recordButton.setOnAction(e -> record());
        playButton.setOnAction(e -> play());
        checkButton.setOnAction(e -> check());
        submitButton.setOnAction(e -> submit());
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public void onBackButtonPressed() {
        // Show confirmation here
    }

    @Override
    public void onOptionsButtonPressed() {
        //
    }

    private void record() {
        Recorder recorder = new Recorder();
        recorder.start(new File(FILENAME), DURATION).then(media -> {
            if (_player != null) {
                _player.dispose();
            }
            _player = new MediaPlayer(media);
        }, err -> {
            // Show an error dialog?
            err.printStackTrace();
        });
        progressBar.progressProperty().bind(recorder.progressProperty());
    }

    private void play() {
        if (_player == null) return;
        _player.stop();
        _player.play();
    }

    private void check() {
        // Checkem
    }

    private void submit() {
        // Submitem
    }
}
