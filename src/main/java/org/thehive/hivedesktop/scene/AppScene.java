package org.thehive.hivedesktop.scene;

import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public interface AppScene {

    void setController(AppController controller);

    Optional<AppController> getController();

    void load(Stage stage) throws IOException;

}
