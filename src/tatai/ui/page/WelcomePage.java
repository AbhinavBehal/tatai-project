package tatai.ui.page;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import tatai.ui.Main;

public class WelcomePage extends Page {

    @FXML
    private Button pushButton;
    @FXML
    private Button popButton;

    public WelcomePage() {
        loadFXML(getClass().getResource("Welcome.fxml"));
    }

    public void initialize() {
        pushButton.setOnAction(e -> {
            Main.pushScene(new TestProgressPage());
        });
        popButton.setOnAction(e -> Main.popScene());
    }

    @Override
    public String getTitle() {
        return "T\u0101tai";
    }

    @Override
    public void onBackButtonPressed() {
        System.out.println("Welcome back");
    }

    @Override
    public void onOptionsButtonPressed() {
        System.out.println("Welcome options");
    }

}
