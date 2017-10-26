package tatai.ui.page;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import tatai.ui.Main;

import java.io.IOException;
import java.net.URL;

/**
 * Wrapper around {@link javafx.scene.Scene } that exposes a title and handles back button events
 * from the surrounding NavigationPage.
 */
public abstract class Page extends Scene {

    public Page() {
        super(new Pane());
    }

    /**
     * Get the title of the Page.
     * @return The tile of the page.
     */
    public abstract String getTitle();

    /**
     * Method that handles back button events from the surrounding NavigationPage.
     */
    public void onBackButtonPressed() {
        Main.popPage();
    }

    /**
     * Helper method that loads an fxml file and sets the page as the controller.
     * @param fileURL the url of the fxml file to load.
     */
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
