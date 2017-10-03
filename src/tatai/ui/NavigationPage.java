package tatai.ui;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Stack;

public class NavigationPage extends Scene {

    @FXML
    private StackPane content;
    @FXML
    private MaterialDesignIconView backButton;

    private static final int BACKBUTTON_SIZE = 48;
    private Stack<Scene> _scenes;

    public NavigationPage(Scene content) {
        super(new Pane());
        _scenes = new Stack<>();
        _scenes.push(content);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("NavigationTest.fxml"));
        loader.setController(this);
        try {
            setRoot(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        content.getChildren().setAll(_scenes.peek().getRoot());
        backButton.setGlyphSize(0);
        backButton.setOnMouseClicked(e -> popScene());
    }

    public void pushScene(Scene scene) {
        _scenes.push(scene);
        backButton.setGlyphSize(BACKBUTTON_SIZE);
        content.getChildren().setAll(scene.getRoot());
    }

    public void popScene() {
        if (_scenes.size() > 1) {
            _scenes.pop();
            content.getChildren().setAll(_scenes.peek().getRoot());

            // Don't show the back button if you can't go back
            backButton.setGlyphSize(_scenes.size() == 1 ? 0 : BACKBUTTON_SIZE);
        }
    }
}
