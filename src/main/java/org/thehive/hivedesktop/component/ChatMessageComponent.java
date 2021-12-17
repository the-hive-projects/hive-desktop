package org.thehive.hivedesktop.component;

import com.jfoenix.controls.JFXListCell;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.thehive.hivedesktop.ProfileDialogView;
import org.thehive.hivedesktop.util.ImageUtils;
import org.thehive.hiveserverclient.model.Image;
import org.thehive.hiveserverclient.payload.ChatMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ChatMessageComponent implements Component<Pane> {

    public final ChatMessage chatMessage;
    private final Pane pane;


    // TODO: 12/16/2021 add default image in resources
    @SneakyThrows
    public ChatMessageComponent(@NonNull ChatMessage chatMessage, @NonNull Image image) throws IOException {
        this.chatMessage = chatMessage;
        var scaledContent = ImageUtils.scaleImageContent(image.getContent(), 20, 20);
        pane = new Pane();
        pane.setMinWidth(100);
        pane.setMinHeight(100);
        var usernameHyperLink = createUser(chatMessage, scaledContent);
        var messageLabel = createLabel(chatMessage.getText());
        var line = createLine();
        pane.getChildren().addAll(messageLabel, line, usernameHyperLink);
    }

    @Override
    public Pane getParentNode() {
        return pane;
    }

    private Hyperlink createUser(ChatMessage chatMessage, byte[] profileImageContent) {
        Hyperlink userName = new Hyperlink();
        Font font = Font.font("Helvetica", FontWeight.BOLD,
                FontPosture.REGULAR, 10);
        userName.setFont(font);
        userName.setMinWidth(100);
        userName.setText(chatMessage.getFrom());
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


    private TextArea createLabel(String text) {
        TextArea messageLabel = new TextArea();
        messageLabel.setEditable(false);
        messageLabel.setWrapText(true);
        messageLabel.wrapTextProperty().set(true);
        messageLabel.setPrefHeight(50);
        Font font = Font.font("Helvetica", FontWeight.NORMAL,
                FontPosture.REGULAR, 12);
        messageLabel.setFont(font);
        messageLabel.setPadding(new Insets(50, 10, 5, 10));
        messageLabel.setStyle("-fx-background-color:transparent;  -fx-text-area-background:#373737; text-area-background:#373737; -fx-text-fill:#ffc107;");
        messageLabel.setText(text);
        return messageLabel;
    }


    private Line createLine() {
        Line line = new Line();
        line.setStartX(0);
        line.setStartY(100);
        line.setEndY(100);
        line.setEndX(300);
        line.setStyle("-fx-background-color:#ffc107; -fx-stroke: #ffc107; -fx-opacity: 0.5; ");
        return line;
    }



}
