package org.thehive.hivedesktop;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class Controller implements Initializable {
    @FXML
    private Button btnClose = new Button();

    @FXML
    private MFXTextField tfUsername;

    @FXML
    private MFXPasswordField pfPassword;

    @FXML
    private MFXButton btnLogin = new MFXButton();

    @FXML
    private Label errorMessageLabel;

    private String errorMessage = "";

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToRegister(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("register-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void handleButtonAction(ActionEvent event) throws IOException {
        System.out.println("You clicked me!");
        root = FXMLLoader.load(getClass().getResource("session-view.fxml"));
        scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide(); //optional
        stage.setScene(scene);
        stage.show();
    }


    private boolean isFieldFilled() {
        boolean isFilled = true;
        if (tfUsername.getText() == null || tfUsername.getText().isEmpty()) {
            isFilled = false;
            errorMessage = "Username is Empty";
        }

        if (pfPassword.getText().isEmpty()) {
            isFilled = false;
            if (errorMessage.isEmpty()) {
                errorMessage = "Password is Empty";
            } else {
                errorMessage += "\nPassword is Empty";
            }
        }

        errorMessageLabel.setText(errorMessage);
        return isFilled;
    }

    private boolean isValid() {
        boolean isValid = true;
        if (!tfUsername.getText().equals(HelloApplication.USERNAME)) {
            isValid = false;
            errorMessage = "Invalid Username";
        }

        if (!pfPassword.getText().equals(HelloApplication.PASSWORD)) {
            isValid = false;
            if (errorMessage.isEmpty()) {
                errorMessage = "Invalid Password";
            } else {
                errorMessage += "\nInvalid Password";
            }
        }

        errorMessageLabel.setText(errorMessage);
        return isValid;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        btnClose.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.exit(0);
            }
        });

        btnLogin.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                log.info("clicked");
                errorMessage = "";
                if (isFieldFilled()) {
                    var u = tfUsername.getText();
                    var p = pfPassword.getPassword();
                }
            }
        });


    }
}
