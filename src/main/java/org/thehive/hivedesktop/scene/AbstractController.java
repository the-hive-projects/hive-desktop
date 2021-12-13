package org.thehive.hivedesktop.scene;

import lombok.NonNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class AbstractController implements AppController {

    protected AbstractController(@NonNull AppSceneManager sceneManager, @NonNull Class<? extends AppScene> sceneType) {
        sceneManager.get(sceneType).ifPresent(scene -> {
            scene.setController(this);
        });
    }

    public AbstractController() {

    }

    @Override
    public final void initialize(URL location, ResourceBundle resources) {
        try {
            this.onStart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
