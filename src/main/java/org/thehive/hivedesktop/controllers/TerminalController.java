package org.thehive.hivedesktop.controllers;

import com.jfoenix.controls.JFXListCell;
import com.kodedu.terminalfx.TerminalBuilder;
import com.kodedu.terminalfx.TerminalTab;
import com.kodedu.terminalfx.config.TerminalConfig;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.ResourceBundle;

public class TerminalController implements Initializable {
    public TabPane tabPane = new TabPane();

    @FXML
    private SplitPane rightSplitPane;

    @FXML
    private JFXListCell btnAttendeeDetails = new JFXListCell();


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
