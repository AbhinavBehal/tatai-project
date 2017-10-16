package tatai.ui.page;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import tatai.ui.Main;

import java.io.IOException;
import java.net.URL;

/**
 * Wrapper around javafx.Scene that has a title and handles back button/options events
 * from the surrounding NavigationPage
 */
public abstract class Page extends Scene {

    public Page() {
        super(new Pane());
    }

    public abstract String getTitle();

    public void onBackButtonPressed() {
        Main.popPage();
    }
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
