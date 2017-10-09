package tatai.ui;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Stack;

public class NavigationPage extends Scene {

    @FXML
    private StackPane parentPane;
    @FXML
    private StackPane content;
    @FXML
    private MaterialDesignIconView backButton;
    @FXML
    private MaterialDesignIconView optionsButton;
    @FXML
    private Label title;
    @FXML
    private GridPane optionsBar;

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
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                System.out.println("pressed");
                if (!optionsBar.isVisible() ||
                        optionsBar.getBoundsInParent().getMinX() == parentPane.getWidth()) {
                    System.out.println("popped");
                    popup(true);
                }
            }
            _pages.peek().onOptionsButtonPressed();
        });
        parentPane.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            if (e.getPickResult().getIntersectedNode() == null) {
                // do nothing
            } else if (e.getPickResult().getIntersectedNode() != optionsBar && optionsBar.isVisible()) {
                popup(false);
            }
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

    private void popup(boolean show) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(1000), optionsBar);
        if (show) {
            System.out.println("show: " + show);
            optionsBar.setVisible(true);
            tt.setFromX(parentPane.getWidth());
            tt.setToX(parentPane.getWidth() - 150);
        } else {
            System.out.println("hide: " + show);
            tt.setFromX(optionsBar.getBoundsInParent().getMinX());
            tt.setToX(parentPane.getWidth());
        }
        tt.play();
        tt.setOnFinished(e -> {
            System.out.println("SHOW: " + show);
            if (!show) {
                optionsBar.setVisible(false);
            }
        });
    }
}
