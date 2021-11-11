package org.thehive.hivedesktop;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;


import java.net.URL;
import java.util.ResourceBundle;

public class SessionController implements Initializable {
    @FXML
    private MFXButton btCreateSession;

    @FXML
    private MFXButton btJoinSession;

    @FXML
    private ImageView image;

    @FXML
    private MFXTextField tfJoinSession;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Merhaba");
    }
}
