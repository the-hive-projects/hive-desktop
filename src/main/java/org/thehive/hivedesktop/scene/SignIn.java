package org.thehive.hivedesktop.scene;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.Ctx;
import org.thehive.hiveserverclient.util.MessageUtils;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringJoiner;

public class SignIn extends SingleInstanceScene {

    static final String FXML_FILENAME = "sign-in.fxml";

    public SignIn() {
        super(FXML_FILENAME);
    }

    @Slf4j
    public static class Controller implements Initializable {

        @FXML
        private MFXPasswordField passwordTextField;

        @FXML
        private MFXButton signInButton;

        @FXML
        private Hyperlink signUpLink;

        @FXML
        private MFXTextField usernameTextField;

        @FXML
        private MFXLabel warningMessageLabel;

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            log.info("Controller is initializing, class: {}", getClass().getName());
        }

        @FXML
        void onSignInButtonClick(MouseEvent event) {
            log.info("Button clicked, id: onSignInButtonClick");
            warningMessageLabel.setText("");
            var username = usernameTextField.getText();
            var password = passwordTextField.getPassword();
            signInButton.setDisable(true);
            Ctx.getInstance().userService.signIn(username, password, result -> {
                if (result.status().isSuccess()) {
                    Platform.runLater(() -> warningMessageLabel.setText("Success"));
                } else if (result.status().isError()) {
                    result.message().ifPresent(message -> {
                        var errorMessageSJ = new StringJoiner(".\n");
                        MessageUtils.parseMessageList(message, ",")
                                .forEach(errorMessageSJ::add);
                        Platform.runLater(() -> {
                            warningMessageLabel.setText(errorMessageSJ.toString());
                            signInButton.setDisable(false);
                        });
                    });
                } else {
                    Platform.runLater(() -> {
                        warningMessageLabel.setText("Fail");
                        signInButton.setDisable(false);
                    });
                }
            });
        }

        @FXML
        void onSignUpLinkClick(MouseEvent event) {
            log.info("Link clicked, id: onSignUpLinkClick");
            Ctx.getInstance().sceneManager.load(SignUp.class);
        }

    }

}
