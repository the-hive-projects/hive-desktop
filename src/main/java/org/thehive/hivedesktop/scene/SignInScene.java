package org.thehive.hivedesktop.scene;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.Consts;
import org.thehive.hivedesktop.Ctx;
import org.thehive.hivedesktop.util.ExecutionUtils;
import org.thehive.hivedesktop.util.InfoLabelHandler;
import org.thehive.hiveserverclient.util.MessageUtils;

import java.util.Map;
import java.util.StringJoiner;

public class SignInScene extends FxmlSingleLoadedScene {

    private static final String FXML_FILENAME = "sign-in.fxml";

    public SignInScene() {
        super(FXML_FILENAME);
    }

    @Slf4j
    public static class Controller extends AbstractController {

        private static final Class<? extends AppScene> SCENE_TYPE = SignInScene.class;

        @FXML
        private MFXPasswordField passwordTextField;

        @FXML
        private MFXButton signInButton;

        @FXML
        private Hyperlink signUpLink;

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
            log.info("SignInScene #onStart");
            this.infoLabelHandler = new InfoLabelHandler(infoLabel);
        }

        @Override
        public void onLoad(Map<String, Object> dataMap) {
            log.info("SignInScene #onLoad");
            if (dataMap.containsKey(Consts.SIGNED_UP_USERNAME_SESSION_DATA_KEY))
                usernameTextField.setText(dataMap.get(Consts.SIGNED_UP_USERNAME_SESSION_DATA_KEY).toString());
        }

        @Override
        public void onUnload() {
            log.info("SignInScene #onUnload");
        }

        @FXML
        void onSignInButtonClick(MouseEvent event) {
            log.info("Button clicked, #onSignInButtonClick");
            infoLabel.setText(Consts.EMPTY_STRING);
            var username = usernameTextField.getText();
            var password = passwordTextField.getPassword();
            signInButton.setDisable(true);
            Ctx.getInstance().userService.signIn(username, password, result -> {
                if (result.status().isSuccess()) {
                    ExecutionUtils.run(() -> infoLabelHandler.setSuccessText("Signed-in successfully"));
                    ExecutionUtils.schedule(() -> {
                        Ctx.getInstance().sceneManager.load(MainScene.class);
                        signInButton.setDisable(false);
                    }, Consts.INFO_DELAY_MILLIS);
                } else if (result.status().isError()) {
                    if (result.message().isPresent()) {
                        var warningTextStringJoiner = new StringJoiner(".\n");
                        MessageUtils.parseMessageList(result.message().get(), ",")
                                .forEach(warningTextStringJoiner::add);
                        ExecutionUtils.run(() -> {
                            infoLabelHandler.setWaringText(warningTextStringJoiner.toString());
                            signInButton.setDisable(false);
                        });
                    } else {
                        ExecutionUtils.run(() -> {
                            infoLabelHandler.setWaringText("Unknown error");
                            signInButton.setDisable(false);
                        });
                    }
                } else {
                    ExecutionUtils.run(() -> {
                        infoLabelHandler.setWaringText(result.message().isPresent() ? result.message().get() : "Unknown fail");
                        signInButton.setDisable(false);
                    });
                }
            });
        }

        @FXML
        void onSignUpLinkClick(MouseEvent event) {
            log.info("Link clicked, #onSignUpLinkClick");
            Ctx.getInstance().sceneManager.load(SignUpScene.class);
        }

    }

}
