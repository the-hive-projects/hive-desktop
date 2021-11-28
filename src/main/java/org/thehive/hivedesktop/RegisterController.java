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
public class RegisterController implements Initializable {
    @FXML
    private Button btnClose;

    @FXML
    private MFXButton btnRegister;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private MFXPasswordField pfPassword;

    @FXML
    private MFXTextField tfEmail;

    @FXML
    private MFXTextField tfName;

    @FXML
    private MFXTextField tfSurname;

    @FXML
    private MFXTextField tfUsername;

    private String errorMessage = "";

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void switchToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("HiveDesktopApplication.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private boolean isFieldFilled() {
        boolean isFilled = true;
        if (tfName.getText() == null || tfName.getText().isEmpty()) {
            isFilled = false;
            errorMessage = "Name is Empty";
            System.out.println("Name is Empty");
        }
        if (tfSurname.getText() == null || tfSurname.getText().isEmpty()) {
            isFilled = false;
            errorMessage = "Surname is Empty";
            System.out.println("Surname is Empty");
        }
        if (tfUsername.getText() == null || tfUsername.getText().isEmpty()) {
            isFilled = false;
            errorMessage = "Username is Empty";
            System.out.println("Username is Empty");
        }
        if (tfEmail.getText() == null || tfEmail.getText().isEmpty()) {
            isFilled = false;
            errorMessage = "Email is Empty";
            System.out.println("Email is Empty");
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnClose.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.exit(0);
            }
        });

        btnRegister.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                log.info("clicked");
                errorMessage = "";
                var name = tfName.getText().trim();
                var surname = tfSurname.getText().trim();
                var username = tfUsername.getText().trim();
                var email = tfEmail.getText().trim();
                var password = pfPassword.getPassword().trim();
            }
        });
    }
}
