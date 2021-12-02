package org.thehive.hivedesktop.scene;

import javafx.stage.Stage;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class AppSceneManagerImpl implements AppSceneManager {

    private final Map<Class<? extends AppScene>, AppScene> nameSceneMap;
    private Stage stage;
    private AppScene currentScene;

    public AppSceneManagerImpl() {
        this(null);
    }

    public AppSceneManagerImpl(Stage stage) {
        this.stage = stage;
        this.nameSceneMap = new HashMap<>();
        this.currentScene = null;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
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
    @SuppressWarnings("unchecked")
    public <S extends AppScene> Optional<S> get(Class<S> sceneType) {
        return Optional.ofNullable((S) nameSceneMap.get(sceneType));
    }

    @Override
    public void remove(@NonNull Class<? extends AppScene> sceneType) {
        var scene = nameSceneMap.remove(sceneType);
        if (scene != null)
            log.info("AppScene was removed successfully, name: {}", sceneType.getName());
        else
            log.warn("AppScene was not removed, name: {}", sceneType.getName());
    }

    @Override
    public boolean contains(@NonNull Class<? extends AppScene> sceneType) {
        return nameSceneMap.containsKey(sceneType);
    }

    @SneakyThrows
    @Override
    public void load(@NonNull Class<? extends AppScene> sceneType) {
        this.load(sceneType, null);
    }

    @SneakyThrows
    @Override
    public void load(@NonNull Class<? extends AppScene> sceneType, Map<String, Object> data) {
        if (stage != null) {
            if (contains(sceneType)) {
                var loadedScene = nameSceneMap.get(sceneType);
                if (currentScene != null)
                    currentScene.getController().ifPresent(AppController::onUnload);
                stage.setScene(loadedScene.getScene());
                loadedScene.getController().ifPresent(appController -> {
                    if (data == null)
                        appController.onLoad(Collections.emptyMap());
                    else
                        appController.onLoad(Collections.unmodifiableMap(data));
                });
                this.currentScene = loadedScene;
                log.info("Scene was loaded successfully, name: {}", sceneType.getName());
            } else
                log.warn("AppScene is not found, name: {}", sceneType.getName());
        } else
            log.warn("Stage in manager is null");
    }

    @Override
    public AppScene currentScene() {
        return currentScene;
    }

}
