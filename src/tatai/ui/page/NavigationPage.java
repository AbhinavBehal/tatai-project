package tatai.ui.page;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
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
import tatai.model.theme.Theme;
import tatai.model.theme.ThemeListener;
import tatai.model.theme.ThemeManager;
import tatai.ui.Main;
import tatai.ui.control.IconButton;

import java.io.IOException;
import java.util.Stack;

public class NavigationPage extends Scene implements ThemeListener {

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
    private GridPane optionsPane;
    @FXML
    private IconButton themesButton;
    @FXML
    private IconButton statsButton;

    private static final int BACKBUTTON_SIZE = 48;
    private Stack<Page> _pages;

    public NavigationPage(Page content) {
        super(new Pane());
        _pages = new Stack<>();
        _pages.push(content);
        ThemeManager.manager().addListener(this);

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
        });
        optionsButton.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                popup(true);
            }
            _pages.peek().onOptionsButtonPressed();
        });
        parentPane.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            if (e.getPickResult().getIntersectedNode() == null || !e.getButton().equals(MouseButton.PRIMARY)) return;

            if (e.getPickResult().getIntersectedNode() != optionsPane && optionsPane.isVisible()) {
                // Don't move it back if it's already moving
                if (optionsPane.getTranslateX() != 0) return;
                popup(false);
            }
        });
        themesButton.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                Main.pushPage(new ThemePickerPage());
                themesButton.setDisable(true);
            }
        });
        statsButton.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                Main.pushPage(new StatisticsPage());
                statsButton.setDisable(true);
            }
        });

        parentPane.getStylesheets().add(ThemeManager.manager().getCurrentTheme().toString());
    }

    public void pushPage(Page page) {
        _pages.push(page);
        backButton.setGlyphSize(BACKBUTTON_SIZE);
        changePage();
    }

    public void popPage() {
        themesButton.setDisable(!(_pages.peek() instanceof ThemePickerPage) && themesButton.isDisabled());
        statsButton.setDisable(!(_pages.peek() instanceof StatisticsPage) && statsButton.isDisabled());
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

    @Override
    public void updateTheme(Theme previousTheme, Theme newTheme) {
        parentPane.getStylesheets().remove(previousTheme.toString());
        parentPane.getStylesheets().add(newTheme.toString());
    }

    private void popup(boolean show) {
        TranslateTransition popOutTranslate = new TranslateTransition(Duration.millis(200), optionsPane);
        FadeTransition overlayFade = new FadeTransition(Duration.millis(100), overlay);
        SequentialTransition st;

        if (show) {
            overlay.setVisible(true);
            overlayFade.setFromValue(0);
            overlayFade.setToValue(0.4);

            optionsPane.setVisible(true);
            popOutTranslate.setFromX(optionsPane.getWidth());
            popOutTranslate.setToX(0);

            st = new SequentialTransition(overlayFade, popOutTranslate);
        } else {
            popOutTranslate.setFromX(0);
            popOutTranslate.setToX(optionsPane.getWidth());

            overlayFade.setFromValue(0.4);
            overlayFade.setToValue(0);

            st = new SequentialTransition(popOutTranslate, overlayFade);
        }

        st.play();
        st.setOnFinished(e -> {
            if (!show) {
                optionsPane.setVisible(false);
                overlay.setVisible(false);
            }
        });
    }
}
