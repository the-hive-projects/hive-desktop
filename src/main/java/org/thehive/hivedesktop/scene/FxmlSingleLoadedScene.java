package org.thehive.hivedesktop.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.NonNull;

import java.io.IOException;

public class FxmlSingleLoadedScene extends FxmlScene {

    private Scene scene;

    protected FxmlSingleLoadedScene(@NonNull String fxmlFilename) {
        super(fxmlFilename);
    }

    @Override
    protected Scene createScene(@NonNull Stage stage) throws IOException {
        if (scene == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(AppScene.class.getResource(fxmlFilename));
            this.scene = new Scene(fxmlLoader.load());
        }
        return scene;
    }

}
