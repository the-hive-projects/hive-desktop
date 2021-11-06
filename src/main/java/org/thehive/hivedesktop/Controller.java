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
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.HttpClients;
import org.thehive.hivedesktop.HelloApplication;
import org.thehive.hiveserverclient.net.http.UserClientImpl;
import org.thehive.hiveserverclient.service.UserServiceImpl;
import org.thehive.hiveserverclient.service.status.SignInStatus;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class Controller implements Initializable {
    @FXML
    private Button btnClose = new Button();

    @FXML
    private MFXTextField tfUsername;

    @FXML
    private MFXPasswordField pfPassword;

    @FXML
    private MFXButton btnLogin =new MFXButton();

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


                    var defaultUserClient = new UserClientImpl("http://localhost:8080/user", HttpClients.createSystem(), new ObjectMapper(), (ThreadPoolExecutor) Executors.newCachedThreadPool());
                    var service = new UserServiceImpl(defaultUserClient);

                    service.signIn(u, p, r -> {
                        if(r.status() == SignInStatus.INCORRECT){
                            Platform.runLater(()->{
                                errorMessageLabel.setText("Incorret Credentials");
                            });
                        }
                        else if(r.status() == SignInStatus.FAIL){
                            Platform.runLater(()->{
                                errorMessageLabel.setText("Connection Error");
                            });
                        }
                        else if(r.status() == SignInStatus.CORRECT){
                            Platform.runLater(()->{
                                try {
                                    root = FXMLLoader.load(getClass().getResource("session-view.fxml"));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                                scene = new Scene(root);
                                stage.setScene(scene);
                                stage.show();
                            });
                        }


                    });

                }
            }
        });



    }
}
