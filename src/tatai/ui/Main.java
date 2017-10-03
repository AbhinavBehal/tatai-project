package tatai.ui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class Main extends Application {

    private static Stack<Scene> _scenes;
    private static Stage _primaryStage;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private static StackPane _content;

    @FXML
    private StackPane content;
    @FXML
    private Button backButton;

    public void initialize() {
        _content = content;
        backButton.setOnAction(e -> {
            try {
                Main.pushScene(new Scene(FXMLLoader.load(getClass().getResource("TestProgress.fxml"))));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Placeholder code until the actual UI is made
        Parent root = FXMLLoader.load(getClass().getResource("NavigationTest.fxml"));
        Scene scene = new Scene(root);
        _scenes = new Stack<>();
        _primaryStage = primaryStage;
        primaryStage.setScene(scene);
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.show();
    }

    public static void pushScene(Scene scene) {
        _scenes.push(scene);
        _content.getChildren().setAll(scene.getRoot());
    }

    public static void popScene() {
        if (_scenes.size() > 1) {
            _scenes.pop();
            _content.getChildren().setAll(_scenes.peek().getRoot());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
