package org.thehive.hivedesktop;


import com.kodedu.terminalfx.helper.ThreadHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Objects;


public class TerminalAppStarter extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        InputStream sceneStream = TerminalAppStarter.class.getResourceAsStream("terminal-view.fxml");
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(sceneStream);

        Scene scene = new Scene(root);


        stage.setTitle("TerminalFX");
        stage.setScene(scene);
        stage.show();
    }



    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}