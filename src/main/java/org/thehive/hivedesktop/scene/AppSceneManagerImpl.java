package org.thehive.hivedesktop.scene;

import javafx.stage.Stage;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AppSceneManagerImpl implements AppSceneManager {

    private final Stage stage;
    private final Map<String, AppScene> nameSceneMap;
    private AppScene currentScene;

    public AppSceneManagerImpl(@NonNull Stage stage) {
        this.stage = stage;
        this.nameSceneMap = new HashMap<>();
        this.currentScene = null;
    }

    @Override
    public void add(@NonNull AppScene scene) {
        nameSceneMap.put(scene.getName(), scene);
        log.info("AppScene was added successfully, name: {}", scene.getName());
    }

    @Override
    public boolean addIfNotExists(@NonNull AppScene scene) {
        if (!contains(scene.getName())) {
            nameSceneMap.put(scene.getName(), scene);
            log.info("AppScene was added successfully, name: {}", scene.getName());
            return true;
        }
        log.warn("AppScene has already been added, name: {}", scene.getName());
        return false;
    }

    @Override
    public void remove(@NonNull String name) {
        var scene = nameSceneMap.remove(name);
        if (scene != null)
            log.info("AppScene was removed successfully, name: {}", name);
        else
            log.warn("AppScene was not removed, name: {}", name);
    }


    @Override
    public boolean contains(String name) {
        return nameSceneMap.containsKey(name);
    }

    @SneakyThrows
    @Override
    public void load(@NonNull String name) {
        if (contains(name)) {
            var scene = nameSceneMap.get(name);
            scene.load(stage);
            this.currentScene = scene;
            log.info("Scene was loaded successfully, name: {}", name);
        } else
            log.warn("AppScene is not found, name: {}", name);
    }

    @Override
    public AppScene currentScene() {
        return currentScene;
    }

}
