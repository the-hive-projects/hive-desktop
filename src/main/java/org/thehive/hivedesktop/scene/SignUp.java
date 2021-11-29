package org.thehive.hivedesktop.scene;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.Ctx;

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
        private MFXTextField emailTextField;

        @FXML
        private MFXTextField nameTextField;

        @FXML
        private MFXPasswordField passwordTextField;

        @FXML
        private Hyperlink signInLink;

        @FXML
        private MFXButton signUpButton;

        @FXML
        private MFXTextField surnameTextField;

        @FXML
        private MFXTextField usernameTextField;

        @FXML
        private MFXLabel warningMessageLabel;

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            log.info("Controller is initializing, class: {}", getClass().getName());
        }

        @FXML
        void onSignUpButtonClick(MouseEvent event) {
            log.info("Button clicked, id: onSignUpButtonClick");
        }

        @FXML
        void onSignInLinkClick(MouseEvent event) {
            log.info("Link clicked, id: onSignInLinkClick");
            Ctx.getInstance().sceneManager.load(SignIn.class);
        }

    }

}
