package org.thehive.hivedesktop.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileDialogViewController implements Initializable {

    @FXML
    private Button btnClose;


    public void hideProfile()
    {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


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