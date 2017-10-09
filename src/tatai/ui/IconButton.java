package tatai.ui;

import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import java.io.IOException;

public class IconButton extends Button {
    private MaterialDesignIconView _icon;
    public IconButton() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("IconButton.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.getStyleClass().add("icon-button");
        _icon = new MaterialDesignIconView();
        setGraphic(_icon);
    }

    public String getGlyphName() {
        return _icon.getGlyphName();
    }

    public void setGlyphName(String value) {
        _icon.setGlyphName(value);
    }

    public ObjectProperty<String> glyphName() {
        return _icon.glyphNameProperty();
    }

    public Number getGlyphSize() {
        return _icon.getGlyphSize();
    }

    public void setGlyphSize(Number value) {
        _icon.setGlyphSize(value);
    }

    public ObjectProperty<Number> glyphSize() {
        return _icon.glyphSizeProperty();
    }
}
