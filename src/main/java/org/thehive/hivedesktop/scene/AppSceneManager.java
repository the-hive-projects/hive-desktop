package org.thehive.hivedesktop.scene;

import javafx.stage.Stage;

import java.util.Map;
import java.util.Optional;

public interface AppSceneManager {

    Stage getStage();

    void setStage(Stage stage);

    void add(AppScene scene);

    boolean addIfNotExists(AppScene scene);

    <S extends AppScene> Optional<S> get(Class<S> sceneType);

    void remove(Class<? extends AppScene> sceneType);

    boolean contains(Class<? extends AppScene> sceneType);

    void load(Class<? extends AppScene> sceneType);

    void load(Class<? extends AppScene> sceneType, Map<String, Object> data);

    AppScene currentScene();

}
