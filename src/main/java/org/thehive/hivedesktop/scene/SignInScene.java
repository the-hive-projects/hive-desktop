package org.thehive.hivedesktop.scene;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.Consts;
import org.thehive.hivedesktop.Ctx;
import org.thehive.hivedesktop.util.ExecutionUtils;
import org.thehive.hivedesktop.util.InfoLabelHandler;
import org.thehive.hiveserverclient.util.MessageUtils;

import java.io.File;
import java.util.Map;
import java.util.StringJoiner;
import java.util.Timer;
import java.util.TimerTask;

public class SignInScene extends FxmlSingleLoadedScene {

    private static final String FXML_FILENAME = "sign-in.fxml";

    public SignInScene() {
        super(FXML_FILENAME);
    }

    @Slf4j
    public static class Controller extends AbstractController {

        private static final Class<? extends AppScene> SCENE_TYPE = SignInScene.class;

        Timer timer = new Timer();


        @FXML
        private Label lblLoading;


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

            File file = new File("src/main/resources/img/loading.gif");
            Image image = new Image(file.toURI().toString());
            ImageView view = new ImageView(image);
            view.setFitHeight(200);
            view.setFitWidth(200);

            infoLabel.setText(Consts.EMPTY_STRING);
            var username = usernameTextField.getText();
            var password = passwordTextField.getPassword();
            signInButton.setDisable(true);

            lblLoading.setGraphic(view);
            lblLoading.setVisible(true);

            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            Ctx.getInstance().userService.signIn(username, password, result -> {


                                if (result.status().isSuccess()) {

                                    ExecutionUtils.run(() -> infoLabelHandler.setSuccessText("Signed-in successfully"));
                                    ExecutionUtils.schedule(() -> {
                                        Ctx.getInstance().sceneManager.load(MainScene.class);
                                        signInButton.setDisable(false);
                                    }, Consts.INFO_DELAY_MILLIS);

                                } else if (result.status().isError()) {
                                    lblLoading.setVisible(false);
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
                    });

                }
            }, 3000);




        }

        @FXML
        void onSignUpLinkClick(MouseEvent event) {
            log.info("Link clicked, #onSignUpLinkClick");
            Ctx.getInstance().sceneManager.load(SignUpScene.class);
        }

    }

}
