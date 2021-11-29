package org.thehive.hivedesktop.scene;

import javafx.stage.Stage;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AppSceneManagerImpl implements AppSceneManager {

    private final Stage stage;
    private final Map<Class<? extends AppScene>, AppScene> nameSceneMap;
    private AppScene currentScene;

    public AppSceneManagerImpl(@NonNull Stage stage) {
        this.stage = stage;
        this.nameSceneMap = new HashMap<>();
        this.currentScene = null;
    }

    @Override
    public void add(@NonNull AppScene scene) {
        nameSceneMap.put(scene.getClass(), scene);
        log.info("AppScene was added successfully, name: {}", scene.getClass().getName());
    }

    @Override
    public boolean addIfNotExists(@NonNull AppScene scene) {
        if (!contains(scene.getClass())) {
            add(scene);
            return true;
        }
        log.warn("AppScene has already been added, name: {}", scene.getClass().getName());
        return false;
    }

    @Override
    public void remove(@NonNull Class<? extends AppScene> type) {
        var scene = nameSceneMap.remove(type);
        if (scene != null)
            log.info("AppScene was removed successfully, name: {}", type.getName());
        else
            log.warn("AppScene was not removed, name: {}", type.getName());
    }

    @Override
    public boolean contains(@NonNull Class<? extends AppScene> type) {
        return nameSceneMap.containsKey(type);
    }

    @SneakyThrows
    @Override
    public void load(@NonNull Class<? extends AppScene> type) {
        if (contains(type)) {
            var scene = nameSceneMap.get(type);
            scene.load(stage);
            this.currentScene = scene;
            log.info("Scene was loaded successfully, name: {}", type.getName());
        } else
            log.warn("AppScene is not found, name: {}", type.getName());
    }

    @Override
    public AppScene currentScene() {
        return currentScene;
    }

}
