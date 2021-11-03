package org.thehive.hivedesktop;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.thehive.hiveserverclient.model.User;
import org.thehive.hiveserverclient.net.http.RequestCallback;
import org.thehive.hiveserverclient.net.http.UserClientImpl;
import org.thehive.hiveserverclient.service.UserServiceImpl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Controller implements Initializable {
    @FXML
    private Button btnClose;

    @FXML
    private MFXTextField tfUsername;

    @FXML
    private MFXPasswordField pfPassword;

    @FXML
    private MFXButton btnLogin;

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

    public void switchToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("HiveDesktopApplication.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
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
                errorMessage = "";
                if (isFieldFilled()) {
                    var u = tfUsername.getText();
                    var p = pfPassword.getPassword();


                    var defaultUserClient = new UserClientImpl("http://localhost:8080", HttpClients.createSystem(), new ObjectMapper(), (ThreadPoolExecutor) Executors.newCachedThreadPool());


                    var service = new UserServiceImpl(defaultUserClient);

                    service.signIn(u, p, r -> {

                        System.out.println(r.status.name());

                    });

                }
            }
        });
    }
}
