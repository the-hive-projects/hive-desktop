package org.thehive.hivedesktop;

import javafx.application.Application;
import javafx.stage.Stage;
import org.thehive.hivedesktop.scene.AppSceneManager;
import org.thehive.hivedesktop.scene.AppSceneManagerImpl;
import org.thehive.hivedesktop.scene.SignIn;

public class App extends Application {

    public static AppSceneManager sceneManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        sceneManager = new AppSceneManagerImpl(stage);
        var signIn = new SignIn("SignIn", "sign-in.fxml");
        sceneManager.add(signIn);
        sceneManager.load("SignIn");
    }

}
