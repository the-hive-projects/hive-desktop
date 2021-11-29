package org.thehive.hivedesktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;

public class EditorView extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EditorView.class.getResource("editor-view.fxml"));
        stage.getIcons().add(new Image(InboxView.class.getResourceAsStream("/img/logo2.png")));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setTitle("HIVE Editor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}