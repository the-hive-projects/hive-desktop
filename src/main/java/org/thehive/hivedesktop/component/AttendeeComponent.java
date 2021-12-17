package org.thehive.hivedesktop.component;

import com.jfoenix.controls.JFXListCell;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import lombok.SneakyThrows;
import org.thehive.hivedesktop.util.ImageUtils;
import org.thehive.hiveserverclient.model.Image;

import java.io.ByteArrayInputStream;

public class AttendeeComponent implements Component<Pane> {

    public final String username;
    private final Pane pane;

    @SneakyThrows
    public AttendeeComponent(String username, Image image) {
        this.username = username;
        var scaledContent = ImageUtils.scaleImageContent(image.getContent(), 20, 20);
        var attendeeLabel = createAttendenceItem(username,scaledContent);

        pane = new Pane();

        pane.getChildren().add(attendeeLabel);
    }

    @Override
    public Pane getParentNode() {
        return pane;
    }

    private JFXListCell<Label> createAttendenceItem(String username, byte[] profileImageContent) {
        JFXListCell<Label> listCell = new JFXListCell<Label>();
        listCell.setStyle("-fx-background-color:#ffc107; -fx-background-radius:15; ");

        var image = new javafx.scene.image.Image(new ByteArrayInputStream(profileImageContent));
        listCell.setText(username);
        listCell.setGraphic(new ImageView(image));
        listCell.setMinWidth(100);




        listCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //setCode(codeEditor, "python", "vs-dark");
                //TODO user info
                // profileTab = createUser();

            }
        });

        return listCell;
    }



}
