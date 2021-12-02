package org.thehive.hivedesktop.scene;

import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.NonNull;

import java.io.IOException;

public abstract class FxmlScene extends AbstractScene {

    protected final String fxmlFilename;

    protected FxmlScene(@NonNull String fxmlFilename) {
        this.fxmlFilename = fxmlFilename;
    }

    @Override
    public final Scene getScene() throws IOException {
        return loadScene();
    }

    protected abstract Scene loadScene() throws IOException;

    public String getFxmlFilename() {
        return fxmlFilename;
    }

}
