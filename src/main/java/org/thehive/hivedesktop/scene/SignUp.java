package org.thehive.hivedesktop.scene;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.Ctx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUp extends SingleInstanceScene {

    static final String FXML_FILENAME = "sign-up.fxml";

    public SignUp() {
        super(FXML_FILENAME);
    }

    @Slf4j
    public static class Controller implements Initializable {

        @FXML
        private Button btnClose;

        @FXML
        private MFXButton btnRegister;

        @FXML
        private Label errorMessageLabel;

        @FXML
        private MFXPasswordField pfPassword;

        @FXML
        private MFXTextField tfEmail;

        @FXML
        private MFXTextField tfName;

        @FXML
        private MFXTextField tfSurname;

        @FXML
        private MFXTextField tfUsername;


        @FXML
        void loadSignIn(ActionEvent event) {
            Ctx.getInstance().sceneManager.load(SignIn.class);
        }


        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

        }

    }

}
