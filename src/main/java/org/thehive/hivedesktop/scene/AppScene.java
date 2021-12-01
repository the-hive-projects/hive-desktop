package org.thehive.hivedesktop.scene;

import javafx.stage.Stage;

import java.io.IOException;

public interface AppScene {

    void load(Stage stage) throws IOException;

    void onLoad();

}
