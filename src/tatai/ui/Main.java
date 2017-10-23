package tatai.ui;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tatai.model.data.DataManager;
import tatai.model.generator.Difficulty;
import tatai.model.generator.Module;
import tatai.model.statistics.Score;
import tatai.model.theme.ThemeManager;
import tatai.ui.page.MenuPage;
import tatai.ui.page.NavigationPage;
import tatai.ui.page.Page;

import java.io.File;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

public class Main extends Application {

    private static final int WIDTH = 960;
    private static final int HEIGHT = 720;
    private static NavigationPage _navigationPage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        _navigationPage = new NavigationPage(new MenuPage());

        primaryStage.setTitle("T\u0101tai");
        primaryStage.setScene(_navigationPage);
        primaryStage.show();

        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.centerOnScreen();

    }

    @Override
    public void stop() {
        // Clean up generated files when the application closes
        new File("out.wav").delete();
        new File("out.mlf").delete();
    }

    /**
     * Method that changes the page shown to the one that is passed in.
     * @param page The page to change to.
     */
    public static void pushPage(Page page) {
        _navigationPage.pushPage(page);
    }

    /**
     * Method that changes the page shown to the previous one (if one exists).
     */
    public static void popPage() {
        _navigationPage.popPage();
    }

    /**
     * Method that shows a modal alert dialog.
     * @param type The type of the alert (determines the icon shown).
     * @param text The message to show in the alert.
     * @param buttons Optional parameter to customise the buttons shown (overrides the default ones specified
     *                by the alert type).
     * @return An optional representing which button was clicked in the alert.
     */
    public static Optional<ButtonType> showAlert(Alert.AlertType type, String text, ButtonType... buttons) {
        Alert alert = new Alert(type, text, buttons);
        Label message = new Label(text);
        message.setWrapText(true);
        alert.getDialogPane().setContent(message);
        alert.getDialogPane().getStylesheets().addAll(
                "/styles/common-styles.css",
                ThemeManager.manager().getCurrentTheme().toString());

        switch(type) {
            case CONFIRMATION:
                alert.getDialogPane().setGraphic(new MaterialDesignIconView(MaterialDesignIcon.HELP_CIRCLE));
                break;
            case ERROR:
                alert.getDialogPane().setGraphic(new MaterialDesignIconView(MaterialDesignIcon.CLOSE_BOX));
                break;
        }
        return alert.showAndWait();
    }

    public static void main(String[] args) {

        Random r = new Random();
        // Sample data
        DataManager.manager().updateScore(new Score(LocalDate.now(), Module.PRACTICE, Difficulty.EASY, r.nextInt(11)));
        DataManager.manager().updateScore(new Score(LocalDate.now(), Module.TEST, Difficulty.HARD, r.nextInt(11)));
        DataManager.manager().updateScore(new Score(LocalDate.now(), Module.TEST, Difficulty.EASY, r.nextInt(11)));
        DataManager.manager().updateScore(new Score(LocalDate.now(), Module.PRACTICE, Difficulty.HARD, r.nextInt(11)));

        launch(args);
    }
}
