package org.thehive.hivedesktop.scene;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;
import org.thehive.hivedesktop.Ctx;

public class MainScene extends FxmlSingleLoadedScene {

    private static final String FXML_FILENAME = "main.fxml";

    public MainScene() {
        super(FXML_FILENAME);
    }

    @Slf4j
    public static class Controller extends AbstractController {

        private static final Class<? extends AppScene> SCENE_TYPE = MainScene.class;

        @FXML
        private MFXButton btCreateSession;

        @FXML
        private MFXButton btJoinSession;

        @FXML
        private ImageView image;

        @FXML
        private Label lbEmail;

        @FXML
        private Label lbFullname;

        @FXML
        private Label lbUsername;

        @FXML
        private MFXTextField tfJoinSession;

        protected Controller() {
            super(Ctx.getInstance().sceneManager, SCENE_TYPE);
        }

        @Override
        public void onStart() {
            log.info("MainScene#onStart");
        }

        @Override
        public void onLoad() {
            log.info("MainScene#onLoad");
        }

        @Override
        public void onUnload() {
            log.info("MainScene#onUnload");
        }

    }

}