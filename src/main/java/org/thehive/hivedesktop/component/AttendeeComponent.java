package org.thehive.hivedesktop.component;

import com.jfoenix.controls.JFXListCell;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.thehive.hivedesktop.util.ImageUtils;
import org.thehive.hiveserverclient.model.Image;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class AttendeeComponent implements Component<Pane> {

    private static final String COLOR_BG_OWNER = "#F07B72";
    private static final String COLOR_BG_ATTENDEE = "#ffc107";

    public final String username;
    private final Pane pane;

    public AttendeeComponent(String username, Image image, boolean isOwner) {
        this.username = username;
        byte[] scaledContent = new byte[0];
        try {
            scaledContent = ImageUtils.scaleImageContent(image.getContent(), 20, 20);
        } catch (IOException e) {
            e.printStackTrace();
        }
        var attendeeLabel = createAttendeeItem(username, scaledContent, isOwner);
        pane = new Pane();
        pane.getChildren().add(attendeeLabel);
    }

    @Override
    public Pane getParentNode() {
        return pane;
    }

    private JFXListCell<Label> createAttendeeItem(String username, byte[] profileImageContent, boolean isOwner) {
        JFXListCell<Label> listCell = new JFXListCell<>();
        if (isOwner)
            listCell.setStyle("-fx-background-color:" + COLOR_BG_OWNER + "; -fx-background-radius:15; ");
        else
            listCell.setStyle("-fx-background-color:" + COLOR_BG_ATTENDEE + "; -fx-background-radius:15; ");
        if (profileImageContent.length > 0) {
            var image = new javafx.scene.image.Image(new ByteArrayInputStream(profileImageContent));
            listCell.setGraphic(new ImageView(image));
        }
        listCell.setText(username);
        listCell.setMinWidth(100);
        return listCell;
    }


}
