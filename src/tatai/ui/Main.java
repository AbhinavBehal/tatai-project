package tatai.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static NavigationPage _navigationPage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Placeholder code until the actual UI is made
        _navigationPage = new NavigationPage(new Scene(FXMLLoader.load(getClass().getResource("TestProgress.fxml"))));

        primaryStage.setScene(_navigationPage);
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMinHeight(HEIGHT);

        primaryStage.show();
    }

    public static void pushScene(Scene scene) {
        _navigationPage.pushScene(scene);
    }

    public static void popScene() {
        _navigationPage.popScene();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
