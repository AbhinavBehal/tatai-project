package tatai.ui.page;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
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

/**
 * Class that wraps a Page, showing a navigation bar at the top of the screen that has a title,
 * a back button and an options button. This class maintains a stack of Pages, and calls to pushPage() and popPage()
 * add to the stack and remove from the stack, respectively. At anytime the currently shown page is the one that is at
 * the top of the stack.
 */
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
    private static final int DURATION = 125;
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

        // Setup click handlers for when the back button gets pressed
        backButton.setOnMouseClicked(e -> {
            if (!e.getButton().equals(MouseButton.PRIMARY)) return;

            // Fire the back button pressed event to the current set page
            _pages.peek().onBackButtonPressed();
        });

        // Show the options menu when the options button is pressed
        optionsButton.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                popup(true);
            }
        });

        // Logic for hiding the options menu when the user click occurs outside of it
        parentPane.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
            if (e.getPickResult().getIntersectedNode() == null || !e.getButton().equals(MouseButton.PRIMARY)) return;

            if (e.getPickResult().getIntersectedNode() != optionsPane && optionsPane.isVisible()) {
                // Don't move it back if it's already moving
                if (optionsPane.getTranslateX() != 0) return;
                popup(false);
            }
        });

        // Logic for transitioning to the themes page when the themes button is clicked
        themesButton.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                Main.pushPage(new ThemePickerPage());
                themesButton.setDisable(true);
            }
        });

        // Logic for transitioning to the statistics page when the statistics button is clicked
        statsButton.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                Main.pushPage(new StatisticsPage());
                statsButton.setDisable(true);
            }
        });

        parentPane.getStylesheets().add(ThemeManager.manager().getCurrentTheme().toString());
    }

    // Method that changes the currently set Page to the one passed in, and handles transition logic
    public void pushPage(Page page) {
        FadeTransition fOut = new FadeTransition(Duration.millis(DURATION), _pages.peek().getRoot());
        FadeTransition fIn = new FadeTransition(Duration.millis(DURATION), _pages.push(page).getRoot());
        FadeTransition navOut = new FadeTransition(Duration.millis(DURATION), this.getRoot());
        FadeTransition navIn = new FadeTransition(Duration.millis(DURATION), this.getRoot());

        fOut.setFromValue(1);
        fOut.setToValue(0);
        navOut.setFromValue(1);
        navOut.setToValue(0);

        fIn.setFromValue(0);
        fIn.setToValue(1);
        navIn.setFromValue(0);
        navIn.setToValue(1);

        ParallelTransition ptOut = new ParallelTransition(fOut, navOut);
        ParallelTransition ptIn = new ParallelTransition(fIn, navIn);

        ptOut.play();
        ptOut.setOnFinished(e -> {
            changePage();
            backButton.setGlyphSize(BACKBUTTON_SIZE);
            ptIn.play();
        });
    }

    // Method that changes the current Page to the previous one in the stack, and handles transition logic
    public void popPage() {
        themesButton.setDisable(!(_pages.peek() instanceof ThemePickerPage) && themesButton.isDisabled());
        statsButton.setDisable(!(_pages.peek() instanceof StatisticsPage) && statsButton.isDisabled());
        if (_pages.size() > 1) {
            FadeTransition fOut = new FadeTransition(Duration.millis(DURATION), _pages.pop().getRoot());
            FadeTransition fIn = new FadeTransition(Duration.millis(DURATION), _pages.peek().getRoot());
            FadeTransition navOut = new FadeTransition(Duration.millis(DURATION), this.getRoot());
            FadeTransition navIn = new FadeTransition(Duration.millis(DURATION), this.getRoot());

            fOut.setFromValue(1);
            fOut.setToValue(0);
            navOut.setFromValue(1);
            navOut.setToValue(0);

            fIn.setFromValue(0);
            fIn.setToValue(1);
            navIn.setFromValue(0);
            navIn.setToValue(1);

            ParallelTransition ptOut = new ParallelTransition(fOut, navOut);
            ParallelTransition ptIn = new ParallelTransition(fIn, navIn);

            ptOut.play();
            ptOut.setOnFinished(e -> {
                changePage();

                // Don't show the back button if you can't go back
                backButton.setGlyphSize(_pages.size() == 1 ? 0 : BACKBUTTON_SIZE);
                ptIn.play();
            });

        }
    }

    // Method for listening to theme change events
    @Override
    public void updateTheme(Theme previousTheme, Theme newTheme) {
        parentPane.getStylesheets().remove(previousTheme.toString());
        parentPane.getStylesheets().add(newTheme.toString());
    }

    // Helper method that sets the current page to the one at the top of the stack
    private void changePage() {
        content.getChildren().setAll(_pages.peek().getRoot());
        title.setText(_pages.peek().getTitle());
    }

    // Method that handles the transitions involved in showing the options menu
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
