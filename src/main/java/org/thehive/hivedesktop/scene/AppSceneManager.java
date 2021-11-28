package org.thehive.hivedesktop.scene;

public interface AppSceneManager {

    void add(AppScene scene);

    boolean addIfNotExists(AppScene scene);

    void remove(String name);

    boolean contains(String name);

    void load(String name);

    AppScene currentScene();

}
