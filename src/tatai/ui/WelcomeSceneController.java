package tatai.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class WelcomeSceneController {
    @FXML
    private Button push;
    @FXML
    private Button pop;

    public void initialize() {
        push.setOnAction(e -> {
            try {
                Main.pushScene(new Scene(FXMLLoader.load(getClass().getResource("TestProgress.fxml"))));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        pop.setOnAction(e -> Main.popScene());
    }
}
