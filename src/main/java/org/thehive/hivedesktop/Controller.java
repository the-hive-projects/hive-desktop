package org.thehive.hivedesktop;

import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button btnClose;

    @FXML
    private MFXTextField tfUsername;

    @FXML
    private MFXPasswordField pfPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Label errorMessageLabel;

    private  String errorMessage = "";

    private  boolean isFieldFilled(){
        boolean isFilled = true;
        if(tfUsername.getText() == null || tfUsername.getText().isEmpty()){
            isFilled = false;
            errorMessage = "Username is Empty";
        }

        if(pfPassword.getText().isEmpty()){
            isFilled = false;
            if(errorMessage.isEmpty()){
                errorMessage = "Password is Empty";
            }else{
                errorMessage += "\nPassword is Empty";
            }
        }

        errorMessageLabel.setText(errorMessage);
        return  isFilled;
    }

    private boolean isValid(){
        boolean isValid = true;
        if(!tfUsername.getText().equals(HelloApplication.USERNAME)){
            isValid = false;
            errorMessage = "Invalid Username";
        }

        if(!pfPassword.getText().equals(HelloApplication.PASSWORD)){
            isValid = false;
            if(errorMessage.isEmpty()){
                errorMessage = "Invalid Password";
            } else{
                errorMessage += "\nInvalid Password";
            }
        }

        errorMessageLabel.setText(errorMessage);
        return  isValid;
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
                errorMessage = "";
                if(isFieldFilled() && isValid()){
                    //do something

                }
            }
        });
    }
}
