package org.thehive.hivedesktop.scene;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.Ctx;

public class SignUpScene extends FxmlSingleLoadedScene {

    private static final String FXML_FILENAME = "sign-up.fxml";

    public SignUpScene() {
        super(FXML_FILENAME);
    }

    @Slf4j
    public static class Controller extends AbstractController {

        private static final Class<? extends AppScene> SCENE_TYPE = SignUpScene.class;

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

        public Controller() {
            super(Ctx.getInstance().sceneManager, SCENE_TYPE);
        }

        @FXML
        void onSignUpButtonClick(MouseEvent event) {
            log.info("Button clicked, id: onSignUpButtonClick");
        }

        @FXML
        void onSignInLinkClick(MouseEvent event) {
            log.info("Link clicked, id: onSignInLinkClick");
            Ctx.getInstance().sceneManager.load(SignInScene.class);
        }

        @Override
        public void onStart() {
            log.info("SignUpScene#onStart");
        }

        @Override
        public void onLoad() {
            log.info("SignUpScene#onLoad");
        }

        @Override
        public void onUnload() {
            log.info("SignUpScene#onUnload");
        }

    }

}
