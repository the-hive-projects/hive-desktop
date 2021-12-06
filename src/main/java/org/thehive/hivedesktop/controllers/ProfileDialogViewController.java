package org.thehive.hivedesktop.controllers;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.thehive.hivedesktop.Ctx;
import org.thehive.hivedesktop.util.ImageUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileDialogViewController implements Initializable {

    @FXML
    private Button btnClose;

    @FXML
    private ImageView imageProfile;

    @FXML
    private Label lblEmail, lblFullName, lblUsername;


    public void fillProfile() {

        //TODO Hyperlink for image who sent message
        Ctx.getInstance().userService.profile(profileResult -> {
            if (profileResult.status().isSuccess()) {
                var user = profileResult.entity().get();
                Platform.runLater(() -> {
                    lblUsername.setText(user.getUsername());
                    lblFullName.setText(user.getUserInfo().getFirstname() + " " + user.getUserInfo().getLastname());
                    lblEmail.setText(user.getEmail());

                });

                Ctx.getInstance().imageService.take(user.getUsername(), imageResult -> {
                    var content = imageResult.entity().get().getContent();
                    var profileImage = new Image(new ByteArrayInputStream(content));
                    Platform.runLater(() -> imageProfile.setImage(profileImage));
                    try {
                        var scaledContent = ImageUtils.scaleImageContent(content, 20, 20);
                        var scaledProfileImage = new Image(new ByteArrayInputStream(scaledContent));
                        Platform.runLater(() -> {
                            imageProfile.setImage(scaledProfileImage);

                        });
                    } catch (IOException e) {
                        //log.warn("Error while scaling profile image", e);
                    }
                });
            }
        });


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fillProfile();


        btnClose.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //TODO Close DialogBox
                Stage stage = (Stage) btnClose.getScene().getWindow();
                stage.close();
            }
        });


    }

}