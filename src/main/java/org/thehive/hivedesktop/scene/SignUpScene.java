package org.thehive.hivedesktop.scene;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.Consts;
import org.thehive.hivedesktop.Ctx;
import org.thehive.hivedesktop.util.ExecutionUtils;
import org.thehive.hivedesktop.util.InfoLabelHandler;
import org.thehive.hiveserverclient.model.User;
import org.thehive.hiveserverclient.model.UserInfo;

import java.util.Map;

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
        private MFXLabel infoLabel;

        private InfoLabelHandler infoLabelHandler;

        public Controller() {
            super(Ctx.getInstance().sceneManager, SCENE_TYPE);
        }

        @Override
        public void onStart() {
            log.info("SignUpScene #onStart");
            infoLabelHandler = new InfoLabelHandler(infoLabel);
        }

        @Override
        public void onLoad(Map<String, Object> dataMap) {
            log.info("SignUpScene #onLoad");
        }

        @Override
        public void onUnload() {
            log.info("SignUpScene #onUnload");
        }

        @FXML
        void onSignInLinkClick(MouseEvent event) {
            log.info("Link clicked, #onSignInLinkClick");
            Ctx.getInstance().sceneManager.load(SignInScene.class);
        }

        @FXML
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        void onSignUpButtonClick(MouseEvent event) {
            log.info("Button clicked, #onSignUpButtonClick");
            signUpButton.setDisable(true);
            var username = usernameTextField.getText();
            var password = passwordTextField.getPassword();
            var email = emailTextField.getText();
            var firstname = nameTextField.getText();
            var lastname = surnameTextField.getText();
            var userInfo = new UserInfo();
            userInfo.setFirstname(firstname);
            userInfo.setLastname(lastname);
            var user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setUserInfo(userInfo);
            Ctx.getInstance().userService.signUp(user, result -> {
                if (result.status().isSuccess()) {
                    ExecutionUtils.run(() -> infoLabelHandler.setSuccessText("Signed-up successfully"));
                    ExecutionUtils.schedule(() -> {
                        Ctx.getInstance().sceneManager.load(SignInScene.class);
                        usernameTextField.clear();
                        passwordTextField.clear();
                        emailTextField.clear();
                        nameTextField.clear();
                        surnameTextField.clear();
                        signUpButton.setDisable(false);
                    }, Consts.INFO_DELAY_MILLIS);
                } else if (result.status().isError()) {
                    Platform.runLater(() -> {
                        infoLabelHandler.setWaringText(result.message().isPresent() ? result.message().get() : "Unknown error");
                        signUpButton.setDisable(false);
                    });
                } else {
                    Platform.runLater(() -> {
                        infoLabelHandler.setWaringText(result.message().isPresent() ? result.message().get() : "Unknown fail");
                        signUpButton.setDisable(false);
                    });
                }
            });
        }

    }

}
