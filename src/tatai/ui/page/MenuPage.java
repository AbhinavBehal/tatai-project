package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import tatai.ui.Main;

/**
 * Menu Page controller, handles actions on Menu page where users can navigate
 * to Practice or Test {@link tatai.model.generator.Module}.
 */
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
        // Setup click handlers for the practice and test buttons, that change the view to the corresponding scene
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
