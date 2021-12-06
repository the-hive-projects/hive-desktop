package org.thehive.hivedesktop.scene;

import javafx.scene.Scene;

import java.io.IOException;
import java.util.Optional;

public interface AppScene {

    Optional<AppController> getController();

    void setController(AppController controller);

    Scene getScene() throws IOException;

}
