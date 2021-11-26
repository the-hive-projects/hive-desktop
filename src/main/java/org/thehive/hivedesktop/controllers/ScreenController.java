package org.thehive.hivedesktop.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.thehive.hivedesktop.EditorView;
import org.thehive.hivedesktop.InboxView;
import org.thehive.hivedesktop.SessionView;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;

public class ScreenController {

    public void switchToInboxView(MouseEvent event, InboxView inboxView) throws IOException {
        inboxView.start(new Stage());
    }
    public void switchToEditorView(MouseEvent event, EditorView editorView) throws IOException {
        editorView.start(new Stage());
    }
    public void switchToISessionView(MouseEvent event, SessionView sessionView) throws IOException {
        sessionView.start(new Stage());
    }

}