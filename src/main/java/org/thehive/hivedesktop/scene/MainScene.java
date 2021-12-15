package org.thehive.hivedesktop.scene;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.Consts;
import org.thehive.hivedesktop.Ctx;
import org.thehive.hivedesktop.util.ExecutionUtils;
import org.thehive.hivedesktop.util.ImageUtils;
import org.thehive.hivedesktop.util.WebSocketLoggingListener;
import org.thehive.hiveserverclient.Authentication;
import org.thehive.hiveserverclient.service.ResultStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

public class MainScene extends FxmlSingleLoadedScene {

    private static final String FXML_FILENAME = "main.fxml";

    public MainScene() {
        super(FXML_FILENAME);
    }

    @Slf4j
    public static class Controller extends AbstractController {

        private static final Class<? extends AppScene> SCENE_TYPE = MainScene.class;
        Stage stage;
        @FXML
        private MFXButton createSessionButton;
        @FXML
        private Label emailLabel;
        @FXML
        private MFXTextField joinSessionIdTextField;
        @FXML
        private Label joinInfoLabel;
        @FXML
        private MFXButton joinSessionButton;
        @FXML
        private Label nameLabel;
        @FXML
        private ImageView profileImageView;
        @FXML
        private Label usernameLabel;
        @FXML
        private BorderPane mainPane;

        public Controller() {
            super(Ctx.getInstance().sceneManager, SCENE_TYPE);
        }

//        public  void start(Stage stage){
//            try {
//
//                Parent root = FXMLLoader.load(getClass().getResource("Scene.fxml"));
//                Scene scene = new Scene(root);
//                stage.setScene(scene);
//                stage.show();
//
//                stage.setOnCloseRequest(event -> {
//                    event.consume();
//                    logout(stage);
//                });
//
//            } catch(Exception e) {
//                e.printStackTrace();
//            }
//        }

        public void logout(ActionEvent event) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("You're about to logout!");
            alert.setContentText("Do you want to save before exiting?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                stage = (Stage) mainPane.getScene().getWindow();
                System.out.println("You successfully logged out!");
                Authentication.INSTANCE.unauthenticate();
                Ctx.getInstance().sceneManager.load(SignInScene.class);
            }
        }

        @Override
        public void onStart() {
            log.info("MainScene#onStart");
        }

        @Override
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        public void onLoad(Map<String, Object> dataMap) {
            log.info("MainScene#onLoad");
            Ctx.getInstance().userService.profile(profileResult -> {
                if (profileResult.status().isSuccess()) {
                    var user = profileResult.entity().get();
                    ExecutionUtils.runOnUi(() -> {
                        usernameLabel.setText(user.getUsername());
                        emailLabel.setText(user.getEmail());
                        nameLabel.setText(user.getUserInfo().getFirstname() + " " + user.getUserInfo().getLastname());
                    });
                    Ctx.getInstance().imageService.take(user.getUsername(), imageResult -> {
                        var content = imageResult.entity().get().getContent();
                        var profileImage = new Image(new ByteArrayInputStream(content));
                        ExecutionUtils.runOnUi(() -> profileImageView.setImage(profileImage));
                        try {
                            var scaledContent = ImageUtils.scaleImageContent(content, 800, 800);
                            var scaledProfileImage = new Image(new ByteArrayInputStream(scaledContent));
                            ExecutionUtils.runOnUi(() -> profileImageView.setImage(scaledProfileImage));
                        } catch (IOException e) {
                            log.warn("Error while scaling profile image", e);
                        }
                    });
                }
            });
            Ctx.getInstance().webSocketService.connect(new WebSocketLoggingListener(log));
        }

        @Override
        public void onUnload() {
            log.info("MainScene#onUnload");
        }

        @FXML
        void onCreateSessionButtonClick(MouseEvent event) {
            log.info("Button clicked, #onCreateSessionButtonClick");
        }

        @FXML
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        void onJoinSessionButtonClick(MouseEvent event) {
            log.info("Button clicked, #onJoinSessionButtonClick");
            var liveId = joinSessionIdTextField.getText();
            if (liveId.isEmpty()) {
                log.info("SessionId is empty");
                return;
            }
            log.info("SessionId: {}", liveId);
            joinSessionButton.setDisable(true);
            joinInfoLabel.setText("Joining ...");
            Ctx.getInstance().sessionService.takeLive(liveId, result -> {
                if (result.status().isSuccess()) {
                    ExecutionUtils.runOnUi(() -> {
                        joinInfoLabel.setText("Joined, name: " + result.entity().get().getName());
                        var dataMap = Map.of(
                                Consts.JOINED_SESSION_SCENE_DATA_KEY, result.entity().get(),
                                Consts.JOINED_SESSION_LIVE_ID_SCENE_DATA_KEY, liveId);
                        Ctx.getInstance().sceneManager.load(EditorScene.class, dataMap);
                    });
                } else if (result.status().isError()) {
                    if (result.status() == ResultStatus.ERROR_UNAVAILABLE) {
                        ExecutionUtils.runOnUi(() -> {
                            joinInfoLabel.setText("Session not found");
                            joinSessionButton.setDisable(false);
                        });
                    } else {
                        ExecutionUtils.runOnUi(() -> {
                            joinInfoLabel.setText(result.message().get());
                            joinSessionButton.setDisable(false);
                        });
                    }
                } else {
                    ExecutionUtils.runOnUi(() -> {
                        joinInfoLabel.setText("Fail");
                        joinSessionButton.setDisable(false);
                    });
                }
            });
        }

    }

}