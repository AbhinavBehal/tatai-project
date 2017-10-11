package tatai.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import tatai.model.Difficulty;
import tatai.model.NumberGenerator;
import tatai.ui.page.NavigationPage;
import tatai.ui.page.Page;
import tatai.ui.page.PronunciationPage;

public class Main extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static NavigationPage _navigationPage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Placeholder code until the actual UI is made
        _navigationPage = new NavigationPage(new PronunciationPage("Easy", new NumberGenerator(Difficulty.EASY)));

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

    public static void main(String[] args) {
        launch(args);
    }
}
