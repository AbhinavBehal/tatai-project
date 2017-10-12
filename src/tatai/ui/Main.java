package tatai.ui;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import tatai.model.theme.ThemeManager;
import tatai.ui.page.MenuPage;
import tatai.ui.page.NavigationPage;
import tatai.ui.page.Page;

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

    public static void pushScene(Page page) {
        _navigationPage.pushPage(page);
    }

    public static void popScene() {
        _navigationPage.popPage();
    }

    public static Optional<ButtonType> showAlert(Alert.AlertType type, String text, ButtonType... buttons) {
        Alert alert = new Alert(type, text, buttons);
        alert.getDialogPane().getStylesheets().addAll("/styles/common-styles.css", ThemeManager.manager().getCurrentTheme().toString());
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
        launch(args);
    }
}
