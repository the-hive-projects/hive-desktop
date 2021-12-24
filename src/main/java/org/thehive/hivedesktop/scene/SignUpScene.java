package org.thehive.hivedesktop.scene;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
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

            DropShadow drop = new DropShadow();
            drop.setBlurType(BlurType.TWO_PASS_BOX);
            drop.setColor(Color.BLACK);
            drop.setHeight(50);
            drop.setWidth(100);

            signUpButton.setEffect(drop);
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
        void onSignUpButtonClick(MouseEvent event) {
            log.info("Button clicked, #onSignUpButtonClick");

            var username = usernameTextField.getText();
            var password = passwordTextField.getPassword();
            var email = emailTextField.getText();
            var firstname = nameTextField.getText();
            var lastname = surnameTextField.getText();
            var userInfo = new UserInfo();

            if(username.isEmpty() || password.isEmpty() || email.isEmpty() || firstname.isEmpty()|| lastname.isEmpty()){
                infoLabelHandler.setWaringText("Empty field");
                return;
            }

            signUpButton.setDisable(true);

            userInfo.setFirstname(firstname);
            userInfo.setLastname(lastname);
            var user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setUserInfo(userInfo);
            Ctx.getInstance().userService.signUp(user, result -> {
                if (result.status().isSuccess()) {
                    ExecutionUtils.runOnUiThread(() -> infoLabelHandler.setSuccessText("Signed-up successfully"));
                    ExecutionUtils.scheduleOnUiThread(() -> {
                        if (result.response().isPresent()) {
                            var dataMap = Map.<String, Object>of(Consts.SIGNED_UP_USERNAME_SESSION_DATA_KEY, result.response().get().getUsername());
                            Ctx.getInstance().sceneManager.load(SignInScene.class, dataMap);
                        } else
                            Ctx.getInstance().sceneManager.load(SignInScene.class);
                        usernameTextField.clear();
                        passwordTextField.clear();
                        emailTextField.clear();
                        nameTextField.clear();
                        surnameTextField.clear();
                        signUpButton.setDisable(false);
                    }, Consts.INFO_DELAY_MILLIS);
                } else if (result.status().isError()) {
                    ExecutionUtils.runOnUiThread(() -> {
                        infoLabelHandler.setWaringText(result.message().isPresent() ? result.message().get() : "Unknown error");
                        signUpButton.setDisable(false);
                    });
                } else {
                    ExecutionUtils.runOnUiThread(() -> {
                        infoLabelHandler.setWaringText(result.message().isPresent() ? result.message().get() : "Unknown fail");
                        signUpButton.setDisable(false);
                    });
                }
            });
        }

    }

}
