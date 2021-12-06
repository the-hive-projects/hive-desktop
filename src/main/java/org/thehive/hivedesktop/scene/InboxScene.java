package org.thehive.hivedesktop.scene;

import com.jfoenix.controls.JFXListCell;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.config.TerminalConfig;
import eu.mihosoft.monacofx.MonacoFX;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class InboxScene extends FxmlMultipleLoadedScene {

    static final String FXML_FILENAME = "inbox.fxml";

    public InboxScene() {
        super(FXML_FILENAME);
    }

    public static class Controller implements Initializable {


        @FXML
        private final JFXListCell btnAttendeeDetails = new JFXListCell();
        public TabPane tabPane = new TabPane();
        @FXML
        private SplitPane rightSplitPane;
        private final Dictionary<String, MonacoFX> dict = new Hashtable<String, MonacoFX>();

        @FXML
        private MonacoFX setEditor(String language, String theme) {
            MonacoFX monacoFXeditor = new MonacoFX();
            int numTabs = dict.size();
            monacoFXeditor.setId("monacoFX" + numTabs);
            //TODO Student Code
            monacoFXeditor.getEditor().getDocument().setText("Student Code HERE");
            // use a predefined language like 'c'
            monacoFXeditor.getEditor().setCurrentLanguage(language);
            monacoFXeditor.getEditor().setCurrentTheme(theme);
            return monacoFXeditor;
        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {


            btnAttendeeDetails.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    rightSplitPane.setVisible(true);
                }
            });


//        Dark Config

            TerminalConfig darkConfig = new TerminalConfig();
            darkConfig.setBackgroundColor(Color.rgb(16, 16, 16));
            darkConfig.setForegroundColor(Color.rgb(240, 240, 240));
            darkConfig.setCursorColor(Color.rgb(255, 0, 0, 0.5));

//        CygWin Config
            TerminalConfig cygwinConfig = new TerminalConfig();
            cygwinConfig.setWindowsTerminalStarter("C:\\cygwin64\\bin\\bash -i");
            cygwinConfig.setFontSize(14);


//        Default Config
            TerminalConfig defaultConfig = new TerminalConfig();

            TerminalBuilder terminalBuilder = new TerminalBuilder(darkConfig);
            TerminalTab terminal = terminalBuilder.newTerminal();
//        terminal.onTerminalFxReady(() -> {
//            terminal.getTerminal().command("java -version\r");
//        });

            tabPane.getTabs().add(terminal);


        }


    }

}