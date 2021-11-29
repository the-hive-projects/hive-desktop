package org.thehive.hivedesktop;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.scene.SignIn;
import org.thehive.hivedesktop.scene.SignUp;

@Slf4j
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        log.info("Application started");
        Ctx.initialize();
    }

    @Override
    public void stop() throws Exception {
        log.info("Application stopped");
    }

    @Override
    public void start(Stage stage) throws Exception {
        Ctx.getInstance().sceneManager.setStage(stage);
        Ctx.getInstance().sceneManager.add(new SignIn());
        Ctx.getInstance().sceneManager.add(new SignUp());
        Ctx.getInstance().sceneManager.load(SignIn.class);
    }

}
