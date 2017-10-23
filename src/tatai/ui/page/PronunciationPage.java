package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import tatai.model.generator.Generator;
import tatai.model.recognition.Recogniser;
import tatai.model.recognition.Recorder;
import tatai.model.recognition.Translator;
import tatai.model.statistics.StatsManager;
import tatai.ui.Main;
import tatai.ui.control.IconButton;

import java.io.File;
import java.time.LocalDate;

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

    private static final String FILENAME = "out.wav";
    private static final int DURATION = 3;
    private static final int MAX_ATTEMPTS = 2;

    private final int MAX_ROUNDS;

    private Recorder _recorder;
    private Generator _generator;
    private MediaPlayer _player;
    private String _title;

    private int _rounds;
    private int _attempts;
    private int _correct;


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
        recordButton.setOnMouseClicked(e -> record());
        playButton.setOnMouseClicked(e -> play());
        checkButton.setOnMouseClicked(e -> check());
        submitButton.setOnMouseClicked(e -> submit());
        restartButton.setOnMouseClicked(e -> updateState(GameState.START));
        updateState(GameState.START);
    }

    @Override
    public String getTitle() {
        return _title;
    }

    @Override
    public void onBackButtonPressed() {
        if (_rounds == 0 || _rounds == MAX_ROUNDS) {
            Main.popPage();
        } else {
            // Ask the user for confirmation before going back, if they have unsaved progress
            Main.showAlert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to go back?\n" +
                    "This round's progress will be lost.",
                    ButtonType.YES,
                    ButtonType.NO)
                    .filter(response -> response == ButtonType.YES)
                    .ifPresent(reply -> Main.popPage());
        }
    }

    private void nextQuestion() {
        questionLabel.setText(_generator.generate());
    }

    private void record() {
        // Start recording, and update the game state when it finishes
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
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(_recorder.progressProperty());
    }

    private void play() {
        if (_player == null) return;
        _player.stop();
        _player.dispose();
        progressBar.progressProperty().unbind();
        _player = new MediaPlayer(_player.getMedia());

        // Listen to the players current time and update the progress bar accordingly
        _player.currentTimeProperty().addListener(e -> {
            progressBar.setProgress(_player.getCurrentTime().toMillis() / _player.getStopTime().toMillis());
        });
        _player.setOnEndOfMedia(() -> {
            updateState(GameState.IDLE);
        });
        updateState(GameState.PLAYING);
        _player.play();
    }

    // Method that recognises what the user has said and shows it to the user
    private void check() {
        if (_player == null) return;
        Recogniser.recognise(new File(_player.getMedia().getSource())).then(recognised -> {
            if (!recognised.isEmpty()) {
                checkLabel.setText("- " + recognised + " -");
            } else {
                checkLabel.setText("We didn't hear anything");
            }
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
                // If the user answered correctly, increase their score and set the appropriate labels
                _attempts = 0;
                _correct++;
                _rounds++;
                updateProgress(true);
                result = "Correct";
                feedback = "";
                continueText = "Continue";
            } else {
                // If the user was incorrect, check if they have any attempts left,
                // and show them what the application recognised, or the answer if this was their final attempt
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
        }, err -> Main.showAlert(Alert.AlertType.ERROR, "An error occurred while trying recognise what you said."));
    }

    // Method that updates the progress icons at the bottom of the screen in response to an incorrect or correct answer
    private void updateProgress(boolean correct) {
        IconButton button = (IconButton) progressView.getChildren().get(_rounds - 1);
        if (correct) {
            button.setGlyphName("CHECKBOX_MARKED");
        } else {
            button.setGlyphName("CLOSE_BOX");
        }
    }

    // Method the clears the progress icons at the bottom of the screen (makes them into all blank squares)
    private void clearProgress() {
        progressView.getChildren().clear();
        for (int i = 0; i < MAX_ROUNDS; ++i) {
            IconButton button = new IconButton();
            button.setGlyphName("CHECKBOX_BLANK_OUTLINE");
            button.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(button, Priority.ALWAYS);
            HBox.setMargin(button, new Insets(0, 10, 0, 10));
            progressView.getChildren().add(button);
        }
    }

    // Method that handles the transition between the states of the game, by enabling/disabling buttons, resetting
    // scores/rounds/attempts, showing/hiding different views and updating the users score once all rounds are finished
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
                progressBar.setProgress(0.0);

                checkLabel.setText("");

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
                finishView.setVisible(false);
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
                finishView.setVisible(false);
                break;
            case FINISH:
                StatsManager.manager().updateScore(LocalDate.now(), _generator.module(), _generator.difficulty(), _correct);
                scoreLabel.setText("You scored\n" + _correct + "/" + MAX_ROUNDS);

                feedbackView.setVisible(false);
                questionView.setVisible(false);
                finishView.setVisible(true);
                break;
        }
    }

    // Helper method that disables/enables the buttons used for answering the question
    private void disableQuestionButtons(boolean disable) {
        recordButton.setDisable(disable);
        playButton.setDisable(disable);
        checkButton.setDisable(disable);
        submitButton.setDisable(disable);
    }

    // Enum representing the state of the game
    private enum GameState {
        // State corresponding to when the user first starts the game
        START,

        // State corresponding to when the user reaches a new question
        INIT,

        // State corresponding to when the user clicks the record button
        RECORDING,

        // State corresponding to when the user clicks the play button
        PLAYING,

        // State corresponding to when the user either finishes recording/playback and is now idle on the question
        IDLE,

        // State corresponding to when the user presses the submit button and receives feedback about their answer
        FEEDBACK,

        // State corresponding to when the user has answered all questions and is shown their score, and given the option to restart
        FINISH
    }
}
