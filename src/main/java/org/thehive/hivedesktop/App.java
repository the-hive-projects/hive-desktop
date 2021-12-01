package org.thehive.hivedesktop;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.scene.*;

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
        Ctx.getInstance().sceneManager.add(new SignInScene());
        Ctx.getInstance().sceneManager.add(new SignUpScene());
        Ctx.getInstance().sceneManager.add(new MainScene());
        Ctx.getInstance().sceneManager.add(new EditorScene());
        Ctx.getInstance().sceneManager.add(new InboxScene());


        Ctx.getInstance().sceneManager.load(EditorScene.class);
    }

}
