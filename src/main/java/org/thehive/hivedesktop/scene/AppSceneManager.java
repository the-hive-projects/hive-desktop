package org.thehive.hivedesktop.scene;

import javafx.stage.Stage;

public interface AppSceneManager {

    Stage getStage();

    void setStage(Stage stage);

    void add(AppScene scene);

    boolean addIfNotExists(AppScene scene);

    void remove(Class<? extends AppScene> type);

    boolean contains(Class<? extends AppScene> type);

    void load(Class<? extends AppScene> type);

    AppScene currentScene();

}
