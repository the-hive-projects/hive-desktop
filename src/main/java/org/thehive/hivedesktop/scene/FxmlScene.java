package org.thehive.hivedesktop.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.NonNull;

import java.io.IOException;

public abstract class FxmlScene extends AbstractScene {

    protected final String fxmlFile;

    protected FxmlScene(String name, @NonNull String fxmlFile) {
        super(name);
        this.fxmlFile = fxmlFile;
    }

    @Override
    public final void load(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignIn.class.getResource(fxmlFile));
        javafx.scene.Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public final String getFxmlFile() {
        return fxmlFile;
    }

}
