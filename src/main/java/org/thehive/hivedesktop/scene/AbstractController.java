package org.thehive.hivedesktop.scene;

import lombok.NonNull;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public abstract class AbstractController implements AppController {

    protected AbstractController(@NonNull AppSceneManager sceneManager, @NonNull Class<? extends AppScene> sceneType) {
        sceneManager.get(sceneType).ifPresent(scene -> {
            scene.setController(this);
        });
    }

    @Override
    public abstract void onStart();

    @Override
    public abstract void onLoad(Map<String,Object> data);

    @Override
    public abstract void onUnload();

    @Override
    public final void initialize(URL location, ResourceBundle resources) {
        this.onStart();
    }

}
