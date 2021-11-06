package org.thehive.hivedesktop;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import lombok.extern.slf4j.Slf4j;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.http.impl.client.HttpClients;
import org.thehive.hiveserverclient.model.User;
import org.thehive.hiveserverclient.model.UserInfo;
import org.thehive.hiveserverclient.net.http.UserClientImpl;
import org.thehive.hiveserverclient.service.UserServiceImpl;
import org.thehive.hiveserverclient.service.status.SignUpStatus;
import org.thehive.hiveserverclient.util.MessageUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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

                    var defaultUserClient = new UserClientImpl("http://localhost:8080/user", HttpClients.createSystem(), new ObjectMapper(), (ThreadPoolExecutor) Executors.newCachedThreadPool());
                    var service = new UserServiceImpl(defaultUserClient);

                    var user = new User(0,username,email,password,new UserInfo(0,name,surname,0L));
                    service.signUp(user,r->{
                        if(r.status() == SignUpStatus.VALID){
                            System.out.println("Successfully SignedUp");

                        }
                        else if(r.status() == SignUpStatus.INVALID){
                            var messageList = MessageUtils.parsePairedMessageList(r.message().get(),",",":","[","]");
                            System.out.println(messageList);
                            var sb=new StringBuilder();
                            messageList.stream().map(i->i.value).forEach(v->sb.append(v+"\n"));
                            var msg=sb.toString();
                            System.out.println(msg);
                            Platform.runLater(()->{
                                errorMessageLabel.setText(msg);
                            });
                        }
                        else if(r.status() == SignUpStatus.FAIL){
                            Platform.runLater(()->{
                                errorMessageLabel.setText("Connection Error");
                            });
                        }

                    });



            }
        });
    }
}
