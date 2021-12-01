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
        private MFXTextField joinIdTextField;

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
        void onJoinSessionButtonClick(MouseEvent event) {
            log.info("Button clicked, #onJoinSessionButtonClick");
        }

    }

}