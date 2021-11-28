package org.thehive.hivedesktop.scene;

import javafx.stage.Stage;

import java.io.IOException;

public interface AppScene {

    String getName();

    void load(Stage stage) throws IOException;

}
