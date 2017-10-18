package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
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
        practiceButton.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                Main.pushPage(new PracticeOptionsPage());
            }
        });
        testButton.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                Main.pushPage(new TestOptionsPage());
            }
        });
    }

    @Override
    public String getTitle() {
        return TITLE;
    }

}
