package org.thehive.hivedesktop.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.NonNull;

import java.io.IOException;

public class FxmlMultipleLoadedScene extends FxmlScene {

    protected FxmlMultipleLoadedScene(@NonNull String fxmlFilename) {
        super(fxmlFilename);
    }

    @Override
    protected Scene createScene(@NonNull Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppScene.class.getResource(super.fxmlFilename));
        return new Scene(fxmlLoader.load());
    }

}
