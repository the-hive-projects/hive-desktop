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
    public final void load(Stage stage) throws IOException {
        stage.setScene(createScene(stage));
        if (!stage.isShowing())
            stage.show();
    }

    public String getFxmlFilename() {
        return fxmlFilename;
    }

    protected abstract Scene createScene(@NonNull Stage stage) throws IOException;

}