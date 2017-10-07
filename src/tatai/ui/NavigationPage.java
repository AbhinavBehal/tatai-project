package tatai.ui;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Stack;

public class NavigationPage extends Scene {

    @FXML
    private StackPane content;
    @FXML
    private MaterialDesignIconView backButton;
    @FXML
    private MaterialDesignIconView optionsButton;
    @FXML
    private Label title;

    private static final int BACKBUTTON_SIZE = 48;
    private Stack<Page> _pages;

    public NavigationPage(Page content) {
        super(new Pane());
        _pages = new Stack<>();
        _pages.push(content);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Navigation.fxml"));
        loader.setController(this);
        try {
            setRoot(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        changePage();
        backButton.setGlyphSize(0);
        backButton.setOnMouseClicked(e -> {
            if (!e.getButton().equals(MouseButton.PRIMARY)) return;
            _pages.peek().onBackButtonPressed();

            // Should we always pop?
            // The page might want to show some sort of confirmation before going back
            popPage();
        });
        optionsButton.setOnMouseClicked(e -> {
            if (!e.getButton().equals(MouseButton.PRIMARY)) return;
            _pages.peek().onOptionsButtonPressed();
        });
    }

    public void pushPage(Page page) {
        _pages.push(page);
        backButton.setGlyphSize(BACKBUTTON_SIZE);
        changePage();
    }

    public void popPage() {
        if (_pages.size() > 1) {
            _pages.pop();
            changePage();

            // Don't show the back button if you can't go back
            backButton.setGlyphSize(_pages.size() == 1 ? 0 : BACKBUTTON_SIZE);
        }
    }

    private void changePage() {
        content.getChildren().setAll(_pages.peek().getRoot());
        title.setText(_pages.peek().getTitle());
    }
}
