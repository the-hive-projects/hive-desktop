package org.thehive.hivedesktop.scene;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public interface AppScene {

    void setController(AppController controller);

    Optional<AppController> getController();

    Scene getScene() throws IOException;

}
