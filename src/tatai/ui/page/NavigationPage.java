package tatai.ui.page;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    private Pane overlay;
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
                if (!optionsBar.isVisible() ||
                        optionsBar.getBoundsInParent().getMinX() == parentPane.getWidth()) {
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
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), optionsBar);
        FadeTransition ft = new FadeTransition(Duration.millis(100), overlay);
        SequentialTransition st;

        if (show) {
            optionsBar.setVisible(true);
            tt.setFromX(optionsBar.getWidth());
            tt.setToX(0);

            overlay.setVisible(true);
            ft.setFromValue(0);
            ft.setToValue(0.4);

            st = new SequentialTransition(ft, tt);
        } else {
            tt.setFromX(0);
            tt.setToX(optionsBar.getWidth());

            ft.setFromValue(0.4);
            ft.setToValue(0);

            st = new SequentialTransition(tt, ft);
        }

        st.play();
        st.setOnFinished(e -> {
            if (!show) {
                optionsBar.setVisible(false);
                overlay.setVisible(false);
            }
        });
    }
}
