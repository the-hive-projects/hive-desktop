package org.thehive.hivedesktop.scene;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public interface AppController extends Initializable {

    void onStart();

    void onLoad(Map<String, Object> data);

    void onUnload();

    @Override
    void initialize(URL location, ResourceBundle resources);

}
