package tatai.ui.page;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

/**
 * Wrapper around javafx.Scene that has a title and handles back button/options events
 * From the surrounding NavigationPage
 */
public abstract class Page extends Scene {

    public Page() {
        super(new Pane());
    }

    public abstract String getTitle();
    public abstract void onBackButtonPressed();
    // Maybe return a list of options to show or something
    public abstract void onOptionsButtonPressed();

    protected final void loadFXML(URL fileURL) {
        FXMLLoader loader = new FXMLLoader(fileURL);
        loader.setController(this);
        try {
            setRoot(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
