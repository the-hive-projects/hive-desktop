package org.thehive.hivedesktop.controllers;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.thehive.hivedesktop.EditorView;
import org.thehive.hivedesktop.InboxView;
import org.thehive.hivedesktop.ProfileDialogView;
import org.thehive.hivedesktop.scene.MainScene;

import java.io.IOException;

public class ScreenController {

    public void switchToInboxView(MouseEvent event, InboxView inboxView) throws IOException {
        inboxView.start(new Stage());
    }
    public void switchToEditorView(MouseEvent event, EditorView editorView) throws IOException {
        editorView.start(new Stage());
    }
    public void switchToISessionView(MouseEvent event, MainScene sessionView) throws IOException {

    }
    public void showProfileDialogView(ProfileDialogView profileDialogView) throws IOException {
        profileDialogView.start(new Stage());
    }


}