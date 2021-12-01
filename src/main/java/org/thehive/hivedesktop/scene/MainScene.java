package org.thehive.hivedesktop.scene;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainScene extends FxmlSingleLoadedScene {

    static final String FXML_FILENAME = "main.fxml";

    public MainScene() {
        super(FXML_FILENAME);
    }

    public static class Controller implements Initializable {

        @FXML
        private MFXButton btCreateSession;

        @FXML
        private MFXButton btJoinSession;

        @FXML
        private ImageView image;

        @FXML
        private Label lbEmail;

        @FXML
        private Label lbFullname;

        @FXML
        private Label lbUsername;

        @FXML
        private MFXTextField tfJoinSession;

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

        }

    }

}