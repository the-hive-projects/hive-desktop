package org.thehive.hivedesktop.scene;

public interface AppSceneManager {

    void add(AppScene scene);

    boolean addIfNotExists(AppScene scene);

    void remove(Class<? extends AppScene> type);

    boolean contains(Class<? extends AppScene> type);

    void load(Class<? extends AppScene> type);

    AppScene currentScene();

}
