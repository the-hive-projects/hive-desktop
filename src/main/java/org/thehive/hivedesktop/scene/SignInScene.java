package org.thehive.hivedesktop.scene;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXLabel;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;
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

            DropShadow drop = new DropShadow();
            drop.setBlurType(BlurType.TWO_PASS_BOX);
            drop.setColor(Color.BLACK);
            drop.setHeight(50);
            drop.setWidth(100);

            signInButton.setEffect(drop);

        }

        @Override
        public void onLoad(Map<String, Object> dataMap) {
            log.info("SignInScene #onLoad");
            infoLabelHandler.setDefaultText("");
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

            if (username.isEmpty() || password.isEmpty()) {
                infoLabelHandler.setWaringText("Empty filed");
                return;
            }

            signInButton.setDisable(true);

            File file = new File("src/main/resources/img/loading.gif");
            Image image = new Image(file.toURI().toString());
            ImageView view = new ImageView(image);
            view.setFitHeight(200);
            view.setFitWidth(200);


            lblLoading.setGraphic(view);
            lblLoading.setVisible(true);


            Circle cir = new Circle(0, 50, 50);
            cir.setFill(Color.web("#373737"));
            cir.setStroke(Color.BLACK);


            Text text = new Text("LOADING");
            text.setBoundsType(TextBoundsType.VISUAL);
            text.setFill(Color.web("#ffc107"));

            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(cir, text);

            TranslateTransition translate = new TranslateTransition();
            translate.setByX(100);
            translate.setDuration(Duration.millis(1000));
            translate.setCycleCount(500);
            translate.setAutoReverse(true);
            translate.setNode(stackPane);
            translate.play();
            lblLoading.setGraphic(stackPane);


            Ctx.getInstance().userService.signIn(username, password, result -> {
                if (result.status().isSuccess()) {
                    ExecutionUtils.runOnUiThread(() -> infoLabelHandler.setSuccessText("Signed-in successfully"));
                    ExecutionUtils.scheduleOnUiThread(() -> {
                        Ctx.getInstance().sceneManager.load(MainScene.class);
                        signInButton.setDisable(false);
                        lblLoading.setVisible(false);
                    }, Consts.SCENE_DELAY_MILLIS);

                } else if (result.status().isError()) {
                    lblLoading.setVisible(false);
                    if (result.message().isPresent()) {
                        var warningTextStringJoiner = new StringJoiner(".\n");
                        MessageUtils.parseMessageList(result.message().get(), ",")
                                .forEach(warningTextStringJoiner::add);
                        ExecutionUtils.runOnUiThread(() -> {
                            infoLabelHandler.setWaringText(warningTextStringJoiner.toString());
                            signInButton.setDisable(false);
                        });
                    } else {
                        ExecutionUtils.runOnUiThread(() -> {
                            infoLabelHandler.setWaringText("Unknown error");
                            signInButton.setDisable(false);
                        });
                    }
                } else {
                    ExecutionUtils.runOnUiThread(() -> {
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
