package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import tatai.ui.Main;

public class MenuPage extends Page {

    @FXML
    private Button practiceButton;
    @FXML
    private Button testButton;

    private static final String TITLE = "T\u0101tai";

    public MenuPage() {
        loadFXML(getClass().getResource("Menu.fxml"));
    }

    public void initialize() {
        practiceButton.setOnAction(e -> Main.pushScene(new PracticeOptionsPage()));
        testButton.setOnAction(e -> Main.pushScene(new TestOptionsPage()));
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public void onOptionsButtonPressed() { }
}
