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
        new File("out.wav").delete();
        new File("out.mlf").delete();
    }

    public static void pushPage(Page page) {
        _navigationPage.pushPage(page);
    }

    public static void popPage() {
        _navigationPage.popPage();
    }

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

        // Sample data
        DataManager.manager().updateScore(new Score(LocalDate.now(), Module.PRACTICE, Difficulty.EASY, 2));
        DataManager.manager().updateScore(new Score(LocalDate.now(), Module.TEST, Difficulty.HARD, 4));
        DataManager.manager().updateScore(new Score(LocalDate.now(), Module.TEST, Difficulty.EASY, 9));
        DataManager.manager().updateScore(new Score(LocalDate.now(), Module.PRACTICE, Difficulty.HARD, 10));

        launch(args);
    }
}
