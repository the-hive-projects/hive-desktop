package org.thehive.hivedesktop.scene;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
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
import org.thehive.hiveserverclient.model.Session;
import org.thehive.hiveserverclient.service.ResponseStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

public class MainScene extends FxmlMultipleLoadedScene {

    private static final String FXML_FILENAME = "main.fxml";

    public MainScene() {
        super(FXML_FILENAME);
    }

    @Slf4j
    public static class Controller extends AbstractController {

        private static final Class<? extends AppScene> SCENE_TYPE = MainScene.class;
        Stage stage;
        @FXML
        private Button btnCopyClipboard;

        @FXML
        private MFXFilterComboBox<String> cmbSessionDuration;

        @FXML
        private MFXButton createSessionButton;

        @FXML
        private Label emailLabel;

        @FXML
        private Label joinInfoLabel;

        @FXML
        private MFXButton joinSessionButton;

        @FXML
        private MFXButton btnInbox;

        @FXML
        private MFXTextField joinSessionIdTextField;

        @FXML
        private MFXButton logoutButton;

        @FXML
        private BorderPane mainPane;

        @FXML
        private Label nameLabel;

        @FXML
        private ImageView profileImageView;

        @FXML
        private Label txSessionLabel;

        @FXML
        private MFXTextField txtSessionName;

        @FXML
        private Label usernameLabel;

        public Controller() {
            super(Ctx.getInstance().sceneManager, SCENE_TYPE);
        }

        public static void copyToClipboardText(String s) {

            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();

            content.putString(s);
            clipboard.setContent(content);

        }

        public void handle(ActionEvent event) {
            copyToClipboardText(txSessionLabel.getText());
        }

        public void logout(ActionEvent event) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("You're about to logout!");
            alert.setContentText("Do you want to save before exiting?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                stage = (Stage) mainPane.getScene().getWindow();
                System.out.println("You successfully logged out!");
                Authentication.INSTANCE.unauthenticate();
                if (Ctx.getInstance().webSocketService.hasConnection())
                    Ctx.getInstance().webSocketService.getConnection().get().disconnect();
                Ctx.getInstance().sceneManager.load(SignInScene.class);
            }
        }

        @Override
        public void onStart() {
            log.info("MainScene#onStart");
            cmbSessionDuration.setItems(FXCollections.observableArrayList("20", "30", "40", "50", "60", "70", "80", "90"));
        }

        @Override
        @SuppressWarnings("OptionalGetWithoutIsPresent")
        public void onLoad(Map<String, Object> dataMap) {
            log.info("MainScene#onLoad");
            Ctx.getInstance().userService.profile(profileResult -> {
                if (profileResult.status().isSuccess()) {
                    var user = profileResult.response().get();
                    ExecutionUtils.runOnUiThread(() -> {
                        usernameLabel.setText(user.getUsername());
                        emailLabel.setText(user.getEmail());
                        nameLabel.setText(user.getUserInfo().getFirstname() + " " + user.getUserInfo().getLastname());
                    });
                    Ctx.getInstance().imageService.take(user.getUsername(), imageResult -> {
                        var content = imageResult.response().get().getContent();
                        var profileImage = new Image(new ByteArrayInputStream(content));
                        ExecutionUtils.runOnUiThread(() -> profileImageView.setImage(profileImage));
                        try {
                            var scaledContent = ImageUtils.scaleImageContent(content, 800, 800);
                            var scaledProfileImage = new Image(new ByteArrayInputStream(scaledContent));
                            ExecutionUtils.runOnUiThread(() -> profileImageView.setImage(scaledProfileImage));
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
        void onInboxButtonClick(MouseEvent event) {
            log.info("Button clicked, #onInboxButtonClick");
            Ctx.getInstance().sceneManager.load(InboxScene.class);
        }

        @FXML
        void onCreateSessionButtonClick(MouseEvent event) {
            log.info("Button clicked, #onCreateSessionButtonClick");
            var sessionName = txtSessionName.getText();
            var duration = Integer.parseInt(cmbSessionDuration.getSelectedValue()) * 1000L;
            var session = new Session();
            session.setName(sessionName);
            session.setDuration(duration);
            createSessionButton.setDisable(true);
            Ctx.getInstance().sessionService.create(session, appResponse -> {
                if (appResponse.status().isSuccess()) {
                    ExecutionUtils.runOnUiThread(() -> {
                        txSessionLabel.setText(appResponse.response().get().getLiveId());
                        createSessionButton.setDisable(false);
                    });
                } else {
                    ExecutionUtils.runOnUiThread(() -> {
                        txSessionLabel.setText("Something wrong");
                        createSessionButton.setDisable(false);
                    });
                }
            });
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
                    ExecutionUtils.runOnUiThread(() -> {
                        joinInfoLabel.setText("Joined, name: " + result.response().get().getName());
                        joinSessionButton.setDisable(false);
                        var dataMap = Map.of(
                                Consts.JOINED_SESSION_SCENE_DATA_KEY, result.response().get(),
                                Consts.JOINED_SESSION_LIVE_ID_SCENE_DATA_KEY, liveId);
                        Ctx.getInstance().sceneManager.load(EditorScene.class, dataMap);
                    });
                } else if (result.status().isError()) {
                    if (result.status() == ResponseStatus.ERROR_UNAVAILABLE) {
                        ExecutionUtils.runOnUiThread(() -> {
                            joinInfoLabel.setText("Session not found");
                            joinSessionButton.setDisable(false);
                        });
                    } else {
                        ExecutionUtils.runOnUiThread(() -> {
                            joinInfoLabel.setText(result.message().get());
                            joinSessionButton.setDisable(false);
                        });
                    }
                } else {
                    ExecutionUtils.runOnUiThread(() -> {
                        joinInfoLabel.setText("Fail");
                        joinSessionButton.setDisable(false);
                    });
                }
            });
        }

    }

}