package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import tatai.model.theme.ThemeManager;

import static tatai.model.theme.Theme.*;

/**
 * ThemePicker Page controller, handles user actions on ThemePicker page,
 * namely for drawing the icons for each theme colour and updating the
 * selected theme.
 */
public class ThemePickerPage extends Page {

    @FXML
    private Canvas sunset;
    @FXML
    private Canvas laboratory;
    @FXML
    private Canvas sublime;
    @FXML
    private Canvas placeholder;

    private static final String TITLE = "Themes";

    public ThemePickerPage() {
        loadFXML(getClass().getResource("ThemePicker.fxml"));
    }

    public void initialize() {
        GraphicsContext ss = sunset.getGraphicsContext2D();
        GraphicsContext lb = laboratory.getGraphicsContext2D();
        GraphicsContext sb = sublime.getGraphicsContext2D();
        GraphicsContext pl = placeholder.getGraphicsContext2D();

        draw(ss, Color.web(BEACHSIDE.hexBackground()), Color.web(BEACHSIDE.hexForeground()));
        draw(lb, Color.web(CULTURAL.hexBackground()), Color.web(CULTURAL.hexForeground()));
        draw(sb, Color.web(SUBLIME.hexBackground()), Color.web(SUBLIME.hexForeground()));
        draw(pl, Color.web(PLACEHOLDER.hexBackground()), Color.web(PLACEHOLDER.hexForeground()));

        sunset.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                ThemeManager.manager().updateTheme(BEACHSIDE);
            }
        });
        laboratory.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                ThemeManager.manager().updateTheme(CULTURAL);
            }
        });
        sublime.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                ThemeManager.manager().updateTheme(SUBLIME);
            }
        });
        placeholder.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                ThemeManager.manager().updateTheme(PLACEHOLDER);
            }
        });
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    // Private helper function used to draw the theme icons
    private void draw(GraphicsContext gc, Color bg, Color fg) {
        gc.setFill(fg);
        gc.fillRoundRect(0, 0, 150, 150, 20, 20);
        gc.setFill(bg);
        gc.fillRoundRect(10, 10, 130, 130, 20, 20);

        gc.setStroke(fg);
        gc.setLineWidth(10);
        gc.strokeLine(125, 0, 0, 125);

        gc.setFill(fg);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.loadFont(getClass().getResource("/fonts/Roboto-Regular.ttf").toExternalForm(), 48));
        gc.fillText("T\u0101", 100, 120);
    }

}
