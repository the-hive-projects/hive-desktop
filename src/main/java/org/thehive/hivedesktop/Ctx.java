package org.thehive.hivedesktop;

import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.scene.AppSceneManager;
import org.thehive.hivedesktop.scene.AppSceneManagerImpl;

@Slf4j
public class Ctx {

    private static Ctx instance;

    public final AppSceneManager sceneManager;

    public Ctx(Stage stage) {
        this.sceneManager = new AppSceneManagerImpl(stage);
    }

    public static Ctx getInstance() {
        return instance;
    }

    public static void initialize(Stage stage) {
        if (instance == null) {
            instance = new Ctx(stage);
            log.info("Context instance was initialized successfully");
        } else
            log.warn("Context instance has already been initialized");
    }

}
