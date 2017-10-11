package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import tatai.model.theme.ThemeManager;

import static tatai.model.theme.Theme.*;

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

        draw(ss, Color.web(SUNSET.hexBackground()), Color.web(SUNSET.hexForeground()));
        draw(lb, Color.web(LABORATORY.hexBackground()), Color.web(LABORATORY.hexForeground()));
        draw(sb, Color.web(SUBLIME.hexBackground()), Color.web(SUBLIME.hexForeground()));
        draw(pl, Color.web(PLACEHOLDER.hexBackground()), Color.web(PLACEHOLDER.hexForeground()));

        sunset.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                ThemeManager.manager().updateTheme(SUNSET);
            }
        });
        laboratory.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                ThemeManager.manager().updateTheme(LABORATORY);
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

    private void draw(GraphicsContext gc, Color bg, Color fg) {
        gc.setFill(fg);
        gc.fillRoundRect(0,0,150,150, 20,20);
        gc.setFill(bg);
        gc.fillRoundRect(10,10,130,130, 20,20);

        gc.setStroke(fg);
        gc.setLineWidth(10);
        gc.strokeLine(125, 0, 0, 125);

        gc.setFill(fg);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.loadFont(getClass().getResource("/Roboto-Regular.ttf").toExternalForm(), 48));
        gc.fillText("T\u0101", 100,120);
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public void onBackButtonPressed() {

    }

    @Override
    public void onOptionsButtonPressed() {

    }
}
