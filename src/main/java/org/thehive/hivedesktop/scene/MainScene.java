package org.thehive.hivedesktop.scene;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.Ctx;
import org.thehive.hivedesktop.util.ImageUtils;
import org.thehive.hiveserverclient.net.websocket.WebSocketConnection;
import org.thehive.hiveserverclient.net.websocket.WebSocketListener;
import org.thehive.hiveserverclient.net.websocket.header.AppStompHeaders;
import org.thehive.hiveserverclient.net.websocket.subscription.StompSubscription;
import org.thehive.hiveserverclient.payload.Payload;
import org.thehive.hiveserverclient.service.ResultStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class MainScene extends FxmlSingleLoadedScene {

    private static final String FXML_FILENAME = "main.fxml";

    public MainScene() {
        super(FXML_FILENAME);
    }

    @Slf4j
    public static class Controller extends AbstractController {

        private static final Class<? extends AppScene> SCENE_TYPE = MainScene.class;

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

        public Controller() {
            super(Ctx.getInstance().sceneManager, SCENE_TYPE);
        }

        @Override
        public void onStart() {
            log.info("MainScene#onStart");
        }

        @Override
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        public void onLoad() {
            log.info("MainScene#onLoad");
            Ctx.getInstance().userService.profile(profileResult -> {
                if (profileResult.status().isSuccess()) {
                    var user = profileResult.entity().get();
                    Platform.runLater(() -> {
                        usernameLabel.setText(user.getUsername());
                        emailLabel.setText(user.getEmail());
                        nameLabel.setText(user.getUserInfo().getFirstname() + " " + user.getUserInfo().getLastname());
                    });
                    Ctx.getInstance().imageService.take(user.getUsername(), imageResult -> {
                        var content = imageResult.entity().get().getContent();
                        var profileImage = new Image(new ByteArrayInputStream(content));
                        Platform.runLater(() -> profileImageView.setImage(profileImage));
                        try {
                            var scaledContent = ImageUtils.scaleImageContent(content, 800, 800);
                            var scaledProfileImage = new Image(new ByteArrayInputStream(scaledContent));
                            Platform.runLater(() -> profileImageView.setImage(scaledProfileImage));
                        } catch (IOException e) {
                            log.warn("Error while scaling profile image", e);
                        }
                    });
                }
            });
            Ctx.getInstance().webSocketService.connect(new WebSocketListener() {
                @Override
                public void onConnect(WebSocketConnection webSocketConnection) {
                    log.info("WebSocketListener#onConnect");
                }

                @Override
                public void onSubscribe(StompSubscription stompSubscription) {
                    log.info("WebSocketListener#onSubscribe");
                }

                @Override
                public void onUnsubscribe(StompSubscription stompSubscription) {
                    log.info("WebSocketListener#onUnsubscribe");
                }

                @Override
                public void onReceive(AppStompHeaders appStompHeaders, Payload payload) {
                    log.info("WebSocketListener#onReceive");
                }

                @Override
                public void onSend(Payload payload) {
                    log.info("WebSocketListener#onSend");
                }

                @Override
                public void onException(Throwable throwable) {
                    log.info("WebSocketListener#onException");
                }

                @Override
                public void onDisconnect(WebSocketConnection webSocketConnection) {
                    log.info("WebSocketListener#onDisconnect");
                }
            });
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
            var sessionId = joinSessionIdTextField.getText();
            if (sessionId.isEmpty()) {
                log.info("SessionId is empty");
                return;
            }
            log.info("SessionId: {}", sessionId);
            joinSessionButton.setDisable(true);
            joinInfoLabel.setText("Joining ...");
            Ctx.getInstance().sessionService.take(sessionId, result -> {
                if (result.status().isSuccess()) {
                    Platform.runLater(() -> {
                        joinInfoLabel.setText("Joined, name: " + result.entity().get().getName());
                        Ctx.getInstance().sceneManager.load(EditorScene.class);
                    });
                } else if (result.status().isError()) {
                    if (result.status() == ResultStatus.ERROR_UNAVAILABLE) {
                        Platform.runLater(() -> {
                            joinInfoLabel.setText("Session not found");
                            joinSessionButton.setDisable(false);
                        });
                    } else {
                        Platform.runLater(() -> {
                            joinInfoLabel.setText(result.message().get());
                            joinSessionButton.setDisable(false);
                        });
                    }
                } else {
                    Platform.runLater(() -> {
                        joinInfoLabel.setText("Fail");
                        joinSessionButton.setDisable(false);
                    });
                }
            });
        }

    }

}