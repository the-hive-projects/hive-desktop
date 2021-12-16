package org.thehive.hivedesktop.component;

import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.thehive.hivedesktop.ProfileDialogView;
import org.thehive.hivedesktop.util.ImageUtils;
import org.thehive.hiveserverclient.model.Image;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class AttendeeComponent implements Component<Pane> {

    public final String username;
    private final Pane pane;

    @SneakyThrows
    public AttendeeComponent(String username, Image image) {
        this.username = username;
        pane = new Pane();
        var scaledContent = ImageUtils.scaleImageContent(image.getContent(), 20, 20);
        pane.getChildren().add(createAttendenceItem(username, scaledContent));
    }

    @Override
    public Pane getParentNode() {
        return pane;
    }

    private Hyperlink createAttendenceItem(String username, byte[] profileImageContent) {
        Hyperlink userName = new Hyperlink();
        Font font = Font.font("Helvetica", FontWeight.BOLD,
                FontPosture.REGULAR, 10);
        userName.setFont(font);
        userName.setText(username);
        userName.setPadding(new Insets(10, 10, 5, 10));
        userName.setTextFill(Color.web("#ffc107"));
        var image = new javafx.scene.image.Image(new ByteArrayInputStream(profileImageContent));
        userName.setGraphic(new ImageView(image));
        userName.setOnMouseClicked(mouseEvent -> {
            ProfileDialogView profileDialogView = new ProfileDialogView();
            try {
                profileDialogView.start(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return userName;
    }

}
