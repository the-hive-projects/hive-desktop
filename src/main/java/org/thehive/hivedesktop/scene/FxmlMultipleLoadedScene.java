package org.thehive.hivedesktop.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import lombok.NonNull;

import java.io.IOException;

public class FxmlMultipleLoadedScene extends FxmlScene {

    protected FxmlMultipleLoadedScene(@NonNull String fxmlFilename) {
        super(fxmlFilename);
    }

    @Override
    protected Scene loadScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppScene.class.getResource(super.fxmlFilename));
        return new Scene(fxmlLoader.load());
    }

}
