package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import tatai.model.generator.Generator;
import tatai.model.Recogniser;
import tatai.model.Recorder;
import tatai.model.Translator;
import tatai.model.theme.ThemeManager;
import tatai.ui.Main;
import tatai.ui.control.IconButton;

import java.io.File;

public class PronunciationPage extends Page {

    @FXML
    private VBox questionView;
    @FXML
    private Label questionLabel;
    @FXML
    private Label checkLabel;
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

    @FXML
    private VBox feedbackView;
    @FXML
    private Label resultLabel;
    @FXML
    private Label feedbackLabel;
    @FXML
    private Button continueButton;

    @FXML
    private VBox finishView;
    @FXML
    private Label scoreLabel;
    @FXML
    private Button restartButton;

    private final static String FILENAME = "out.wav";
    private final static int DURATION = 3;
    private static final int MAX_ATTEMPTS = 2;
    private final int MAX_ROUNDS;
    private Recorder _recorder;
    private int _rounds;
    private int _attempts;
    private int _correct;
    private String _title;
    private Generator _generator;
    private MediaPlayer _player;

    public PronunciationPage(String title, Generator generator) {
        _generator = generator;
        _title = title;
        _recorder = new Recorder();
        MAX_ROUNDS = generator.questions();
        loadFXML(getClass().getResource("Pronunciation.fxml"));
    }

    public void initialize() {
        continueButton.setOnAction(e -> {
            if (_rounds == MAX_ROUNDS) {
                updateState(GameState.FINISH);
            } else {
                updateState(GameState.INIT);
            }
        });
        recordButton.setOnAction(e -> record());
        playButton.setOnAction(e -> play());
        checkButton.setOnAction(e -> check());
        submitButton.setOnAction(e -> submit());
        restartButton.setOnAction(e -> updateState(GameState.START));
        updateState(GameState.START);
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public void onBackButtonPressed() {
        if (_rounds == 0 || _rounds == MAX_ROUNDS) {
            Main.popScene();
        } else {
            Main.showAlert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to go back?\n" +
                    "This round's progress will be lost.")
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(reply -> Main.popScene());
        }
    }

    @Override
    public void onOptionsButtonPressed() {
        // Do we still need this?
    }

    private void nextQuestion() {
        questionLabel.setText(_generator.generate());
    }

    private void record() {
        recordButton.setDisable(true);
        playButton.setDisable(true);
        submitButton.setDisable(true);
        checkButton.setDisable(true);

        _recorder.start(new File(FILENAME), DURATION).then(media -> {
            if (_player != null) {
                _player.dispose();
            }
            _player = new MediaPlayer(media);
            updateState(GameState.IDLE);
        }, err -> {
            Main.showAlert(Alert.AlertType.ERROR, "Your computer does not support recording.");
            progressBar.progressProperty().unbind();
            progressBar.setProgress(0.0);
            recordButton.setDisable(false);
        });
        updateState(GameState.RECORDING);
        // TODO: Change the way you get progress from the recorder, sometimes the recording finishes faster than the TimerTask and the bar jumps
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(_recorder.progressProperty());
    }

    private void play() {
        if (_player == null) return;
        _player.stop();
        _player.dispose();
        progressBar.progressProperty().unbind();
        _player = new MediaPlayer(_player.getMedia());

        _player.currentTimeProperty().addListener(e -> {
            // TODO: Make this smoother
            progressBar.setProgress(_player.getCurrentTime().toMillis() / _player.getStopTime().toMillis());
        });
        _player.setOnEndOfMedia(() -> {
            updateState(GameState.IDLE);
        });
        updateState(GameState.PLAYING);
        _player.play();
    }

    private void check() {
        if (_player == null) return;
        Recogniser.recognise(new File(_player.getMedia().getSource())).then(recognised -> {
            checkLabel.setText("- " + recognised + " -");
        }, err -> {
            Main.showAlert(Alert.AlertType.ERROR, "An error occurred while trying recognise what you said.");
        });
    }

    private void submit() {
        if (_player == null) return;
        Recogniser.recognise(new File(_player.getMedia().getSource())).then(recognised -> {
            String answer = Translator.translate(_generator.value());
            boolean correct = answer.equals(recognised);
            String result;
            String feedback;
            String continueText;
            if (correct) {
                _attempts = 0;
                _correct++;
                _rounds++;
                updateProgress(true);
                result = "Correct";
                feedback = "";
                continueText = "Continue";
            } else {
                _attempts++;
                result = "Incorrect";
                if (_attempts == MAX_ATTEMPTS) {
                    _attempts = 0;
                    _rounds++;
                    updateProgress(false);
                    feedback = "The correct answer was\n" + answer;
                    continueText = "Continue";
                } else {
                    feedback = recognised.isEmpty() ? "We didn't hear anything" : "You said\n" + recognised;
                    continueText = "Try again";
                }
            }
            resultLabel.setText(result);
            feedbackLabel.setText(feedback);
            continueButton.setText(continueText);
            updateState(GameState.FEEDBACK);
        }, err -> {
            Main.showAlert(Alert.AlertType.ERROR, "An error occurred while trying recognise what you said.");
        });
    }

    private void updateProgress(boolean correct) {
        IconButton button = new IconButton();
        if (correct) {
            button.setGlyphName("CHECKBOX_MARKED");
        } else {
            button.setGlyphName("CLOSE_BOX");
        }
        button.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(button, Priority.ALWAYS);
        progressView.getChildren().set(_rounds - 1, button);
    }

    private void clearProgress() {
        progressView.getChildren().clear();
        for (int i = 0; i < MAX_ROUNDS; ++i) {
            IconButton button = new IconButton();
            button.setGlyphName("CHECKBOX_BLANK_OUTLINE");
            button.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(button, Priority.ALWAYS);
            progressView.getChildren().add(button);
        }
    }

    private void updateState(GameState state) {
        switch(state) {
            case START:
                disableQuestionButtons(true);
                recordButton.setDisable(false);
                nextQuestion();
                _attempts = 0;
                _correct = 0;
                _rounds = 0;
                clearProgress();
                progressBar.progressProperty().unbind();
                checkLabel.setText("");
                progressBar.setProgress(0.0);
                questionView.setVisible(true);
                feedbackView.setVisible(false);
                finishView.setVisible(false);
                break;
            case INIT:
                disableQuestionButtons(true);
                recordButton.setDisable(false);
                if (_attempts == 0) nextQuestion();
                progressBar.progressProperty().unbind();
                progressBar.setProgress(0.0);
                checkLabel.setText("");
                questionView.setVisible(true);
                feedbackView.setVisible(false);
                break;
            case RECORDING:
                disableQuestionButtons(true);
                checkLabel.setText("");
                break;
            case PLAYING:
                disableQuestionButtons(true);
                break;
            case IDLE:
                progressBar.progressProperty().unbind();
                progressBar.setProgress(1.0);
                disableQuestionButtons(false);
                break;
            case FEEDBACK:
                questionView.setVisible(false);
                feedbackView.setVisible(true);
                break;
            case FINISH:
                // TODO: Update scores here
                scoreLabel.setText("You scored\n" + _correct + "/" + MAX_ROUNDS);
                feedbackView.setVisible(false);
                questionView.setVisible(false);
                finishView.setVisible(true);
                break;
        }
    }

    private void disableQuestionButtons(boolean disable) {
        recordButton.setDisable(disable);
        playButton.setDisable(disable);
        checkButton.setDisable(disable);
        submitButton.setDisable(disable);
    }

    private enum GameState {
        START,
        INIT,
        RECORDING,
        PLAYING,
        IDLE,
        FEEDBACK,
        FINISH
    }
}
