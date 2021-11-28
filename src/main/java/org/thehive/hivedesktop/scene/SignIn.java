package org.thehive.hivedesktop.scene;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignIn extends SingleInstanceScene {

    static final String FXML_FILENAME = "sign-in.fxml";

    public SignIn() {
        super(FXML_FILENAME);
    }

    public static class Controller implements Initializable {


        private MFXTextField tfUsername;


        private MFXPasswordField pfPassword;


        private MFXButton btnLogin;


        @Override
        public void initialize(URL location, ResourceBundle resources) {

        }

        public void switchToRegister(ActionEvent event) throws IOException {

        }

    }

}
